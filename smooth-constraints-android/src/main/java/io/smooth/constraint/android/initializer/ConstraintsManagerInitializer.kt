package io.smooth.constraint.android.initializer

import android.content.Context
import androidx.startup.Initializer
import io.smooth.constraint.ConstraintsService

class ConstraintsManagerInitializer : Initializer<ConstraintsService> {

    override fun create(context: Context): ConstraintsService {
        return ConstraintsService.getInstance()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = arrayListOf()

}