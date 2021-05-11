package io.ncorti.kotlin.template.app.use

import io.ncorti.kotlin.template.app.AlwaysTrueConstraint
import io.smooth.config.ConfigProvider
import io.smooth.constraint.Constraint
import io.smooth.constraint.provider.ConstraintsProvider
import kotlin.reflect.KClass

class MyConstraintsProvider(
    private val configProvider: ConfigProvider
) : ConstraintsProvider(configProvider) {

    override fun getConstraint(constraintClass: KClass<out Constraint>): Constraint =
        AlwaysTrueConstraint()

}