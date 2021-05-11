package io.smooth.data.fetch

import io.smooth.data.source.DataSource

sealed class FetchResult<RequestData, Data>(
    open val requestData: RequestData,
    open val source: DataSource<RequestData, Data>
)

data class NoResult<RequestData, Data>(
    override val requestData: RequestData,
    override val source: DataSource<RequestData, Data>
) : FetchResult<RequestData, Data>(requestData, source)

data class SuccessResult<RequestData, Data>(
    override val requestData: RequestData,
    override val source: DataSource<RequestData, Data>,
    val data: Data
) :
    FetchResult<RequestData, Data>(requestData, source)

data class FailedResult<RequestData, Data>(
    override val requestData: RequestData,
    override val source: DataSource<RequestData, Data>,
    val error: Throwable
) :
    FetchResult<RequestData, Data>(requestData, source)
