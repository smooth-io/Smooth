package io.smooth.constraint.android.initializer

import io.smooth.constraint.provider.ConstraintsProvider

interface ConstraintsConfig {

    fun provideConstraintsProvider(): ConstraintsProvider

}