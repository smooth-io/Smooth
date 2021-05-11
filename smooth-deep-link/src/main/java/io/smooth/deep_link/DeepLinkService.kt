package io.smooth.deep_link

import io.smooth.constraint.*
import io.smooth.deep_link.provider.DeepLinkProvider
import io.smooth.deep_link.target.DeepLinkTarget
import io.smooth.store.Store
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlin.reflect.KClass

class DeepLinkService<T : DeepLinkTarget>(
    private val deepLinkTargetsStore: Store<Int, T>,
    private val deepLinkProviders: List<DeepLinkProvider<Any, T>>
) {

    suspend fun checkDeepLink(data: Any?, disabledProviders: List<KClass<DeepLinkProvider<*, T>>>) {
        deepLinkProviders.filter { provider ->
            val foundClass = disabledProviders.firstOrNull {
                it == provider::class
            }

            foundClass == null
        }.forEach {
            if (it.canHandleDeepLink(data)) {
                it.getTarget(data)?.also { target ->
                    onTargetAvailable(target)
                    return@forEach
                }
            }
        }
    }

    private suspend fun onTargetAvailable(target: T) {
        saveDeepLinkTarget(target)
    }

    @ExperimentalCoroutinesApi
    suspend fun listenToDeepLinks(): Flow<DeepLinkResult<T>> =
        deepLinkTargetsStore.getById(DEEP_LINK_ID).filterNotNull()
            .transformLatest {

                val deepLinkTargetFlow = flowOf(it)
                val constraintsFlow = ConstraintsService.getInstance()
                    .check(*it.requiredConstraints.toTypedArray())

                this.emitAll(deepLinkTargetFlow.combine(constraintsFlow) { deepLink, constraintsResult ->
                    DeepLinkResult(
                        deepLink,
                        constraintsResult
                    )
                }
                )
            }.onEach {
                if (it.constraintsResult is ConstraintsMet) {
                    deleteDeepLinkTarget(it.target)
                }
            }


    private suspend fun deleteDeepLinkTarget(target: T) {
        deepLinkTargetsStore.deleteById(DEEP_LINK_ID)
    }

    private suspend fun saveDeepLinkTarget(target: T) {
        deepLinkTargetsStore.save(Store.SaveDto(DEEP_LINK_ID, target))
    }

    companion object {
        private const val DEEP_LINK_ID = 1
    }

}