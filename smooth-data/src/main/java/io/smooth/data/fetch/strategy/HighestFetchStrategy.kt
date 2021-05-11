package io.smooth.data.fetch.strategy

import io.smooth.data.fetch.FetchResult
import io.smooth.data.source.DataSource
import kotlinx.coroutines.flow.*

class HighestFetchStrategy<RequestData, Data> : FetchStrategy<RequestData, Data>() {

    override suspend fun fetch(
        requestData: RequestData,
        allSources: List<DataSource<RequestData, Data>>
    ): Flow<FetchResult<RequestData, Data>> = flow {
        fetchFromSource(requestData, allSources.last())
    }

}