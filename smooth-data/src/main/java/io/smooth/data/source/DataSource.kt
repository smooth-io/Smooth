package io.smooth.data.source

import io.smooth.data.error.DataDeletedError
import io.smooth.data.error.DataIgnoredError
import io.smooth.data.fetch.FailedResult
import io.smooth.data.fetch.FetchResult
import io.smooth.data.fetch.SuccessResult
import io.smooth.data.source.operation.modifiying.DeleteOperation
import io.smooth.data.source.operation.GetOperation
import io.smooth.data.source.operation.modifiying.SaveOperation
import io.smooth.data.source.operation.modifiying.UpdateOperation
import io.smooth.data.source.validation.ResolutionType
import io.smooth.data.source.validation.ValidationStrategy

open class DataSource<RequestData, Data>(
    val name: String,
    val priority: Int,
    val getOperation: GetOperation<RequestData, Data>
) {

    var defaultRetry: Int = 0
    var validationStrategies: MutableList<ValidationStrategy<RequestData, Data>> = arrayListOf()
    open var saveOperation: SaveOperation<RequestData, Data>? = null
    open var updateOperation: UpdateOperation<RequestData, Data>? = null
    open var deleteOperation: DeleteOperation<RequestData, Data>? = null

    protected fun addValidationStrategy(validationStrategy: ValidationStrategy<RequestData, Data>) {
        this.validationStrategies.add(validationStrategy)
    }

    private suspend fun getResolutionTypeForResource(
        requestData: RequestData,
        data: Data
    ): ResolutionType {
        var highestResolutionType: Int = ResolutionType.VALID.ordinal
        validationStrategies.forEach {
            val resolutionType = it.isValid(requestData, data)
            if (resolutionType.ordinal > highestResolutionType) {
                highestResolutionType = resolutionType.ordinal
            }
        }
        return ResolutionType.values()[highestResolutionType]
    }

    internal suspend fun resolveData(
        result: SuccessResult<RequestData, Data>
    ): FetchResult<RequestData, Data> {

        val resolutionType =
            result.source.getResolutionTypeForResource(result.requestData, result.data)

        return when (resolutionType) {
            ResolutionType.VALID -> result
            ResolutionType.IGNORE -> FailedResult(
                result.requestData,
                result.source,
                DataIgnoredError(result.data as Any)
            )
            ResolutionType.DELETE -> {
                result.source.deleteOperation?.delete(result.requestData, result.data)
                FailedResult(
                    result.requestData,
                    result.source,
                    DataDeletedError(result.data as Any)
                )
            }
        }
    }

}