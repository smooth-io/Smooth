package io.smooth.use_cases.android.starter

import androidx.work.WorkManager
import io.smooth.store.converter.DataConverterStore
import io.smooth.use_cases.android.WorkManagerUseCaseExecutor
import io.smooth.use_cases.executor.DefaultUseCaseExecutor
import io.smooth.use_cases.executor.UseCaseExecutor
import io.smooth.use_cases.provider.UseCasesProvider

interface UseCasesConfig {

    fun useCasesProvider(): UseCasesProvider

    fun executors(): List<UseCaseExecutor<*>> =
        arrayListOf(
            DefaultUseCaseExecutor(),
            WorkManagerUseCaseExecutor(workManager(), requestsStore())
        )

    fun workManager(): WorkManager

    fun requestsStore(): DataConverterStore<String, Any, String>


}