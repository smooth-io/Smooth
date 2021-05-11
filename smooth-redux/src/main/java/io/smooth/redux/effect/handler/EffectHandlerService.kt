package io.smooth.redux.effect.handler

import io.smooth.redux.effect.Effect
import io.smooth.store.Store
import io.smooth.store.memory.PerpetualInMemoryStore
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Provider
import kotlin.reflect.KClass


class EffectHandlerService {

    private val eventsHandlerStore =
        PerpetualInMemoryStore<KClass<out Effect>, Provider<EffectHandler<*>>>()

    suspend fun <E : Effect> assignHandler(
        effectClass: KClass<E>,
        handlerProvider: Provider<EffectHandler<E>>
    ) {
        eventsHandlerStore.save(
            Store.SaveDto(effectClass, handlerProvider as Provider<EffectHandler<*>>)
        )
    }

    suspend fun <E : Effect> getEffectHandler(effectClass: KClass<E>): Provider<EffectHandler<E>>? =
        eventsHandlerStore.getById(effectClass).firstOrNull() as? Provider<EffectHandler<E>>

}