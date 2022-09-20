package com.aleovall.mesaphot.Utils

import com.amplitude.api.Amplitude
import com.amplitude.api.Identify
import org.json.JSONException
import org.json.JSONObject

object Analytics {

    fun open(){
        Amplitude.getInstance().logEvent("open")
    }

    fun openBlack(){
        Amplitude.getInstance().logEvent("open_black")
    }


    fun event(eventName : String){
        Amplitude.getInstance().logEvent(eventName)
    }

    fun event(eventName : String, propKey : String, propValue : String){
        val eventProperties = JSONObject()
        try {
            eventProperties.put(propKey, propValue)
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
        Amplitude.getInstance().logEvent(eventName, eventProperties)
    }

    fun openWhite(){
        Amplitude.getInstance().logEvent("open_white")
    }

    fun getDomain(){
        Amplitude.getInstance().logEvent("get_domain")
    }


    fun setUserDomain(domain : String){
        var identify = Identify().setOnce("domain", domain)
        Amplitude.getInstance().identify(identify)
    }

    fun setUserNaming(naming : String){
        var identify = Identify().setOnce("naming", naming)
        Amplitude.getInstance().identify(identify)
    }

    fun setUserUrl(url : String){
        var identify = Identify().setOnce("url", url)
        Amplitude.getInstance().identify(identify)
    }
}