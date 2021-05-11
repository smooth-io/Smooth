package io.ncorti.kotlin.template.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import io.ncorti.kotlin.template.app.data.User
import io.ncorti.kotlin.template.app.data.UserManager
import io.ncorti.kotlin.template.app.data.UsersRepo
import io.ncorti.kotlin.template.app.use.MyUseCase
import io.realm.Realm
import io.realm.RealmConfiguration
import io.smooth.arch.builder.Builder
import io.smooth.arch.compat.CompatActivityInteractor
import io.smooth.constraint.Constraint
import io.smooth.constraint.ConstraintStatus
import io.smooth.constraint.ConstraintStatus.*
import io.smooth.constraint.manager.ConstraintsManager
import io.smooth.constraint.provider.ConstraintInitializer
import io.smooth.data.fetch.SuccessResult
import io.smooth.deep_link.android.provider.IntentDeepLinkSource
import io.smooth.deep_link.handler.DeepLinkEntity
import io.smooth.deep_link.handler.DeepLinkHandler
import io.smooth.deep_link.provider.DeepLinkProvider
import io.smooth.deep_link.provider.DeepLinkSource
import io.smooth.use_cases.android.UseCasesManager
import io.smooth.use_cases.callback.CallbackWork
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class MainActivity : CompatActivityInteractor<MainScreen>(),
    DeepLinkHandler, DeepLinkProvider {


    private var deepLinkHandlerCallback: DeepLinkHandler.DeepLinkHandlerCallback? = null
    override fun setCallback(deepLinkHandlerCallback: DeepLinkHandler.DeepLinkHandlerCallback?) {
        this.deepLinkHandlerCallback = deepLinkHandlerCallback
    }

    override fun getDeepLinkEntities(): List<DeepLinkEntity> =
        arrayListOf(TestDeepEntity())


    private var deepLinkProviderCallback: DeepLinkProvider.DeepLinkProviderCallback? = null
    override fun setCallback(deepLinkProviderCallback: DeepLinkProvider.DeepLinkProviderCallback?) {
        this.deepLinkProviderCallback = deepLinkProviderCallback
    }

    override fun getSources(): List<DeepLinkSource> =
        arrayListOf(IntentDeepLinkSource())


    override fun createDefaultData(): MainScreen = MainScreen()

    override fun getBuilder(): Builder<*> =
        MainBuilder(
            object : MainBuilder.Dependencies {}
        )

    override fun containerId(): Int = R.id.container

    val TAG = "MainActivity123"

    @ExperimentalCoroutinesApi
    override fun dispatchAttach(savedInstanceState: Bundle?) {
        super.dispatchAttach(savedInstanceState)
        setContentView(R.layout.activity_main)

        UseCasesManager.getInstance()
            .execute(
                "hi",
                MyUseCase::class,
                null,
                null, object : CallbackWork.WorkCallback<String> {
                    override fun onExecuting() {
                    }

                    override fun onProgress(progressDetails: Map<String, Any>) {
                    }

                    override fun onResult(result: String) {
                    }

                    override fun onFailed(error: Throwable) {
                    }

                    override fun onConstraintsNotMet(blockingConstraints: List<Constraint>) {
                    }

                    override fun onCancelled(cancelReason: Throwable?) {
                    }

                    override fun onCompleted() {
                    }
                })




        Realm.init(this)
        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .name("test")
                .allowWritesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build()
        )

        val usersRepo = UsersRepo(
            UserManager()
        )

        val user = User(1, "hi")

        lifecycleScope.launch(Dispatchers.Main) {

            usersRepo.saveUser2(User(1, "I am the one"))
                .collect {
                    Log.i(TAG, "s: " + it.source.name)
                    Log.i(TAG, "s: " + it.toString())
                }

            usersRepo.saveUser(user)
                .collect {
                    Log.i(TAG, "save: " + it.source.name)
                    Log.i(TAG, "save: " + it.toString())
                }

            usersRepo.getUser(1)
                .collect {
                    when (it) {
                        is SuccessResult<*, *> -> {
                            Log.i(TAG, "save: " + (it.data as User).name)
                        }
                    }
                    Log.i(TAG, "get: " + it.source.name)
                    Log.i(TAG, "get: " + it.toString())
                }


        }

        val myConstraint = ConstraintsManager.getInstance()
            .getConstraint<AlwaysTrueConstraint>(
                ConstraintInitializer(AlwaysTrueConstraint::class)
            )

        myConstraint.apply {
            this.registerCallback(object : Constraint.ConstraintCallback {
                override fun onConstraintStatusChanged(status: ConstraintStatus) {
                    when (status) {
                        PENDING -> {
                            //Constraint not executed yet
                        }
                        CONSTRAINT_MET -> {
                            //Constraints met, you can proceed
                        }
                        CONSTRAINT_NOT_MET -> {
                            //Constraint not met, call checkConstraint() again or handle not met constraitns
                        }
                    }
                }
            })
            checkConstraint()

            clear()
        }

//
//
//        val communicationManager = BgCommunicationManager.getInstance()
//
//        BgJobManager.getInstance().getJob(
//            "hi", MyUseCase::class,
//            arrayListOf(
//                PluginInitializer(MyForegroundService::class)
//            ),
//            arrayListOf(
////                ConfigConstraintInitializer(
////                    AlwaysTrueConstraint::class,
////                    AlwaysTrueConstraint.Config(5000)
////                )
//            ),
//            BgJobInfo(
//                true,
//                MyForegroundService::class.java.name
//            )
//        ).apply {
//            lifecycleScope.launch(Dispatchers.IO) {
//                communicationManager.listenTo<String>(jobId())
//                    .collect {
//                        Log.i(TAG, "Status: ${it}")
//                    }
//            }
//            execute()
//        }
//
//
//        SmoothNetworkManager.getInstance()
//            .registerListener(object : NetworkListener {
//                override fun onNetworkChanged(networkDetails: NetworkDetails) {
//                    Log.i(TAG, networkDetails.toString())
//                }
//            })
//
//        lifecycleScope.launch(Dispatchers.IO) {
//            SmoothNetworkManager.getInstance()
//                .listenForNetworkChanges().collect {
//                    Log.i(TAG, "ktx: $it")
//                }
//        }
//
//        ConstraintsManager.getInstance()
//            .getConstraint<AlwaysTrueConstraint>(ConstraintInitializer(AlwaysTrueConstraint::class))
//            .apply {
//                registerCallback(object : Constraint.ConstraintCallback {
//                    override fun onConstraintStatusChanged(status: ConstraintStatus) {
//
//                    }
//                })
//
//                checkConstraint()
//            }
//
//
//
//        SmoothBatteryManager.getInstance()
//            .registerListener(
//                object : BatteryListener {
//                    override fun onChargingStateChanged(batteryChargingState: BatteryChargingState) {
//                        Log.i(TAG, "Battery c : $batteryChargingState")
//                    }
//
//                    override fun onLevelChanged(levelState: BatteryLevelState) {
//                        Log.i(TAG, "Battery l : $levelState")
//                    }
//                }
//            )
//
//        SmoothBatteryManager.getInstance().apply {
//            Log.i(TAG, "Battery s : ${getChargingState()}")
//            Log.i(TAG, "Battery p : ${getBatteryPercentage()}")
//            Log.i(TAG, "Battery level : ${getLevelState()}")
//        }
//
//        Log.i("naav", "intent data: ${intent.data}")
//        router?.handleDeepLink()
//        router?.checkDeepLink(intent)
//

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i("naav", "intent data: ${intent?.data}")
//        router?.checkDeepLink(intent)
//        router?.handleDeepLink()
//        router?.attachChild(RootScreen("haha"), true, false)
    }

    override fun willResignActive() {

    }


}
