package io.smooth.redux.store

import io.smooth.redux.SmoothState
import io.smooth.redux.StateSection
import io.smooth.redux.*
import io.smooth.redux.defaults.DefaultRedux
import io.smooth.redux.defaults.ErrorTypeSpecifier
import io.smooth.redux.defaults.modifyContent
import io.smooth.redux.defaults.section.ContentSection
import io.smooth.redux.defaults.section.ErrorType
import io.smooth.redux.effect.Effect
import io.smooth.redux.effect.handler.EffectHandlerService
import io.smooth.redux.harmony.builder.RulesBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

data class State(val s: String)

sealed class Action
class HiAction(val t: String) : Action()

fun main() {
    runBlocking {

        val errorTypeSpecifier = object: ErrorTypeSpecifier{
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
//
//        var c = 0
//        val redux = object : SmoothRedux<Action>() {
//            override fun reducer(): SmoothReducer<Action> =
//                object : SmoothReducer<Action> {
//                    override fun reduce(state: SmoothState, action: Action): SmoothState =
//                        state.modify<ProgressSection> {
//                            c++
//                            copy(
//                                progress = c * 10,
//                                isActive = true
//                            )
//                        }.activate<ContentSection>()
//
//                    override fun canReduce(action: Any): Boolean =
//                        action is Action
//                }
//
//            override fun sections(): List<StateSection> =
//                arrayListOf(
//                    ProgressSection(0, false),
//                    ContentSection(null, arrayListOf(), false)
//                )
//
//            override fun RulesBuilder.rules() {
//                onActivated<ContentSection> {
//                    deactivate(
//                        byType(SmoothStateType.Error),
//                        byName("haha")
//                    )
//                }
//                onDeactivated<ProgressSection> {
//                    activate(
//                        bySection(ContentSection::class)
//                    )
//                }
//            }
//
//        }

        redux.apply {

            val s = EffectHandlerService()

            getUpdates()
                .distinctUntilChanged()
                .onEach {

                    it.effects.forEach {
                        println(this)
                    }

                    println(
                        it.sections
                    )

                }.launchIn(this@runBlocking)

            runLce("iam") {
                delay(1000)
                action(HiAction("sss"))
            }

//            action(HiAction("sss1"))
//            action(HiAction("sss2"))
//            action(HiAction("sss3"))
//            effect(MyEffect("hahaha"))
//            action(HiAction("sss4"))
//            effect(MyEffect("wee"))
        }

    }

}

data class MyEffect(val s: String) : Effect()
