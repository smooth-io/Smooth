package io.smooth.data.modify.strategy

import io.smooth.data.error.SourceNotFoundError
import io.smooth.data.modify.Direction
import io.smooth.data.modify.ModifyRequest
import io.smooth.data.modify.ModifyResult
import io.smooth.data.source.DataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SpecificModifyStrategy<RequestData, Data>(private val sourcesNames: List<String>) :
    ModifyStrategy<RequestData, Data> {

    override fun direction(): Direction = Direction.NONE

    override var stopExecution: Boolean = false

    override suspend fun modifyOnlyMatching(
        request: ModifyRequest<RequestData, Data>,
        sources: List<DataSource<RequestData, Data>>
    ): Flow<ModifyResult<RequestData, Data>> = flow {
        sourcesNames.map { sourceName ->
            val source = sources.firstOrNull {
                sourceName == it.name
            } ?: throw SourceNotFoundError(sourceName)

            doOpOnSource(request, source)
        }

    }


}