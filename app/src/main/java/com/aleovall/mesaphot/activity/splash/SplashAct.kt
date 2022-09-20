package com.aleovall.mesaphot.activity.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.webkit.CookieSyncManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.aleovall.mesaphot.App
import com.aleovall.mesaphot.R
import com.aleovall.mesaphot.Utils.Analytics
import com.aleovall.mesaphot.Utils.lk.*
import com.appsflyer.AppsFlyerLib
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.onesignal.OneSignal
import com.walls.walllivappo.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SplashAct : AppCompatActivity(R.layout.activity_splash) {

    private var questions = ""
    private var isStartNextScreen = false

    private var app = App()
    private var variables = Variables()
    private var mPrefs = PreferenceProvider()
    private var flyerParams = FlyerParams()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var paramUtils = ParamUtils()
    private val wv = WV()

    private lateinit var webView: WebView
    private val imagePick = 1
    private var mUploadMessage: ValueCallback<Array<Uri>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Analytics.open()

        startGettingDeep()

        app.getAdvertisingId(this)
        init()

        variables.OPEN = mPrefs.getDataInt(LideraSharedKeys.FirstOpenView.key)
        variables.lastUrl = mPrefs.getDataString(LideraSharedKeys.LastOpenUrl.key)

        splashLoader()

    }

    fun startWhiteScreen() {
        Analytics.openWhite()
        var intentStartChoiseAct = Intent(this@SplashAct, MainActivity::class.java)
        startActivity(intentStartChoiseAct)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val results = arrayOf(Uri.parse(data!!.dataString))
        mUploadMessage!!.onReceiveValue(results)
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onResume() {
        super.onResume()
        CookieSyncManager.getInstance().startSync()
    }

    override fun onPause() {
        super.onPause()
        mPrefs.saveDataInt(LideraSharedKeys.FirstOpenView.key, variables.OPEN)
        mPrefs.saveDataString(LideraSharedKeys.LastOpenUrl.key, webView.url)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            variables.outCode++
            if (variables.outCode == 2) {
                variables.outCode = 0
                variables.firstUrl = mPrefs.getDataString(LideraSharedKeys.FirstOpenUrl.key)
                webView.loadUrl(variables.firstUrl)
            }
        }
    }

    private fun init() {
        variables.appsFlyerId = AppsFlyerLib.getInstance().getAppsFlyerUID(applicationContext)
        CookieSyncManager.createInstance(applicationContext)
        val tm = applicationContext.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        Variables.CC = "static"
        Analytics.event("iso_cmplt", "key", tm.networkCountryIso)
        webView = WebView(this)
        webView.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        )
        wvSettings()
    }

    private fun getFirebaseData() {
        val docRef: DocumentReference =
                db.collection(LideraSharedKeys.COLLECTION.key).document(variables.DOCUMENT)
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    val map: MutableMap<String, Any>? = document.data
                    if (variables.lastUrl == LideraSharedKeys.AppCheckerWord.key) {
                        if (map!![Variables.CC] != null) {
                            variables.FIREBASE = map[Variables.CC].toString()
                        }
                        if (variables.FIREBASE.isNotEmpty()) {
                            variables.FIREBASE = paramUtils.replaceParamHome(
                                    variables.FIREBASE,
                                    App.gadid,
                                    variables.appsFlyerId
                            )
                            variables.FIREBASE = paramUtils.replaceParamOut(
                                    variables.FIREBASE,
                                    variables.sub_name_1,
                                    variables.sub_name_2,
                                    variables.sub_name_3,
                                    variables.sub_name_4
                            )

                            fl_splash.removeAllViews()
                            fl_splash.addView(webView)
                            Analytics.event("open", "pg", variables.FIREBASE)
                            Log.e("LOL", variables.FIREBASE)///////////
                            webView.loadUrl(variables.FIREBASE)

                            OneSignal.initWithContext(this)
                            OneSignal.setAppId("d1f4a8df-3401-4f87-a13c-9e2d53c8027d")
                        } else {
                            startWhiteScreen()
                        }
                    }
                } else {
                    startWhiteScreen()
                }
            } else {
                startWhiteScreen()
            }
        }
    }


    private fun startGettingDeep() {
        //Analytics.startGettingDeep()

        GlobalScope.launch {
            //Log.e("LOL", "launch")
            TimeUnit.SECONDS.sleep(6)

            FacebookSdk.setAutoInitEnabled(true)
            FacebookSdk.fullyInitialize()

            AppLinkData.fetchDeferredAppLinkData(
                    this@SplashAct
            ) { appLinkData: AppLinkData? ->
                if (appLinkData != null) {
                    //Log.e("LOL", "appLinkData")///////////////

                    Analytics.event("dp_rcvd", "dp", appLinkData.targetUri.toString())
                    App.deep = appLinkData.targetUri.toString()
                } else {
                    Analytics.event("dp_ntrcvd")
                    //Log.e("LOL", "else")
                    //Analytics.notReceivedDeep()
                }
            }
        }
    }


    private fun splashLoader() {
        val t: Thread = object : Thread() {
            @SuppressLint("SetTextI18n")
            override fun run() {
                try {
                    while (!variables.isRun) {
                        if (variables.runIterator >= 100 || App.campaign != "null" && App.campaign != "None" || App.deep != null) {
                            variables.isRun = true
                            startUIChange()
                            break
                        }

                        variables.runIterator++

                        sleep(120)

                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        t.start()
    }

    private fun startUIChange() {
        if (variables.lastUrl == LideraSharedKeys.AppCheckerWord.key) {
            getAppsflyerParametr()
        } else {
            secondUiChanger()
        }
    }


    private fun secondUiChanger() {
        runOnUiThread {
            fl_splash.removeAllViews()
            fl_splash.addView(webView)
            webView.loadUrl(variables.lastUrl)
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun wvSettings() {
        wv.setParams(webView)
        webView.webChromeClient = object : WebChromeClient() {

            override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
            ): Boolean {
                mUploadMessage = filePathCallback
                val pickIntent = Intent()
                pickIntent.type = "image/*"
                pickIntent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                        Intent.createChooser(pickIntent, "Select Picture"),
                        imagePick
                )
                return true
            }
        }
    }

    private fun getAppsflyerParametr() {
        flyerParams.paramReceiver()
        variables.DOCUMENT = flyerParams.getDocName()
        getFirebaseData()
    }
}