package io.smooth.deep_link.provider

import io.smooth.deep_link.target.DeepLinkTarget

interface DeepLinkProvider<Data, T : DeepLinkTarget> {

    suspend fun canHandleDeepLink(data: Any?): Boolean

    suspend fun getTarget(data: Data?): T?

}