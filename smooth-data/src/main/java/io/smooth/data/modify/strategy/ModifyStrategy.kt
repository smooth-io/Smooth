package io.smooth.data.modify.strategy

import io.smooth.data.modify.*
import io.smooth.data.source.DataSource
import io.smooth.data.source.operation.modifiying.DeleteOperation
import io.smooth.data.source.operation.modifiying.SaveOperation
import io.smooth.data.source.operation.modifiying.UpdateOperation
import kotlinx.coroutines.flow.*

interface ModifyStrategy<RequestData, Data> {

    var stopExecution: Boolean

    fun direction(): Direction

    suspend fun modifyOnlyMatching(
        request: ModifyRequest<RequestData, Data>,
        sources: List<DataSource<RequestData, Data>>
    ): Flow<ModifyResult<RequestData, Data>>

    suspend fun modify(
        request: ModifyRequest<RequestData, Data>,
        allSources: List<DataSource<RequestData, Data>>
    ): Flow<ModifyResult<RequestData, Data>> =
        modifyOnlyMatching(request,
            allSources.filter {
                when (request.requiredOperation) {
                    SaveOperation::class -> it.saveOperation != null
                    UpdateOperation::class -> it.updateOperation != null
                    DeleteOperation::class -> it.deleteOperation != null
                    else -> false
                }
            }
        ).map {
            if (it is FailedResult && (request.recoverOnFail || !request.resumeOnFail)) {
                stopExecution = true
            }
            it
        }

    suspend fun FlowCollector<ModifyResult<RequestData, Data>>.doOpOnSource(
        request: ModifyRequest<RequestData, Data>,
        source: DataSource<RequestData, Data>
    ) {
        if (stopExecution) return
        try {
            when (request.requiredOperation) {
                SaveOperation::class -> {
                    source.saveOperation?.save(request.requestData, request.data!!)
                }
                UpdateOperation::class -> {
                    source.updateOperation?.update(request.requestData, null, request.data!!)
                }
                DeleteOperation::class -> {
                    source.deleteOperation?.delete(request.requestData, request.data)
                }
            }
            emit(SuccessResult(request.requestData, source))
        } catch (e: Throwable) {
            emit(FailedResult(request.requestData, source, e))
        }
    }


}