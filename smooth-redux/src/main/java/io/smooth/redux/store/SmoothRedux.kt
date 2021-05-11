package io.smooth.redux.store

import io.smooth.redux.*
import io.smooth.redux.harmony.builder.RulesBuilder
import io.smooth.redux.SmoothState
import io.smooth.redux.effect.*
import io.smooth.redux.effect.handler.EffectHandler
import io.smooth.redux.effect.handler.EffectHandlerService
import io.smooth.redux.reducer.SmoothReducer
import io.smooth.redux.reducer.SmoothReducerOfReducers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.reduxkotlin.*
import javax.inject.Provider
import kotlin.reflect.KClass

abstract class SmoothRedux<Action>(
    middlewares: List<Middleware<SmoothState>>? = null,
    optionalReducers: List<SmoothReducer<*>>? = null,
    storeType: StoreTypes = StoreTypes.THREAD_SAFE,
) {

    val effectsService = EffectHandlerService()

    suspend inline fun <reified E : Effect> effectHandler(
        effectHandlerProvider: Provider<EffectHandler<E>>
    ) {
        effectsService.assignHandler(E::class, effectHandlerProvider)
    }

    private suspend fun <E : Effect> getEffectHandler(
        effectClass: KClass<E>
    ) = effectsService.getEffectHandler(effectClass)

    enum class StoreTypes {
        THREAD_SAFE, SAME_THREAD, NONE
    }

    protected abstract fun sections(): List<StateSection>
    protected abstract fun RulesBuilder.rules()

    private fun initialState(): SmoothState {
        val rulesBuilder = RulesBuilder()
        rulesBuilder.rules()

        return createState(sections(), rulesBuilder.getRules())
    }

    private val defaultReducers = arrayListOf(reducer(), EffectsReducer())

    private val reducerOfReducers = SmoothReducerOfReducers(
        optionalReducers?.plus(defaultReducers) ?: defaultReducers
    )

    protected abstract fun reducer(): SmoothReducer<Action>

    val store: Store<SmoothState> =
        when (storeType) {
            StoreTypes.SAME_THREAD -> createSameThreadEnforcedStore(
                reducerOfReducers,
                initialState(),
                middlewares?.toTypedArray()?.let {
                    applyMiddleware(*it)
                }
            )
            StoreTypes.THREAD_SAFE -> createThreadSafeStore(
                reducerOfReducers, initialState(),
                middlewares?.toTypedArray()?.let {
                    applyMiddleware(*it)
                }
            )
            StoreTypes.NONE -> createStore(
                reducerOfReducers, initialState(),
                middlewares?.toTypedArray()?.let {
                    applyMiddleware(*it)
                }
            )
        }

    fun action(
        action: Action
    ) = store.dispatch(action as Any)

    fun anyAction(
        action: Any
    ) = store.dispatch(action)

    fun effect(
        effect: Effect
    ) = store.effect(effect)

    suspend fun runSafe(
        block: suspend SafeRunner.() -> Unit
    ) = store.runSafe(block)

    private var _stateFlow: MutableStateFlow<StateResult>? = null

    data class StateResult(
        val sections: Map<String, StateSection>,
        val effects: List<EffectWithHandler<*>>
    )

    suspend fun getUpdates(): Flow<StateResult> {

        if (_stateFlow == null) {
            val state = store.getState.invoke()
            _stateFlow = MutableStateFlow(
                StateResult(
                    state.sections,
                    getEffects(state)
                )
            )
            listenForStateUpdates()
        }

        return _stateFlow!!
    }

    private suspend fun listenForStateUpdates() {
        store.subscribe {
            GlobalScope.launch {
                val state = store.getState.invoke()
                _stateFlow?.value = StateResult(
                    state.sections,
                    getEffects(state)
                )
            }
        }
    }

    private suspend fun getEffects(state: SmoothState): List<EffectWithHandler<*>> =
        state.effects
            .filter { !it.consumed }
            .map { effect ->
                EffectWithHandler(
                    effect,
                    (getEffectHandler(effect::class) as? Provider<EffectHandler<Effect>>)
                        ?: throw IllegalArgumentException("$effect doesn't have handler")
                )
            }


}