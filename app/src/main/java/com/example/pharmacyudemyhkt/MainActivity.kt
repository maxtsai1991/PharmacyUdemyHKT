package com.example.pharmacyudemyhkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import okhttp3.*
import java.io.IOException

/**
 * 3-8 & 3-9
 * 1.   添加 OkHttp 依賴庫 , 在 GRADLE (Module) 層級的 dependencies 中內加入 EX : implementation 'com.squareup.okhttp3:okhttp:4.9.0'
 * 2.   權限宣告 (permissions) , 連線網路，需在 AndroidManifest.xml 中宣告 EX : <uses-permission android:name="android.permission.INTERNET" />
 */

/**
 * 3-10 基本使用套件介紹
 * (OkHttp 獲取網路資料方式參考網址 : https://tw-hkt.blogspot.com/2020/12/android-okhttp.html)
 * 補充說明 :
 * GET :  直接在瀏覽器,輸入網址,直接執行,瀏覽器就會跑出資料,這種請求方式大部分就是GET方式
 * POST : 帳號密碼登入,透過表單去傳遞資料,這種請求方式就是POST方式
 * Execute Synchronous(執行同步) :整個系統,會等你把整個資料抓回來,我們在去執行之後的動作
 * Enqueue Synchronous(排隊非同步) :送出了這個請求之後,剛剛所要抓資料這部分,幫你在背後做執行,而不會阻塞整個主線程,大部分都會使用非同步,因為Android會限制你,拉網路資料(很耗時的),不允許把整個畫面卡死
 */

/**
 * 3-11 實戰OKHttp中的Enqueue非同步處理GET請求方式
 * 口罩資料網址 : https://raw.githubusercontent.com/thishkt/pharmacies/master/data/info.json
 */

class MainActivity : AppCompatActivity() {
    companion object{
        val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPharmacyData()
    }

    private fun getPharmacyData() {

        //口罩資料網址 (資料來源)
        val pharmaciesDataUrl = "https://raw.githubusercontent.com/thishkt/pharmacies/master/data/info.json"

        //Part 1: 宣告(設定)OkHttpClient
        val okHttpClient : OkHttpClient = OkHttpClient().newBuilder().build()

        //Part 2: 宣告(設定)Request，要求要連到指定網址 ,透過GET方式
        val request : Request = Request.Builder().url(pharmaciesDataUrl).get().build()

        //Part 3: 宣告 Call ,要連線到哪個網址,用怎麼樣方式去做連線,把上面的request塞給newCall
        val call : Call = okHttpClient.newCall(request)

        //執行 Call 連線後，採用 enqueue 非同步方式，獲取到回應的結果資料 , 實作兩個覆寫方法(失敗&成功)
        call.enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG,"onFailure: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "onResponse: ${response.body?.string()}") // body有可能為空null,所以要加?(問號)
            }
        })

    }

}