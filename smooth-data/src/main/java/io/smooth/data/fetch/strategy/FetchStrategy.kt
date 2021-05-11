package io.smooth.data.fetch.strategy

import io.smooth.data.fetch.FailedResult
import io.smooth.data.fetch.FetchResult
import io.smooth.data.fetch.NoResult
import io.smooth.data.fetch.SuccessResult
import io.smooth.data.source.DataSource
import kotlinx.coroutines.flow.*

abstract class FetchStrategy<RequestData, Data> {

    abstract suspend fun fetch(
        requestData: RequestData,
        allSources: List<DataSource<RequestData, Data>>
    ): Flow<FetchResult<RequestData, Data>>

    suspend fun FlowCollector<FetchResult<RequestData, Data>>.fetchFromSource(
        requestData: RequestData,
        source: DataSource<RequestData, Data>
    ) {
        try {
            source.getOperation.get(requestData)
                .collect {
                    if (it == null) {
                        emit(NoResult(requestData, source))
                    } else {
                        emit(source.resolveData(SuccessResult(requestData, source, it)))
                    }
                }
        } catch (e: Throwable) {
            emit(FailedResult(requestData, source, e))
        }
    }

}