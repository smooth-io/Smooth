package io.smooth.constraint.resolution

import io.smooth.constraint.Constraint
import io.smooth.constraint.ConstraintsService
import javax.inject.Provider
import kotlin.reflect.KClass

suspend inline fun <reified C : Constraint<C>> forConstraint(
    block: ConstraintResolutionBuilder<C>.() -> Unit
) {
    val builder = ConstraintResolutionBuilder<C>()
    block(builder)

    ConstraintsService.getInstance().addConstraintsResolutions(
        C::class,
        builder.getResolutionsProviders()
    )
}

class ConstraintResolutionBuilder<C : Constraint<C>> {

    private val resolutionsProviders: MutableList<Provider<ConstraintResolution<C, *>>> =
        arrayListOf()

    fun resolution(vararg resolution: Provider<ConstraintResolution<C, *>>) {
        resolutionsProviders.addAll(resolution)
    }

    fun getResolutionsProviders(): List<Provider<out ConstraintResolution<out C, *>>> =
        resolutionsProviders

}