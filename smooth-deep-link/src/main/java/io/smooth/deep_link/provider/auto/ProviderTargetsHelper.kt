package io.smooth.deep_link.provider.auto

import io.smooth.deep_link.target.DeepLinkTarget

class ProviderTargetsHelper<Data, T : DeepLinkTarget> {

    private val dataToTargetMapping: MutableMap<Data?, T> = mutableMapOf()

    fun onData(data: Data?, block: Data?.() -> T) {
        dataToTargetMapping[data] = block(data)
    }

    fun get(data: Data?): T? = dataToTargetMapping[data]

}