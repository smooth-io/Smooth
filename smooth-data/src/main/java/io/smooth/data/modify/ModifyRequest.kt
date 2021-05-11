package io.smooth.data.modify

import io.smooth.data.source.operation.modifiying.ModifyingOperation
import kotlin.reflect.KClass

data class ModifyRequest<RequestData, Data>(
    val requestData: RequestData,
    val data: Data?,
    internal val requiredOperation: KClass<out ModifyingOperation<*,*>>
) {
    var resumeOnFail: Boolean = false
    var recoverOnFail: Boolean = false
}