package io.smooth.network.starter

import android.content.Context
import androidx.startup.Initializer
import io.smooth.network.SmoothNetworkService

class NetworkManagerInitializer : Initializer<SmoothNetworkService> {

    override fun create(context: Context): SmoothNetworkService =
        SmoothNetworkService.init(context)

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = arrayListOf()

}