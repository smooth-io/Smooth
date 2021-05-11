package io.ncorti.kotlin.template.app

import android.net.Uri
import android.util.Log
import io.smooth.deep_link.android.controller.AndroidDeepLinkMiddleware

class MyMiddleware: AndroidDeepLinkMiddleware {

    override fun handleDeepLink(deepLink: Uri) {
        Log.i("naav","Deeplink w22 : ${deepLink}")
    }

}