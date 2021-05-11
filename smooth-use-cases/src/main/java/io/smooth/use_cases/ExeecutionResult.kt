package io.smooth.use_cases

sealed class ExecutionResult<Res>
data class SuccessResult<Res>(val result: Res) : ExecutionResult<Res>()
data class ErrorResult<Res>(val error: Throwable) : ExecutionResult<Res>()
data class ProgressResult<Res>(
    val progress: Float,
    val metaData: Map<String, *>
) : ExecutionResult<Res>()