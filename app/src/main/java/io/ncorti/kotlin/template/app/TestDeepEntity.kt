package io.ncorti.kotlin.template.app

import io.smooth.constraint.provider.ConstraintInitializer
import io.smooth.deep_link.handler.DeepLinkEntity

class TestDeepEntity : DeepLinkEntity{

    //Provide constraints here if any.
    override fun getConstraintsInitializer(): List<ConstraintInitializer> = arrayListOf()

    //Return true if deeplink is handled using this entity otherwise return false
    override fun handleDeepLink(deepLink: String): Boolean {
        when(deepLink){
            "/login" ->{
                router.goToLogin()
                return true
            }
            "/signup" ->{
                router.goToSignup()
                return true
            }
            else -> return false
        }
    }

}