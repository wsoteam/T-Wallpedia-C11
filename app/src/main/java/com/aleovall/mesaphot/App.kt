package com.aleovall.mesaphot

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import com.amplitude.api.Amplitude
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.aleovall.mesaphot.Utils.Analytics
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.firebase.FirebaseApp
import java.io.IOException

class App: Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()

        sInstance = this

        FirebaseApp.initializeApp(this)

        val conversionDataListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                data!!.forEach {
                     Log.e("LOL", "${it.key}----${it.value}")
                 }
                Analytics.event("naming_cmp", "name", data!!["campaign"].toString())
                Analytics.event("af_ad", "name", data!!["af_ad"].toString())
                Analytics.event("af_adset", "name", data!!["af_adset"].toString())
                Analytics.event("af_status", "name", data!!["af_status"].toString())
                Analytics.event("af_channel", "name", data!!["af_channel"].toString())
                Analytics.event("media_source", "name", data!!["media_source"].toString())
                Analytics.event("af_ad_id", "name", data!!["af_ad_id"].toString())

                campaign = data!!["campaign"].toString()
                adset = data!!["af_adset"].toString()
                status = data!!["af_status"].toString()
                channel = data!!["af_channel"].toString()
                ad = data!!["af_ad"].toString()
                source = data!!["media_source"].toString()
                af_ad_id = data!!["af_ad_id"].toString()

            }

            override fun onConversionDataFail(error: String?) {
                Log.e("LOG_TAG", "error onAttributionFailure :  $error")
            }

            override fun onAppOpenAttribution(data: MutableMap<String, String>?) {
                data?.map {
                    Log.d("LOG_TAG", "onAppOpen_attribute: ${it.key} = ${it.value}")
                }
            }

            override fun onAttributionFailure(error: String?) {
                Log.e("LOG_TAG", "error onAttributionFailure :  $error")
            }
        }
        AppsFlyerLib.getInstance().init(getString(R.string.appsfl), conversionDataListener, applicationContext)
        AppsFlyerLib.getInstance().start(this)

        Amplitude.getInstance().initialize(this, getString(R.string.amplitude_id))
                .enableForegroundTracking(this)
    }

    companion object {

        private lateinit var sInstance: App

        fun getInstance(): App {
            return sInstance
        }

        var campaign: String = "null"
        var gadid: String? = null
        var deep: String? = null
        var ad: String? = "null"
        var adset: String? = "null"
        var channel: String? = "null"
        var status: String? = "null"
        var source: String? = "null"
        var af_ad_id: String? = "null"
    }

    fun getAdvertisingId(context: Context) {
        AsyncTask.execute {
            try {
                val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
                gadid = adInfo?.id
            } catch (exception: IOException) {
                Log.i("TAG_EXCEPTION", exception.toString())
            } catch (exception: GooglePlayServicesRepairableException) {
                Log.i("TAG_EXCEPTION", exception.toString())
            } catch (exception: GooglePlayServicesNotAvailableException) {
                Log.i("TAG_EXCEPTION", exception.toString())
            }
        }
    }
}