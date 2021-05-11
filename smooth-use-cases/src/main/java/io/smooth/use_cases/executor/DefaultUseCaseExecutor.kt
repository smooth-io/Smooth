package io.smooth.use_cases.executor

import io.smooth.constraint.Constraint
import io.smooth.use_cases.*
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect

class DefaultUseCaseExecutor : UseCaseExecutor<DefaultUseCaseExecutor.RequestModifier> {

    class RequestModifier()

    override suspend fun <Request, Res, UC : UseCase<Request, Res>> execute(
        flow: FlowCollector<UseCaseResult<Request, Res>>,
        constraints: List<Constraint>?,
        useCase: UC,
        request: Request,
        requestModifier: RequestModifier?
    ) {
        try {
            useCase.execute(request)
                .collect {
                    handleExecutionResult(flow,request,it)
                }
        } catch (e: Throwable) {
            flow.emit(Failed(request, e))
        }
    }

    override fun handleConstraints(constraints: List<Constraint>, requestModifier: RequestModifier?): ConstraintsHandler =
        ConstraintsHandler(
            null,
            constraints
        )

    override fun newInstanceOfRequestModifier(): RequestModifier? = null

}