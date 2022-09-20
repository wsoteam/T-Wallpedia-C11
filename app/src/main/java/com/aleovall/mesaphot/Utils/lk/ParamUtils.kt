package com.aleovall.mesaphot.Utils.lk

import com.aleovall.mesaphot.App
import com.aleovall.mesaphot.BuildConfig
import com.aleovall.mesaphot.R

class ParamUtils {
    private var mPrefs = PreferenceProvider()

    fun replaceParamHome(a: String, adid: String?, afid: String?): String {
        var a = a
        if (a.contains("gadid")) {
            a = a.replace("gadid", adid!!)
        }
        if (a.contains("afid")) {
            a = a.replace("afid", afid!!)
        }
        return a
    }

    fun replaceParamOut(b: String, sub_id1: String?,
                        sub_id2: String?,sub_id3: String?,sub_id4: String?): String {
        var b = b
        var subId1 = sub_id1
        var subId2 = sub_id2
        var subId3 = sub_id3
        var subId4 = sub_id4
        var subId5 = sub_id4
        var subId6 = sub_id4
        var subId7 = sub_id4
        var subId8 = sub_id4

        var subId9 = sub_id4
        var subId10 = sub_id4
        var subId11 = sub_id4
        var subId12 = sub_id4

        //cmp
        if (b.contains("wp1")) {
            subId1 = mPrefs.getDataString(LideraSharedKeys.P1.key)
            b = b.replace("wp1", subId1)
        }
        //ad
        if (b.contains("wp2")) {
            subId2 = mPrefs.getDataString(LideraSharedKeys.P2.key)
            b = b.replace("wp2", subId2)
        }
        //adset
        if (b.contains("wp3")) {
            subId3 = mPrefs.getDataString(LideraSharedKeys.P3.key)
            b = b.replace("wp3", subId3)
        }
        //channel
        if (b.contains("wp4")) {
            subId4 = mPrefs.getDataString(LideraSharedKeys.P4.key)
            b = b.replace("wp4", subId4)
        }
        //status
        if (b.contains("wp5")) {
            subId5 = mPrefs.getDataString(LideraSharedKeys.P5.key)
            b = b.replace("wp5", subId5)
        }
        //source
        if (b.contains("wp6")) {
            subId6 = mPrefs.getDataString(LideraSharedKeys.P6.key)
            b = b.replace("wp6", subId6)
        }

        //afkey
        if (b.contains("wp7")) {
            subId7 = App.getInstance().getString(R.string.appsfl)
            b = b.replace("wp7", subId7)
        }

        //ap id
        if (b.contains("wp8")) {
            subId8 = BuildConfig.APPLICATION_ID
            b = b.replace("wp8", subId8)
        }

        //af_ad_id
        if (b.contains("wp9")) {
            subId9 = mPrefs.getDataString(LideraSharedKeys.P7.key)
            b = b.replace("wp9", subId9)
        }

        /*//s1
        if (b.contains("wpks1")) {
            subId9 = mPrefs.getDataString(LideraSharedKeys.P7.key)
            b = b.replace("wpks1", subId9)
        }

        //s2
        if (b.contains("wpks2")) {
            subId10 = mPrefs.getDataString(LideraSharedKeys.P8.key)
            b = b.replace("wpks2", subId10)
        }

        //s3
        if (b.contains("wpks3")) {
            subId11 = mPrefs.getDataString(LideraSharedKeys.P9.key)
            b = b.replace("wpks3", subId11)
        }

        //s4
        if (b.contains("wpks4")) {
            subId12 = mPrefs.getDataString(LideraSharedKeys.P10.key)
            b = b.replace("wpks4", subId12)
        }*/


        return b
    }
}