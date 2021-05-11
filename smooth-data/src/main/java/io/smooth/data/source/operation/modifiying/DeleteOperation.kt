package io.smooth.data.source.operation.modifiying

interface DeleteOperation<RequestData, Data> : ModifyingOperation<RequestData, Data> {

    suspend fun delete(requestData: RequestData, data: Data?): Data?

}