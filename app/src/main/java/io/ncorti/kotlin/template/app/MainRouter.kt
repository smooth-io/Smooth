package io.ncorti.kotlin.template.app

import android.util.Log
import androidx.core.net.toUri
import io.ncorti.kotlin.template.app.root.RootScreen
import io.smooth.arch.interactor.FragmentInteractor
import io.smooth.arch.nav.CompatRouter
import io.smooth.arch.interactor.InteractorChildrenProvider
import io.smooth.deep_link.android.provider.IntentDeepLinkSource
import io.smooth.deep_link.provider.DeepLinkSource
import javax.inject.Inject


class MainRouter @Inject constructor(interactorChildrenProvider: InteractorChildrenProvider) :
    CompatRouter(interactorChildrenProvider) {

    override fun onReady() {
        super.onReady()

        configInteractor {
//
//            this.modifyInteractor {
//                it.interactor as FragmentInteractor<*, *>
//            }
//
//            this.modifyTransaction { routingDetails, fragmentTransaction ->
//                fragmentTransaction.setCustomAnimations(
//                    R.anim.enter,
//                    R.anim.exit,
//                    R.anim.pop_enter,
//                    R.anim.pop_exit
//                )
//                fragmentTransaction
//            }
//
//            config<RootScreen> {
//
                modifyInteractor {
                    Log.i("naav", "Modifiying interactor 2")
                    it.interactor as FragmentInteractor<*, *>
                }

//            }

        }

        deepLinks {

            add("test/22", RootScreen("dsd"))
//            add(":/:name/dd/") { list: List<String>, map: Map<String, String>, map1: Map<String, String?> ->
//                RootScreen(map["name"]!!)
//            }
//            add {
//                uri = ":/dd/ss".toUri()
//                screen = RootScreen("ss")
//            }

        }
    }

    override fun getSources(): List<DeepLinkSource> =
        arrayListOf(IntentDeepLinkSource())

}