package io.smooth.data

import io.smooth.data.coordinator.DataChangeResolutionType
import io.smooth.data.coordinator.ResourceCoordinator
import io.smooth.data.fetch.FailedResult
import io.smooth.data.fetch.FetchResult
import io.smooth.data.fetch.NoResult
import io.smooth.data.fetch.SuccessResult
import io.smooth.data.request.GetRequest
import io.smooth.data.request.Request
import io.smooth.data.source.DataSource
import io.smooth.store.Store
import io.smooth.store.memory.LRUInMemoryStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

abstract class BaseResourceCoordinator<RequestData, Data> : ResourceCoordinator<RequestData, Data> {

    private lateinit var sources: List<DataSource<RequestData, Data>>

    abstract fun initSources(): List<DataSource<RequestData, Data>>

    fun getSources(): List<DataSource<RequestData, Data>> = sources

    private val runningFlows =
        LRUInMemoryStore<Request, Flow<FetchResult<RequestData, Data>>>(2)

    init {
        preInit()
        sources = initSources().sortedBy { it.priority }
    }

    open fun preInit() {}

    override fun getLowerSource(source: DataSource<RequestData, Data>): DataSource<RequestData, Data>? {
        val index = sources.indexOf(source)
        return sources.getOrNull(index - 1)
    }

    override fun getUpperSource(source: DataSource<RequestData, Data>): DataSource<RequestData, Data>? {
        val index = sources.indexOf(source)
        return sources.getOrNull(index + 1)
    }

    fun getLastSource(): DataSource<RequestData, Data>? = sources.lastOrNull()
    fun getFirstSource(): DataSource<RequestData, Data>? = sources.firstOrNull()

    override fun areObjectsTheSame(first: Data, second: Data): Boolean =
        first == second

    protected suspend fun saveOperation(
        request: Request,
        flow: Flow<FetchResult<RequestData, Data>>
    ) = runningFlows.save(Store.SaveDto(request, flow))

    protected suspend fun getOperation(request: Request): Flow<FetchResult<RequestData, Data>>? =
        runningFlows.getById(request).firstOrNull()

    protected suspend fun updateUpperSources(
        getRequest: GetRequest<RequestData, Data>,
        successResult: SuccessResult<RequestData, Data>
    ) {
        var currentSource = getUpperSource(successResult.source)
        while (currentSource != null) {
            updateDataInSourceIfNeeded(getRequest, currentSource, successResult)
            currentSource = getLowerSource(currentSource)
        }
    }


    protected suspend fun updateLowerSources(
        getRequest: GetRequest<RequestData, Data>,
        successResult: SuccessResult<RequestData, Data>
    ) {
        var currentSource = getLowerSource(successResult.source)
        while (currentSource != null) {
            updateDataInSourceIfNeeded(getRequest, currentSource, successResult)
            currentSource = getLowerSource(currentSource)
        }
    }

    protected suspend fun updateDataInSourceIfNeeded(
        getRequest: GetRequest<RequestData, Data>,
        targetSource: DataSource<RequestData, Data>,
        successResult: SuccessResult<RequestData, Data>
    ) {
        val flow = flow {
            getRequest.fetchStrategy.apply {
                fetchFromSource(
                    successResult.requestData,
                    targetSource
                )
            }
        }

        val result = flow.firstOrNull()

        when (result) {
            is NoResult<*, *> -> {
                targetSource.saveOperation?.save(successResult.requestData, successResult.data)
            }
            is FailedResult<*, *> -> {
                targetSource.saveOperation?.save(successResult.requestData, successResult.data)
            }
            is SuccessResult<*, *> -> {
                val dataChangeResolutionType = dataChangeStrategy.onDataChanged(
                    this,
                    successResult.requestData,
                    successResult.source,
                    successResult.data,
                    targetSource,
                    result.data as Data
                )

                if (areObjectsReferenceMatching(result.data, successResult.data)) {
                    when (dataChangeResolutionType) {
                        DataChangeResolutionType.UPDATE -> {
                            if (areObjectsTheSame(result.data, successResult.data)) {
                                targetSource.updateOperation?.update(
                                    successResult.requestData,
                                    result.data,
                                    successResult.data
                                )
                            }
                        }
                        DataChangeResolutionType.INVALIDATE -> {
                            targetSource.deleteOperation?.delete(
                                successResult.requestData,
                                successResult.data
                            )
                        }
                        DataChangeResolutionType.IGNORE -> {
                        }
                    }

                } else {
                    targetSource.saveOperation?.save(successResult.requestData, successResult.data)
                }

            }

        }

    }


}