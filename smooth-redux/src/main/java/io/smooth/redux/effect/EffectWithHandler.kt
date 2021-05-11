package io.smooth.redux.effect

import io.smooth.redux.effect.handler.EffectHandler
import javax.inject.Provider

data class EffectWithHandler<E : Effect>(
    val effect: E,
    val handlerProvider: Provider<EffectHandler<E>>
)