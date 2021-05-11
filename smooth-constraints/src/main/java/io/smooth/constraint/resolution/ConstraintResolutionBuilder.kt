package io.smooth.constraint.resolution

import io.smooth.constraint.Constraint
import io.smooth.constraint.ConstraintsService
import javax.inject.Provider
import kotlin.reflect.KClass

suspend inline fun <reified C : Constraint> forConstraint(
    block: ConstraintResolutionBuilder<C>.() -> Unit
) {
    val builder = ConstraintResolutionBuilder(C::class)
    block(builder)
    builder.getResolutionsProviders().forEach {
        ConstraintsService.getInstance().addConstraintResolution(
            C::class as KClass<Constraint>,
            it
        )
    }
}

class ConstraintResolutionBuilder<C : Constraint>(
    private val constraintClass: KClass<C>
) {

    private val resolutionsProviders: MutableList<Provider<ConstraintResolution<*>>> = arrayListOf()
    fun resolutions(vararg resolution: Provider<ConstraintResolution<*>>) {
        resolutionsProviders.addAll(resolution)
    }

    fun getResolutionsProviders(): List<Provider<ConstraintResolution<*>>> =
        resolutionsProviders

}