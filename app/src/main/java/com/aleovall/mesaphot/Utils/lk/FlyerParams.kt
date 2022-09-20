package com.aleovall.mesaphot.Utils.lk

import com.aleovall.mesaphot.App
import com.aleovall.mesaphot.Utils.Analytics

class FlyerParams {
    private val variables = Variables()
    private val mPrefs = PreferenceProvider()

    fun paramReceiver() {
        Analytics.event("paramReceiver")
        if (App.campaign != "null") {
            Analytics.event("campNotNull", "camp", App.campaign)

            /*try {
                if (App.campaign.contains("_")){
                    var array = App.campaign.split("_")

                    mPrefs.saveDataString(LideraSharedKeys.P7.key, array[0])
                    mPrefs.saveDataString(LideraSharedKeys.P8.key, array[1])
                    mPrefs.saveDataString(LideraSharedKeys.P9.key, array[2])
                    mPrefs.saveDataString(LideraSharedKeys.P10.key, array[3])

                }else{
                    Analytics.event("ntCnt_")
                }
            }catch (ex : Exception){
                Analytics.event("crash_")
            }*/


            mPrefs.saveDataString(LideraSharedKeys.P1.key, App.campaign)
            mPrefs.saveDataString(LideraSharedKeys.P2.key, App.ad)
            mPrefs.saveDataString(LideraSharedKeys.P3.key, App.adset)
            mPrefs.saveDataString(LideraSharedKeys.P4.key, App.channel)
            mPrefs.saveDataString(LideraSharedKeys.P5.key, App.status)
            mPrefs.saveDataString(LideraSharedKeys.P6.key, App.source)
            mPrefs.saveDataString(LideraSharedKeys.P7.key, App.af_ad_id)

            mPrefs.saveDataString("type", "naming")
        } else if (App.deep != null) {
            Analytics.event("deepNotNull", "dp", App.deep!!)
            //app://wsoapps.com/sub1/sub2/sub3/sub4/sub5
            //           2       3    4    5    6    7
            try {
                mPrefs.saveDataString(LideraSharedKeys.P1.key, App.campaign)

                mPrefs.saveDataString("type", "deep")
            } catch (ex: Exception) {

            }
        }
    }

    fun getDocName(): String {
        if (App.campaign != "null" && App.campaign != "None" || App.deep != null) {
            variables.DOCUMENT = variables.NONORGANIC
        } else {
            Variables.CC += "org"
            variables.DOCUMENT = variables.ORGANIC
        }
        return variables.DOCUMENT
    }
}