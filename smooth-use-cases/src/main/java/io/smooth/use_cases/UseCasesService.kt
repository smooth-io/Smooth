package io.smooth.use_cases

import io.smooth.constraint.*
import io.smooth.constraint.ConstraintsNotMet
import io.smooth.use_cases.error.UseCaseNotFoundError
import io.smooth.use_cases.executor.UseCaseExecutor
import io.smooth.use_cases.middleware.Middleware
import io.smooth.use_cases.provider.UseCasesProvider
import kotlinx.coroutines.flow.*
import kotlin.reflect.KClass

class UseCasesService(private val provider: UseCasesProvider) {

    fun getUseCase(
        useCaseClass: KClass<UseCase<Any, Any>>
    ): UseCase<*, *>? = provider.getUseCase(useCaseClass)

    private val executors: MutableMap<KClass<UseCaseExecutor<*>>, UseCaseExecutor<*>> =
        mutableMapOf()

    fun <Modification> addExecutor(executor: UseCaseExecutor<Modification>) {
        executors[executor::class as KClass<UseCaseExecutor<*>>] = executor
    }

    suspend fun <Req, Res, UC : UseCase<Req, Res>, RequestModifier> execute(
        executorClass: KClass<out UseCaseExecutor<RequestModifier>>,
        useCaseClass: KClass<UC>,
        request: Req,
        middlewares: List<Middleware<Req, Res, UC>>? = null,
        constraints: List<Constraint<*>>? = null,
        modify: RequestModifier.() -> Unit = {}
    ): Flow<UseCaseResult<Req, Res>> = flow {

        val executor = executors[executorClass] as UseCaseExecutor<RequestModifier>
            ?: throw IllegalArgumentException("${executorClass} must be added to executors")

        val requestModifier = executor.newInstanceOfRequestModifier()?.apply(modify)

        if (constraints != null) {
            val constraintsHandler = executor.handleConstraints(constraints, requestModifier)

            val nonHandledConstraints = constraintsHandler.nonHandledConstraints
            if (nonHandledConstraints != null) {
                ConstraintsService.getInstance()
                    .check(*nonHandledConstraints.toTypedArray())
                    .collect { constraintResult ->

                        when (constraintResult) {
                            is ConstraintsMet -> {
                                onConstraintsMet(
                                    executor,
                                    constraintsHandler.handledConstraints,
                                    useCaseClass,
                                    request,
                                    requestModifier
                                )
                            }
                            is ConstraintsNotMet -> {
                                emit(
                                    ConstraintsNotMet<Req, Res>(
                                        request,
                                        constraintResult.blockingConstraints
                                    )
                                )
                            }
                            is io.smooth.constraint.ConstraintsPending -> {
                                emit(
                                    ConstraintsPending<Req, Res>(
                                        request
                                    )
                                )
                            }
                        }
                    }
            } else {
                onConstraintsMet(
                    executor,
                    constraintsHandler.handledConstraints,
                    useCaseClass,
                    request,
                    requestModifier
                )
            }
        } else {
            onConstraintsMet(
                executor,
                null,
                useCaseClass,
                request,
                requestModifier
            )
        }
    }.map { result ->
        middlewares?.forEach {
            it.handleResult(useCaseClass, result)
        }
        result
    }

    private suspend fun <Req, Res, UC : UseCase<Req, Res>, RequestModifier> FlowCollector<UseCaseResult<Req, Res>>.onConstraintsMet(
        executor: UseCaseExecutor<RequestModifier>,
        constraints: List<Constraint<*>>?,
        useCaseClass: KClass<UC>,
        request: Req,
        requestModifier: RequestModifier?
    ) {
        emit(Executing(request))
        val useCase = provider.getUseCase(useCaseClass)
        if (useCase == null) {
            emit(
                Failed(
                    request, UseCaseNotFoundError(useCaseClass as KClass<UseCase<*, *>>)
                )
            )
            return
        }

        executor.execute(
            this,
            constraints,
            useCase,
            request,
            requestModifier
        )
    }

    companion object {
        private var instance: UseCasesService? = null

        fun init(provider: UseCasesProvider) {
            instance = UseCasesService(provider)
        }

        fun getInstance(): UseCasesService = instance!!
    }

}


