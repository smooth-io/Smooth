package io.smooth.deep_link.provider.auto

import io.smooth.deep_link.target.DeepLinkTarget
import io.smooth.deep_link.provider.DeepLinkProvider

abstract class AutoDeepLinkProvider<Data, T : DeepLinkTarget> : DeepLinkProvider<Data, T> {

    private val providerTargetsHelper = ProviderTargetsHelper<Data, T>()

    init {
        providerTargetsHelper.buildTargets()
    }

    protected abstract fun ProviderTargetsHelper<Data, T>.buildTargets()

    override suspend fun getTarget(data: Data?): T? =
        providerTargetsHelper.get(data)

}