package io.ncorti.kotlin.template.app.use

import android.content.Context
import io.smooth.config.ConfigProvider
import io.smooth.use_cases.plugin.UseCasePlugin
import io.smooth.use_cases.plugin.provider.PluginsProvider
import kotlin.reflect.KClass

class MyPluginsProvider(
    private val context: Context
) : PluginsProvider() {

    override fun getPlugin(pluginClass: KClass<out UseCasePlugin<*>>): UseCasePlugin<*> =
        MyForegroundService(context) as UseCasePlugin<*>

}