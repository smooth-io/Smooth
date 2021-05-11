package io.smooth.use_cases.android

import androidx.work.*
import io.smooth.constraint.Constraint
import io.smooth.constraint.work.WorkConstraintsUtils
import io.smooth.store.Store
import io.smooth.store.converter.DataConverterStore
import io.smooth.store.memory.PerpetualInMemoryStore
import io.smooth.use_cases.ConstraintsHandler
import io.smooth.use_cases.UseCaseResult
import io.smooth.use_cases.executor.UseCaseExecutor
import kotlinx.coroutines.flow.FlowCollector
import io.smooth.use_cases.UseCase
import io.smooth.use_cases.android.work.UseCaseWorker
import kotlinx.coroutines.flow.firstOrNull

class WorkManagerUseCaseExecutor(
    private val workManager: WorkManager,
    private val hasRequestsStore: HasRequestsStore
) : UseCaseExecutor<WorkManagerUseCaseExecutor.RequestModifier>, HasRequestsStore {

    private val requestFlowCollectorsStores: Store<String, FlowCollector<UseCaseResult<*, *>>> =
        PerpetualInMemoryStore()

    override fun getRequestsStore(): DataConverterStore<String, Any, String> =
        hasRequestsStore.getRequestsStore()

    suspend fun getRequest(requestId: String): Any? =
        getRequestsStore().getById(requestId).firstOrNull()

    internal suspend fun getRequestFlowCollector(requestId: String): FlowCollector<UseCaseResult<*, *>>? =
        requestFlowCollectorsStores.getById(requestId).firstOrNull()

    data class RequestModifier(
        var workManagerConstraints: Constraints? = null
    )

    override suspend fun <Request, Res, UC : UseCase<Request, Res>> execute(
        flow: FlowCollector<UseCaseResult<Request, Res>>,
        constraints: List<Constraint>?,
        useCase: UC,
        request: Request,
        requestModifier: RequestModifier?
    ) {
        requestModifier?.apply {
            val workRequestBuilder = OneTimeWorkRequestBuilder<UseCaseWorker>()
                .addTag(useCase::class.toString())

            workRequestBuilder.setInputData(
                Data.Builder().apply {
                    putString(
                        UseCaseWorker.Companion.INPUT_DATA.USE_CASE_CLASS_NAME_INPUT_DATA,
                        useCase::class.toString()
                    )
                }.build()
            )

            workManagerConstraints?.apply {
                workRequestBuilder.setConstraints(this)
            }

            val workRequest = workRequestBuilder.build()

            val requestId = workRequest.id.toString()
            requestFlowCollectorsStores.save(
                Store.SaveDto(
                    requestId, requestFlowCollectorsStores as FlowCollector<UseCaseResult<*, *>>
                )
            )
            getRequestsStore().save(
                Store.SaveDto(requestId, request as Any)
            )

            workManager.enqueue(
                workRequest
            )
        }

    }

    override fun handleConstraints(
        constraints: List<Constraint>,
        requestModifier: RequestModifier?
    ): ConstraintsHandler {
        val constraintsMappingResult = WorkConstraintsUtils.mapConstraints(constraints)

        requestModifier?.workManagerConstraints = constraintsMappingResult.workManagerConstraints

        return ConstraintsHandler(
            constraintsMappingResult.handledConstraints,
            constraintsMappingResult.nonHandledConstraints
        )
    }

    override fun newInstanceOfRequestModifier(): RequestModifier =
        RequestModifier()

    companion object {

        private var instance: WorkManagerUseCaseExecutor? = null

        fun init(workManager: WorkManager, hasRequestsStore: HasRequestsStore) {
            instance = WorkManagerUseCaseExecutor(workManager, hasRequestsStore)
        }

        fun getInstance(): WorkManagerUseCaseExecutor =
            instance!!

    }

}