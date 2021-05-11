package io.smooth.data.modify

import io.smooth.data.source.DataSource

sealed class ModifyResult<RequestData, Data>(
    open val requestData: RequestData,
    open val source: DataSource<RequestData, Data>
)

data class SuccessResult<RequestData, Data>(
    override val requestData: RequestData,
    override val source: DataSource<RequestData, Data>
) : ModifyResult<RequestData, Data>(requestData, source)

data class FailedResult<RequestData, Data>(
    override val requestData: RequestData,
    override val source: DataSource<RequestData, Data>,
    val error: Throwable
) :
    ModifyResult<RequestData, Data>(requestData, source)
