package io.smooth.use_cases.android.starter

import android.content.Context
import androidx.startup.Initializer
import io.smooth.use_cases.UseCasesService

class UseCasesInitializer : Initializer<UseCasesService> {

    override fun create(context: Context): UseCasesService {
        if (context is UseCasesConfig) {
            UseCasesService.init(context.useCasesProvider())
            val service = UseCasesService.getInstance()

            context.executors().forEach {
                service.addExecutor(it)
            }
        } else {
            throw IllegalArgumentException("Application class must extend ${UseCasesConfig::class}")
        }

        return UseCasesService.getInstance()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = arrayListOf()

}