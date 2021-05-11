package io.smooth.data.source.operation

import kotlinx.coroutines.flow.Flow

interface GetOperation<RequestData, Data> : Operation<RequestData, Data> {

    suspend fun get(requestData: RequestData): Flow<Data?>

}