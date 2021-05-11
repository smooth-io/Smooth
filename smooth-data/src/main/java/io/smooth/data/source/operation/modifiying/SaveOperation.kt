package io.smooth.data.source.operation.modifiying

interface SaveOperation<RequestData, Data> : ModifyingOperation<RequestData, Data> {

    suspend fun save(requestData: RequestData, data: Data)

}