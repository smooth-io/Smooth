package io.smooth.data.modify.strategy

import io.smooth.data.modify.Direction
import io.smooth.data.modify.ModifyRequest
import io.smooth.data.modify.ModifyResult
import io.smooth.data.source.DataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class LowToHighModifyStrategy<RequestData, Data> : ModifyStrategy<RequestData, Data> {

    override fun direction(): Direction = Direction.UPPER

    override var stopExecution: Boolean = false

    override suspend fun modifyOnlyMatching(
        request: ModifyRequest<RequestData, Data>,
        sources: List<DataSource<RequestData, Data>>
    ): Flow<ModifyResult<RequestData, Data>> = flow {
        sources.forEach {
            doOpOnSource(request, it)
        }
    }


}