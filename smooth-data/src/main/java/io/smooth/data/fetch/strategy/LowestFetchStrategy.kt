package io.smooth.data.fetch.strategy

import io.smooth.data.fetch.FetchResult
import io.smooth.data.source.DataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

class LowestFetchStrategy<RequestData, Data> : FetchStrategy<RequestData, Data>() {

    override suspend fun fetch(
        requestData: RequestData,
        allSources: List<DataSource<RequestData, Data>>
    ): Flow<FetchResult<RequestData, Data>> = flow {
        fetchFromSource(requestData, allSources.first())
    }

}