package io.ncorti.kotlin.template.app.root

import io.smooth.arch.routing.Screen
import kotlinx.android.parcel.Parcelize

@Parcelize
class RootScreen(
    val msg: String
) : Screen()