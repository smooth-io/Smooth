package io.ncorti.kotlin.template.app.root

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.ncorti.kotlin.template.app.R
import io.smooth.arch.compat.CompatViewFragmentInteractor
import io.smooth.arch.view.ViewPresenter
import io.smooth.arch.dagger.RibScope
import javax.inject.Inject

@RibScope
class RootFragmentInteractor @Inject constructor(
) : CompatViewFragmentInteractor<RootScreen, RootFragmentInteractor.Presenter>() {

    override fun containerId(): Int? = null

    override fun createDefaultData(): RootScreen =
        RootScreen("def")

    override var delegateBackToChildren: Boolean = true

    override fun getView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.fragment_root, container, false)
    }

    override fun onViewReady(
        view: View,
        data: RootScreen,
        savedInstanceState: Bundle?
    ) {
        super.onViewReady(view, data, savedInstanceState)
        Toast.makeText(context, data.msg, Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }


    interface Presenter : ViewPresenter {
    }


}