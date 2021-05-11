package io.ncorti.kotlin.template.app

import android.util.Log
import io.smooth.constraint.coroutine.SuspendedConfigConstraint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

class DelayedConstraint(coroutineScope: CoroutineScope) :
    SuspendedConfigConstraint<DelayedConstraint.Config>(
        coroutineScope
    ) {

    data class Config(
        val delay: Long
    )

    override fun setConfig(config: Config) {
        super.setConfig(config)
    }

    override fun configClass(): Class<Config> = Config::class.java

    override suspend fun checkSuspended(): Boolean {
        delay(config().delay)
        return true
    }

    override fun clear() {

    }

}