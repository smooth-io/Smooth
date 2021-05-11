package io.smooth.network

interface NetworkListener {
    fun onNetworkChanged(networkDetails: NetworkDetails)
}