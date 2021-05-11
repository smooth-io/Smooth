package io.smooth.use_cases

import kotlinx.coroutines.flow.FlowCollector

suspend fun <Req, Res> handleExecutionResult(
    flow: FlowCollector<UseCaseResult<Req, Res>>,
    req: Req,
    result: ExecutionResult<Res>
) {
    when (result) {
        is ErrorResult ->
            flow.emit(Failed(req, result.error))
        is ProgressResult ->
            flow.emit(Progress(req, result.progress, result.metaData))
        is SuccessResult ->
            flow.emit(Success(req, result.result))
    }
}


