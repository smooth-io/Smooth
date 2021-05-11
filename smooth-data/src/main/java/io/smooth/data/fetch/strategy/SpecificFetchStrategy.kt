package io.smooth.data.fetch.strategy

import io.smooth.data.error.SourceNotFoundError
import io.smooth.data.fetch.FetchResult
import io.smooth.data.source.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext

class SpecificFetchStrategy<RequestData, Data>(private val sourcesNames: List<String>) :
    FetchStrategy<RequestData, Data>() {

    override suspend fun fetch(
        requestData: RequestData,
        allSources: List<DataSource<RequestData, Data>>
    ): Flow<FetchResult<RequestData, Data>> = flow {
        sourcesNames.map { sourceName ->
            val source = allSources.firstOrNull {
                sourceName == it.name
            } ?: throw SourceNotFoundError(sourceName)

            fetchFromSource(requestData, source)
        }
    }

}