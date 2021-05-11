package io.smooth.use_cases.android.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.smooth.use_cases.*
import io.smooth.use_cases.android.WorkManagerUseCaseExecutor
import io.smooth.use_cases.android.work.UseCaseWorker.Companion.INPUT_DATA.USE_CASE_CLASS_NAME_INPUT_DATA
import io.smooth.use_cases.executor.DefaultUseCaseExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass

class UseCaseWorker(
    context: Context,
    private val params: WorkerParameters
) : CoroutineWorker(context, params) {

    val useCaseService = UseCaseService.getInstance()
    val workManagerExecutor = WorkManagerUseCaseExecutor.getInstance()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val result = useCaseService.execute(
            DefaultUseCaseExecutor::class,
            getUseCaseClass(),
            getRequest() ?: return@withContext Result.failure(),
            null,
            null
        ).firstOrNull()

        val flowCollector = workManagerExecutor.getRequestFlowCollector(params.id.toString())
        flowCollector?.emit(result as UseCaseResult<*, *>)

        return@withContext when (result) {
            is Failed -> Result.retry()
            is Success -> Result.success()
            null -> Result.retry()
            else -> Result.failure()
        }
    }

    private suspend fun getRequest(): Any? {
        val requestId = params.id.toString()
        return workManagerExecutor.getRequest(requestId)
    }


    private fun getUseCaseClass(): KClass<UseCase<Any, Any>> {
        val useCaseClassName = params.inputData.getString(USE_CASE_CLASS_NAME_INPUT_DATA)!!
        return Class.forName(useCaseClassName).kotlin as KClass<UseCase<Any, Any>>
    }

    companion object {

        object INPUT_DATA {
            val USE_CASE_CLASS_NAME_INPUT_DATA = "USE_CASE_CLASS_NAME"
        }

    }


}
