package io.smooth.redux

import io.smooth.constraint.Constraint
import io.smooth.redux.defaults.DefaultRedux
import io.smooth.redux.defaults.ErrorAction
import io.smooth.redux.defaults.ProcessingAction
import io.smooth.redux.defaults.Recoverable
import io.smooth.redux.defaults.section.ProcessingType
import io.smooth.redux.error.ConstraintsNotMetError
import io.smooth.use_cases.*
import io.smooth.use_cases.executor.UseCaseExecutor
import io.smooth.use_cases.middleware.Middleware
import kotlinx.coroutines.flow.collect
import kotlin.reflect.KClass

suspend fun <Req, Res, UC : UseCase<Req, Res>, RequestModifier> DefaultRedux<*, *>.runUseCase(
    executorClass: KClass<out UseCaseExecutor<RequestModifier>>,
    useCaseClass: KClass<UC>,
    request: Req,
    onSuccess: suspend SafeRunner.(result: Success<Req, Res>) -> Unit,
    middlewares: List<Middleware<Req, Res, UC>>? = null,
    constraints: List<Constraint>? = null,
    modify: RequestModifier.() -> Unit = {}
) = runLce(useCaseClass) {

    UseCaseService.getInstance()
        .execute(
            executorClass,
            useCaseClass,
            request, middlewares, constraints, {
                modify.invoke(this)
            }
        ).collect {
            when (it) {
                is ConstraintsNotMet -> {
                    dispatch(
                        ErrorAction(
                            ConstraintsNotMetError(
                                it.blockingConstraints
                            ),
                            useCaseClass,
                            null
                        )
                    )
                }
                is ConstraintsPending -> {
                    dispatch(
                        ProcessingAction(
                            ProcessingType.NORMAL, 0f, useCaseClass
                        )
                    )
                }
                is Executing -> {
                    dispatch(
                        ProcessingAction(
                            ProcessingType.NORMAL, 0f, useCaseClass
                        )
                    )
                }
                is Failed -> {
                    dispatch(
                        ErrorAction(
                            it.error,
                            useCaseClass,
                            object : Recoverable {
                                override suspend fun recover() {
                                    this@runUseCase.restartWork(
                                        executorClass,
                                        useCaseClass,
                                        request,
                                        onSuccess,
                                        middlewares,
                                        constraints,
                                        modify
                                    )
                                }
                            }
                        )
                    )
                }
                is Progress -> {
                    dispatch(
                        ProcessingAction(
                            ProcessingType.PROGRESSIVE,
                            it.progress,
                            useCaseClass,
                            it.metaData
                        )
                    )
                }
                is Success -> {
                    onSuccess.invoke(this, it)
                }
            }
        }

}

suspend fun <Req, Res, UC : UseCase<Req, Res>, RequestModifier> DefaultRedux<*, *>.restartWork(
    executorClass: KClass<out UseCaseExecutor<RequestModifier>>,
    useCaseClass: KClass<UC>,
    request: Req,
    onSuccess: suspend SafeRunner.(result: Success<Req, Res>) -> Unit,
    middlewares: List<Middleware<Req, Res, UC>>? = null,
    constraints: List<Constraint>? = null,
    modify: RequestModifier.() -> Unit = {}
) {
    runUseCase(executorClass, useCaseClass, request, onSuccess, middlewares, constraints, modify)
}