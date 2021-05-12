package io.smooth.constraint.resolution

import io.smooth.constraint.Constraint
import io.smooth.store.Store
import io.smooth.store.memory.PerpetualInMemoryStore
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Provider
import kotlin.reflect.KClass

internal class ConstraintResolutionService {

    private val resolutionsStore =
        PerpetualInMemoryStore<KClass<out Constraint<*>>, MutableList<Provider<out ConstraintResolution<*, *>>>>()


    internal suspend fun <C : Constraint<C>> addConstraintsResolutions(
        constraint: Constraint<C>
    ) {
        val resolutions = constraint.resolutions() ?: return
        addConstraintsResolutions(constraint::class, resolutions)
    }

    internal suspend fun addConstraintsResolutions(
        constraintClass: KClass<out Constraint<*>>,
        resolutions: List<Provider<out ConstraintResolution<out Constraint<*>, *>>>
    ) {
        var resolutionsProviders = resolutionsStore.getById(constraintClass).firstOrNull()
        if (resolutionsProviders == null) {
            resolutionsProviders = resolutions.toMutableList()
        } else {
            resolutionsProviders.addAll(resolutions)
        }
        resolutionsStore.save(
            Store.SaveDto(constraintClass, resolutionsProviders)
        )
    }


    internal suspend fun <C : Constraint<C>> addConstraintResolution(
        constraintClass: KClass<C>,
        resolutionProvider: Provider<out ConstraintResolution<C, *>>
    ) {
        var resolutionsProviders = resolutionsStore.getById(constraintClass).firstOrNull()
        if (resolutionsProviders == null) {
            resolutionsProviders = arrayListOf()
        }
        resolutionsProviders.add(resolutionProvider)
        resolutionsStore.save(
            Store.SaveDto(constraintClass, resolutionsProviders)
        )
    }

    internal suspend fun <C : Constraint<C>> getConstraintResolutions(constraintClass: KClass<C>): List<Provider<out ConstraintResolution<C, *>>>? =
        resolutionsStore.getById(constraintClass).firstOrNull()?.map {
            it as Provider<ConstraintResolution<C, Any>>
        }


}