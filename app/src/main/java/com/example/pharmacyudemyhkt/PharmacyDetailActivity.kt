package com.example.pharmacyudemyhkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pharmacyudemyhkt.data.Feature
import com.example.pharmacyudemyhkt.databinding.ActivityPharmacyDetailBinding

/**
 * 4-43 藥局詳細頁佈局與資料顯示
 *  參考網址 : https://tw-hkt.blogspot.com/2021/01/android-recyclerview_30.html
 */

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
    private val data by lazy { intent.getSerializableExtra("data") as? Feature }

    private val name by lazy { data?.property?.name } // ?的意思 : 假設data是空值,就不會繼續往下拿資料(不會繼續往property資料)   // 藥局名稱
    private val maskAdultAmount by lazy { data?.property?.mask_adult }                                                  // 成人口罩數量
    private val maskChildAmount by lazy { data?.property?.mask_child }                                                  // 小孩口罩數量
    private val phone by lazy { data?.property?.phone }                                                                 // 藥局電話
    private val address by lazy { data?.property?.address }                                                             // 藥局地址

    private lateinit var binding: ActivityPharmacyDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_pharmacy_detail)

        binding = ActivityPharmacyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }
    private fun initView() {
        binding.tvName.text = name ?: "資料發生錯誤"              // 假設這筆資料(藥局名稱)是空的時候,則顯示"資料發生錯誤"
        binding.tvAdultAmount.text = maskAdultAmount.toString()
        binding.tvChildAmount.text = maskChildAmount.toString()
        binding.tvPhone.text = phone
        binding.tvAddress.text = address
    }
}