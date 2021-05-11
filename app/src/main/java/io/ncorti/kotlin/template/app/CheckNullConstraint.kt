package io.ncorti.kotlin.template.app

import io.smooth.constraint.config.BaseConfigConstraint

class CheckNullConstraint: BaseConfigConstraint<CheckNullConstraint.Config<*>>() {

    data class Config<T>(
        val t: T?
    )

    override fun configClass(): Class<Config<*>> = Config::class.java

    override fun check(): Boolean =
        config().t != null

    override fun clear() {
    }

}