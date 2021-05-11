package io.smooth.use_cases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

abstract class UseCase<Req, Res> {

    abstract suspend fun execute(request: Req): Flow<ExecutionResult<Res>>

    protected suspend fun <Res> FlowCollector<ExecutionResult<Res>>.success(
        result: Res
    ) {
        emit(SuccessResult(result))
    }

    protected suspend fun FlowCollector<ExecutionResult<*>>.error(
        error: Throwable
    ) {
        emit(ErrorResult<Any>(error))
    }

    protected suspend fun FlowCollector<ExecutionResult<*>>.progress(
        progress: Float,
        metaData: Map<String, *>
    ) {
        emit(ProgressResult<Any>(progress, metaData))
    }
}