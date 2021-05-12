package io.smooth.network

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.net.ConnectivityManagerCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch


class SmoothNetworkService(private val context: Context) {

    private val connectivityStateFlow: MutableSharedFlow<NetworkDetails> = MutableSharedFlow(0)


    fun listen(): Flow<NetworkDetails> = connectivityStateFlow

    init {
        connectivityStateFlow.onSubscription {
            val subsCount = connectivityStateFlow.subscriptionCount.value
            if (subsCount > 0) startListening()
            else clear()
        }
    }

    private var connectivityManager: ConnectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private var networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            GlobalScope.launch {
                notifyNetworkStatus()
            }
        }
    }

    private suspend fun startListening() {
        notifyNetworkStatus()
        when {
            // for devices above Nougat
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> connectivityManager.registerDefaultNetworkCallback(
                getConnectivityManagerCallback()
            )

            // for devices b/w Lollipop and Nougat
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> lollipopNetworkAvailableRequest()

            //below lollipop
            else -> {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    context.registerReceiver(
                        networkReceiver,
                        IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
                    )
                }
            }
        }

    }


    private fun clear() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } else {
            context.unregisterReceiver(networkReceiver)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private suspend fun lollipopNetworkAvailableRequest() {
        val builder = NetworkRequest.Builder()
            .addTransportType(android.net.NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(android.net.NetworkCapabilities.TRANSPORT_WIFI)
        connectivityManager.registerNetworkCallback(
            builder.build(),
            getConnectivityManagerCallback()
        )
    }

    private suspend fun getConnectivityManagerCallback(): ConnectivityManager.NetworkCallback {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network?) {
                    GlobalScope.launch {
                        notifyNetworkStatus()
                    }
                }

                override fun onLost(network: Network?) {
                    GlobalScope.launch {
                        notifyNetworkStatus()
                    }
                }
            }
            return networkCallback
        } else {
            throw IllegalAccessError("Should not have happened")
        }
    }

    private suspend fun notifyNetworkStatus() {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo

        notifyListeners(
            getNetworkDetails(connectivityManager, activeNetwork)
        )
    }

    private suspend fun notifyListeners(networkDetails: NetworkDetails) {
        connectivityStateFlow.emit(networkDetails)
    }

    fun getNetworkDetails(
        connectivityManager: ConnectivityManager,
        networkInfo: NetworkInfo?
    ): NetworkDetails {
        val isConnected = isConnected(networkInfo)
        val isMetered = isMetered(connectivityManager)
        val isRoaming = if (networkInfo == null) false else isRoaming(networkInfo)
        return NetworkDetails(
            if (isConnected) NetworkType.CONNECTED else NetworkType.NOT_CONNECTED,
            if (isMetered) NetworkMeteringType.METERED else NetworkMeteringType.NOT_METERED,
            if (isRoaming) NetworkRoamingType.ROAMING else NetworkRoamingType.NOT_ROAMING
        )
    }

    fun isConnected(networkInfo: NetworkInfo?): Boolean =
        networkInfo != null && networkInfo.isConnected

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    fun isMetered(connectivityManager: ConnectivityManager): Boolean =
        ConnectivityManagerCompat.isActiveNetworkMetered(connectivityManager)

    fun isRoaming(networkInfo: NetworkInfo): Boolean = networkInfo.isRoaming

    fun getNetworkDetails(): NetworkDetails =
        getNetworkConnectivityManager().let {
            getNetworkDetails(
                it,
                it.activeNetworkInfo
            )
        }

    fun isConnected(): Boolean =
        getNetworkConnectivityManager().let {
            isConnected(it.activeNetworkInfo)
        }

    fun isMetered(): Boolean =
        isMetered(getNetworkConnectivityManager())

    fun isRoaming(): Boolean =
        getNetworkConnectivityManager().activeNetworkInfo.let {
            if (it == null) return false
            isRoaming(it)
        }

    private fun getNetworkConnectivityManager(): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    companion object {
        private var smoothNetworkService: SmoothNetworkService? = null

        fun init(context: Context): SmoothNetworkService {
            smoothNetworkService =
                SmoothNetworkService(
                    context
                )
            return smoothNetworkService!!
        }

        fun getInstance(): SmoothNetworkService {
            if (smoothNetworkService == null) {
                throw IllegalArgumentException("Must call init for Connectivity manager")
            }
            return smoothNetworkService!!
        }
    }

}