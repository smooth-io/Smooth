package io.smooth.redux

import io.smooth.redux.*
import io.smooth.redux.defaults.*
import io.smooth.redux.defaults.section.ContentSection
import io.smooth.redux.defaults.section.ErrorType
import io.smooth.redux.effect.Effect
import io.smooth.redux.harmony.builder.RulesBuilder
import io.smooth.use_cases.*
import io.smooth.use_cases.executor.DefaultUseCaseExecutor
import io.smooth.use_cases.provider.UseCasesProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.reflect.KClass

data class State(val s: String)

sealed class Action
class HiAction(val t: String) : Action()

fun main() {
    runBlocking {


        UseCaseService.init(
            object : UseCasesProvider {
                override fun <Req, Res, UC : UseCase<Req, Res>> getUseCase(useCaseClass: KClass<UC>): UC? =
                    TestUC() as UC
            }
        )

        UseCaseService.getInstance().addExecutor(
            DefaultUseCaseExecutor()
        )

        val errorTypeSpecifier = object : ErrorTypeSpecifier {
            override fun determineErrorType(
                state: SmoothState,
                error: Throwable,
                reason: Any
            ): ErrorType =
                ErrorType.HARD
        }

        val redux = object : DefaultRedux<State, Action>(errorTypeSpecifier) {

            override fun contentSection(): ContentSection<State> =
                ContentSection(
                    State(""), false
                )

            override fun extraSections(): List<StateSection>? = null

            override fun RulesBuilder.extraRules() {}

            override fun SmoothState.reduceState(action: Action): SmoothState =
                when (action) {
                    is HiAction ->
                        modifyContent<State> {
                            copy(s = action.t)
                        }
                }

            override fun canReduce(action: Any): Boolean = action is Action

        }

        redux.apply {

            getUpdates()
                .onEach {
                    it.effects.fetch {
                        println(this)
                    }

                    println(
                        it.sections.filter { it.value.isActive }
                    )

                    println("--------------")
                    println("")

                }.launchIn(this@runBlocking)

            runUseCase(
                DefaultUseCaseExecutor::class,
                TestUC::class,
                "hi",
                {
                    dispatch(
                        HiAction(it.res)
                    )
                }
            )

        }

    }

}

data class MyEffect(val s: String) : Effect()
