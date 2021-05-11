package io.smooth.data.source.validation

interface ValidationStrategy<RequestData, Data> {

    suspend fun isValid(requestData: RequestData, data: Data): ResolutionType

}