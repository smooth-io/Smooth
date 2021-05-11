package io.ncorti.kotlin.template.app

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.HiltAndroidApp
import io.ncorti.kotlin.template.app.use.*
import io.smooth.config.ConfigProvider
import io.smooth.config.HasConfig
import io.smooth.constraint.android.initializer.ConstraintsConfig
import io.smooth.constraint.manager.ConstraintsManager
import io.smooth.constraint.provider.ConstraintsProvider
import io.smooth.constraint.provider.di.InjectableConstraintsProvider
import io.smooth.deep_link.android.initializer.DeepLinkManagerConfiguration
import io.smooth.deep_link.android.sharedPreferenceDeepLinkStoreProvider
import io.smooth.deep_link.controller.DeepLinkMiddleware
import io.smooth.deep_link.controller.store.DeepLinkStoreProvider
import io.smooth.store.converter.DataConverter
import io.smooth.store.gson.GsonDataConverter
import io.smooth.use_cases.android.UseCasesConfig
import io.smooth.use_cases.android.WorkRequestStore
import io.smooth.use_cases.android.bg.room.BgRoomManagerConfig
import io.smooth.use_cases.android.provider.*
import io.smooth.use_cases.android.work.def.WorkDefaultManagerConfig
import io.smooth.use_cases.android.work.room.WorkRoomManagerConfig
import io.smooth.use_cases.android.work.store.json.JsonConstraintsInitializer
import io.smooth.use_cases.android.work.store.json.JsonPluginsInitializer
import io.smooth.use_cases.plugin.provider.PluginsProvider
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(), ConstraintsConfig,
    DeepLinkManagerConfiguration,
    UseCasesConfig {


    override fun middlewares(): List<DeepLinkMiddleware> = arrayListOf()

    override fun deepLinkStoreProvider(): DeepLinkStoreProvider =
        sharedPreferenceDeepLinkStoreProvider(this)

    override fun deleteDeepLinkOnProviderDie(): Boolean = false

    @Inject
    lateinit var constraintsProvider: ConstraintsProvider

    override fun provideConstraintsProvider(): ConstraintsProvider = constraintsProvider

    override fun getUseCaseProvider(): UseCasesProvider =
        MyUseCasesProvider()

    override fun getPluginsProvider(): PluginsProvider =
        MyPluginsProvider(this)

//
//    DeepLinkManagerConfiguration,
//    BgRoomManagerConfig,
//    WorkDefaultManagerConfig,
//    WorkRoomManagerConfig,
//
//
//    Configuration.Provider {


    override fun onCreate() {
        super.onCreate()
//        PluginsProvider.init(getPluginsProvider())
    }


//
//    override fun workRequestStore(): WorkRequestStore {
//        return super<BgRoomManagerConfig>.workRequestStore()
//    }
//
//    override fun jsonDataConverter(): DataConverter<Any, String> =
//        GsonDataConverter(Gson())
//
//    override fun constraintsInitializersJsonDataConverter(): DataConverter<List<JsonConstraintsInitializer<*>>, String> =
//        object : DataConverter<List<JsonConstraintsInitializer<*>>, String> {
//            val gson = Gson()
//
//            override fun convert(first: List<JsonConstraintsInitializer<*>>): String =
//                gson.toJson(first)
//
//            override fun convertReversed(second: String): List<JsonConstraintsInitializer<*>> {
//                val typeToken = object : TypeToken<List<JsonConstraintsInitializer<*>>>() {}.type
//                return gson.fromJson(second, typeToken) as List<JsonConstraintsInitializer<*>>
//            }
//        }
//
//    override fun pluginsInitializersJsonDataConverter(): DataConverter<List<JsonPluginsInitializer<*>>, String> =
//        object :
//            DataConverter<List<JsonPluginsInitializer<*>>, String> {
//            val gson = Gson()
//
//            override fun convert(first: List<JsonPluginsInitializer<*>>): String =
//                gson.toJson(first)
//
//            override fun convertReversed(second: String): List<JsonPluginsInitializer<*>> {
//                val typeToken = object : TypeToken<List<JsonPluginsInitializer<*>>>() {}.type
//                return gson.fromJson(second, typeToken) as List<JsonPluginsInitializer<*>>
//            }
//        }
//
//
//    override fun getUseCaseProvider(): UseCasesProvider =
//        MyUseCasesProvider()
//
//    override fun getPluginsProvider(): PluginsProvider =
//        MyPluginsProvider(this,
//            object : ConfigProvider {
//                override fun <Config : Any> applyConfig(
//                    hasConfig: HasConfig<Config>,
//                    config: Config?
//                ): HasConfig<Config> {
//                    if (config != null) {
//                        if (config is LinkedTreeMap<*, *>) {
//                            val gson = Gson()
//                            val json = gson.toJson(config)
//                            hasConfig.setConfig(
//                                gson.fromJson<Config>(json, hasConfig.configClass())
//                            )
//                        } else {
//                            hasConfig.setConfig(config)
//                        }
//                    }
//                    return hasConfig
//                }
//
//                override fun <Config : Any> canApplyConfig(
//                    hasConfig: HasConfig<Config>,
//                    config: Config?
//                ): Boolean = true
//            })
//
//    override fun getWorkManagerConfiguration() =
//        Configuration.Builder()
//            .setMinimumLoggingLevel(android.util.Log.DEBUG)
//            .build()


}