package io.smooth.use_cases.executor

import io.smooth.constraint.Constraint
import io.smooth.use_cases.*
import kotlinx.coroutines.flow.FlowCollector

interface UseCaseExecutor<RequestModifier> {

    suspend fun <Request, Res, UC : UseCase<Request, Res>> execute(
        flow: FlowCollector<UseCaseResult<Request, Res>>,
        constraints: List<Constraint<*>>?,
        useCase: UC,
        request: Request,
        requestModifier: RequestModifier?
    )

    /**
     * @return constraints that were not handled yet
     */
    fun handleConstraints(
        constraints: List<Constraint<*>>,
        requestModifier: RequestModifier?
    ): ConstraintsHandler

    fun newInstanceOfRequestModifier(): RequestModifier?

}