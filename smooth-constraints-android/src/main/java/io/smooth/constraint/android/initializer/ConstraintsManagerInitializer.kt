package io.smooth.constraint.android.initializer

import android.content.Context
import androidx.startup.Initializer
import io.smooth.constraint.manager.ConstraintsManager

class ConstraintsManagerInitializer : Initializer<ConstraintsManager> {

    override fun create(context: Context): ConstraintsManager {
        if (context is ConstraintsConfig) {
            ConstraintsManager.init(context.provideConstraintsProvider())
        } else {
            throw IllegalArgumentException("Application class must implement ${ConstraintsConfig::class} in order for constraints manager to work")
        }
        return ConstraintsManager.getInstance()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = arrayListOf()

}