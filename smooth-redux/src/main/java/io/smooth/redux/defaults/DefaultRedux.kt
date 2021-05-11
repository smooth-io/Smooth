package io.smooth.redux.defaults

import io.smooth.redux.SafeRunner
import io.smooth.redux.SmoothState
import io.smooth.redux.StateSection
import io.smooth.redux.defaults.section.ContentSection
import io.smooth.redux.defaults.section.ProcessingType
import io.smooth.redux.harmony.builder.RulesBuilder
import io.smooth.redux.reducer.SmoothReducer
import io.smooth.redux.runSafe
import io.smooth.redux.store.SmoothRedux
import org.reduxkotlin.Middleware

abstract class DefaultRedux<Content, Action>(
    errorTypeSpecifier: ErrorTypeSpecifier,
    private val hideContentOnProcessing: Boolean = true,
    middlewares: List<Middleware<SmoothState>>? = null,
    optionalReducers: List<SmoothReducer<*>>? = null,
    storeType: StoreTypes = StoreTypes.THREAD_SAFE
) : SmoothRedux<Action>(
    middlewares,
    optionalReducers?.plusElement(DefaultReducer(errorTypeSpecifier))
        ?: arrayListOf(DefaultReducer(errorTypeSpecifier)),
    storeType
) {

    abstract fun SmoothState.reduceState(action: Action): SmoothState

    abstract fun canReduce(action: Any): Boolean

    override fun reducer(): SmoothReducer<Action> =
        object : SmoothReducer<Action> {
            override fun canReduce(action: Any): Boolean =
                this@DefaultRedux.canReduce(action)

            override fun SmoothState.reduce(action: Action): SmoothState =
                this.reduceState(action)
        }

    abstract fun contentSection(): ContentSection<Content>

    abstract fun extraSections(): List<StateSection>?

    override fun sections(): List<StateSection> =
        defaultSections().toMutableList().apply {
            add(contentSection())
            extraSections()?.also {
                addAll(it)
            }
        }

    override fun RulesBuilder.rules() {
        applyDefaultRules(hideContentOnProcessing)
        extraRules()
    }

    abstract fun RulesBuilder.extraRules()

    suspend fun runLce(
        reason: Any,
        block: suspend SafeRunner.() -> Unit
    ) = store.runSafe {

        dispatch(
            ProcessingAction(
                ProcessingType.NORMAL, 0f, reason
            )
        )

        try {
            runSafe(block)
        } catch (e: Throwable) {
            dispatch(
                ErrorAction(
                    e,
                    reason,
                    object : Recoverable {
                        override suspend fun recover() {
                            restartWork(reason, block)
                        }
                    }
                )
            )
        }

    }

    private suspend fun restartWork(reason: Any, block: suspend SafeRunner.() -> Unit) {
        runLce(reason, block)
    }

}