package io.smooth.data.request

import io.smooth.data.SaveStrategy
import io.smooth.data.fetch.strategy.FetchStrategy

data class GetRequest<RequestData, Data>(
    val requestData: RequestData,
    val fetchStrategy: FetchStrategy<RequestData, Data>
): Request {
    var deduplicateRequest: Boolean = true
    var getSameResultOnlyOnce: Boolean = true
    var saveStrategy: SaveStrategy = SaveStrategy.LOWER
}