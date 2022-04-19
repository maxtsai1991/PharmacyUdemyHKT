package com.example.pharmacyudemyhkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pharmacyudemyhkt.data.Feature

class PharmacyDetailActivity : AppCompatActivity() {
    val TAG = PharmacyDetailActivity::class.java.simpleName

    /**
     * 萬惡錯誤 NullPointerException (參考 : https://tw-hkt.blogspot.com/2021/01/recyclerview.html)
     *      假設資料，沒有正確傳遞過來，將會引起 Caused by: java.lang.NullPointerException 造成 APP 不可預期閃退，所以需要再修改一下，確保資料為空時，仍可以正常顯示。
     */

    /**
     * 獲取data
     * 其中 「as Feature」，是指強行將資料轉換成 Feature。「as? Feature」，as 後面多一個問號，是確保資料不為空才進行轉換。
     */
    private val data by lazy {
        intent.getSerializableExtra("data") as? Feature
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy_detail)

        //印出資料
        Log.d(TAG,"PharmacyDetailActivity_info: "+ data?.property?.name);
    }

}