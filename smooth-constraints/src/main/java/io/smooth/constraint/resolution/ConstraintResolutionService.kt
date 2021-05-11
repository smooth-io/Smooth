package io.smooth.constraint.resolution

import io.smooth.constraint.Constraint
import io.smooth.store.Store
import io.smooth.store.memory.PerpetualInMemoryStore
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Provider
import kotlin.reflect.KClass

internal class ConstraintResolutionService {

    private val resolutionsStore =
        PerpetualInMemoryStore<KClass<Constraint>, MutableList<Provider<ConstraintResolution<*>>>>()

    internal suspend fun addConstraintResolution(
        constraintClass: KClass<Constraint>,
        resolutionProvider: Provider<ConstraintResolution<*>>
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

    internal suspend fun getConstraintResolutions(constraintClass: KClass<Constraint>): List<Provider<ConstraintResolution<*>>>? =
        resolutionsStore.getById(constraintClass).firstOrNull()


}