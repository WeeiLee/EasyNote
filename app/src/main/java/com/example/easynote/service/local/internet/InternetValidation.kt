package com.example.easynote.service.local.internet

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun internetStateFlow(context: Context): StateFlow<Boolean> {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onCapabilitiesChanged(
                network: Network,
                caps: NetworkCapabilities
            ) {
                trySend(
                    caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                )
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        cm.registerNetworkCallback(request, callback)

        // Estado inicial
        trySend(hasValidatedInternet(context))

        awaitClose {
            cm.unregisterNetworkCallback(callback)
        }
    }.stateIn(
        scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )
}

fun hasValidatedInternet(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = cm.activeNetwork ?: return false
    val caps = cm.getNetworkCapabilities(network) ?: return false
    return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}