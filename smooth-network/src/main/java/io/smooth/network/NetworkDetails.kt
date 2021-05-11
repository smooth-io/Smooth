package io.smooth.network


data class NetworkDetails(
    val networkType: NetworkType,
    val networkMeteringType: NetworkMeteringType,
    val networkRoamingType: NetworkRoamingType
)

enum class NetworkType {
    CONNECTED, NOT_CONNECTED
}

enum class NetworkMeteringType {
    METERED, NOT_METERED
}

enum class NetworkRoamingType {
    ROAMING, NOT_ROAMING
}

