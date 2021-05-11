package io.smooth.data

import io.smooth.data.error.recover.RecoverFailed
import io.smooth.data.fetch.FetchResult
import io.smooth.data.fetch.SuccessResult
import io.smooth.data.fetch.strategy.FetchStrategy
import io.smooth.data.fetch.strategy.HighToLowFetchStrategy
import io.smooth.data.fetch.strategy.LowToHighFetchStrategy
import io.smooth.data.modify.*
import io.smooth.data.modify.strategy.LowToHighModifyStrategy
import io.smooth.data.modify.strategy.ModifyStrategy
import io.smooth.data.request.GetRequest
import io.smooth.data.source.DataSource
import io.smooth.data.source.operation.modifiying.DeleteOperation
import io.smooth.data.source.operation.modifiying.ModifyingOperation
import io.smooth.data.source.operation.modifiying.SaveOperation
import io.smooth.data.source.operation.modifiying.UpdateOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass


abstract class ResourceManager<RequestData, Data> : BaseResourceCoordinator<RequestData, Data>() {

    suspend fun fetch(
        requestData: RequestData,
        fetchStrategy: FetchStrategy<RequestData, Data> = LowToHighFetchStrategy(),
        block: GetRequest<RequestData, Data>.() -> Unit = {}
    ): Flow<FetchResult<RequestData, Data>> {
        val request = GetRequest(requestData, fetchStrategy)
        block(request)
        var flow = getOperation(request)
        if (request.deduplicateRequest && flow != null) return flow

        flow = request.fetchStrategy.fetch(request.requestData, getSources())
            .distinctUntilChanged { old, new ->
                if (request.getSameResultOnlyOnce) false
                else if (old is SuccessResult<RequestData, Data> && new is SuccessResult<RequestData, Data>) areObjectsTheSame(
                    old.data,
                    new.data
                )
                else false
            }.transform {
                emit(it)
                if (it is SuccessResult<RequestData, Data>) {
                    withContext(Dispatchers.IO) {
                        try {
                            coordinateFetchResult(request, it)
                        } catch (e: Throwable) {

                        }
                    }
                }
            }

        try {
            if (request.deduplicateRequest) {
                flow = flow.shareIn(
                    GlobalScope, SharingStarted.WhileSubscribed(), 1
                )
                saveOperation(request, flow)
            }
        } catch (e: Throwable) {
        }

        return flow!!
    }

    private suspend fun coordinateFetchResult(
        getRequest: GetRequest<RequestData, Data>,
        successResult: SuccessResult<RequestData, Data>
    ) {
        val source = successResult.source

        when (getRequest.saveStrategy) {
            SaveStrategy.LOWER -> updateLowerSources(getRequest, successResult)
            SaveStrategy.HIGHER -> updateUpperSources(getRequest, successResult)
            SaveStrategy.ALL -> {
                updateLowerSources(getRequest, successResult)
                updateUpperSources(getRequest, successResult)
            }
            SaveStrategy.ONLY_LOWEST -> {
                getFirstSource()?.apply {
                    if (this != source) {
                        updateDataInSourceIfNeeded(getRequest, this, successResult)
                    }
                }
            }
            SaveStrategy.ONLY_HIGHEST -> {
                getLastSource()?.apply {
                    if (this == source) {
                        updateDataInSourceIfNeeded(getRequest, this, successResult)
                    }
                }
            }
        }
    }


    suspend fun save(
        requestData: RequestData,
        data: Data,
        modifyStrategy: ModifyStrategy<RequestData, Data> = LowToHighModifyStrategy(),
        block: ModifyRequest<RequestData, Data>.() -> Unit = {}
    ) = modify(requestData, data, SaveOperation::class, modifyStrategy, block)

    suspend fun update(
        requestData: RequestData,
        data: Data?,
        modifyStrategy: ModifyStrategy<RequestData, Data> = LowToHighModifyStrategy(),
        block: ModifyRequest<RequestData, Data>.() -> Unit = {}
    ) = modify(requestData, data, UpdateOperation::class, modifyStrategy, block)

    suspend fun delete(
        requestData: RequestData,
        data: Data?,
        modifyStrategy: ModifyStrategy<RequestData, Data> = LowToHighModifyStrategy(),
        block: ModifyRequest<RequestData, Data>.() -> Unit = {}
    ) = modify(requestData, data, DeleteOperation::class, modifyStrategy, block)


    private suspend fun modify(
        requestData: RequestData,
        data: Data?,
        requiredOperation: KClass<out ModifyingOperation<*, *>>,
        modifyStrategy: ModifyStrategy<RequestData, Data> = LowToHighModifyStrategy(),
        block: ModifyRequest<RequestData, Data>.() -> Unit = {}
    ): Flow<ModifyResult<RequestData, Data>> {
        val request = ModifyRequest(requestData, data, requiredOperation)
        block(request)

        var dataToRecover: Map<DataSource<RequestData, Data>, Data?>? = null
        if (request.recoverOnFail) {
            try {
                dataToRecover = getFromSourceIfApplicable(
                    request.requiredOperation, requestData
                )
            } catch (e: Throwable) {
            }
        }

        return modifyStrategy.modify(request, getSources()).map {
            if (it is FailedResult && request.recoverOnFail) {
                try {
                    recoverOnFailure(request, it.source, modifyStrategy.direction(), dataToRecover)
                } catch (e: Throwable) {

                }
            }
            it
        }
    }

    internal suspend fun getFromSourceIfApplicable(
        requiredOperation: KClass<out ModifyingOperation<*, *>>,
        requestData: RequestData
    ): Map<DataSource<RequestData, Data>, Data?> {
        val map: MutableMap<DataSource<RequestData, Data>, Data?> = mutableMapOf()
        getSources().forEach {
            when (requiredOperation) {
                SaveOperation::class -> {
                    if (it.saveOperation != null) {
                        map[it] = it.getOperation.get(requestData).firstOrNull()
                    }
                }
                UpdateOperation::class -> {
                    if (it.updateOperation != null) {
                        map[it] = it.getOperation.get(requestData).firstOrNull()
                    }
                }
                DeleteOperation::class -> {
                    if (it.deleteOperation != null) {
                        map[it] = it.getOperation.get(requestData).firstOrNull()
                    }
                }
            }
        }

        return map
    }

    private suspend fun recoverOnFailure(
        request: ModifyRequest<RequestData, Data>, failedSource: DataSource<RequestData, Data>,
        direction: Direction,
        dataToRecover: Map<DataSource<RequestData, Data>, Data?>?
    ) {
        when (direction) {
            Direction.UPPER -> {
                var currentSource: DataSource<RequestData, Data>? = failedSource
                while (currentSource != null) {
                    try {
                        recoverDataInSource(
                            request,
                            currentSource,
                            dataToRecover?.get(currentSource)
                        )
                    } catch (e: Throwable) {
                    }
                    currentSource = getUpperSource(currentSource)
                }
            }
            Direction.LOWER -> {
                var currentSource: DataSource<RequestData, Data>? = failedSource
                while (currentSource != null) {
                    try {
                        recoverDataInSource(
                            request,
                            currentSource,
                            dataToRecover?.get(currentSource)
                        )
                    } catch (e: Throwable) {
                    }
                    currentSource = getLowerSource(currentSource)
                }
            }
            Direction.NONE -> {
                try {
                    recoverDataInSource(request, failedSource, dataToRecover?.get(failedSource))
                } catch (e: Throwable) {
                }
            }
        }

    }

    private suspend fun recoverDataInSource(
        request: ModifyRequest<RequestData, Data>,
        source: DataSource<RequestData, Data>,
        data: Data?
    ) {

        val oppositeOperation: KClass<out ModifyingOperation<*, *>> =
            OPPOSITE_OPERATIONS[request.requiredOperation] ?: return

        when (oppositeOperation) {
            SaveOperation::class -> {
                if (data == null) throw RecoverFailed()
                source.saveOperation?.save(request.requestData, data)
            }
            UpdateOperation::class -> {
                if (data == null) throw RecoverFailed()
                source.updateOperation?.update(request.requestData, null, data)
            }
            DeleteOperation::class -> {
                source.deleteOperation?.delete(request.requestData, data)
            }
        }
    }

}