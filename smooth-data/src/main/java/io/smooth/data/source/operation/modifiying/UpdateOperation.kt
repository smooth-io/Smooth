package io.smooth.data.source.operation.modifiying

interface UpdateOperation<RequestData, Data> : ModifyingOperation<RequestData, Data> {

    suspend fun update(requestData: RequestData, oldData: Data?, newData: Data)

}