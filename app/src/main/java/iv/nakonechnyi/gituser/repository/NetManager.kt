package iv.nakonechnyi.gituser.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.os.Build


class NetManager(private var context: Context) {

    val isNetworkAvailable: Boolean get() {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.let {
                    when {
                        it.hasTransport(TRANSPORT_CELLULAR) ||
                        it.hasTransport(TRANSPORT_WIFI) ||
                        it.hasTransport(TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                } ?: false
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                activeNetworkInfo != null && activeNetworkInfo.isConnected
            }
        } else false
    }
}