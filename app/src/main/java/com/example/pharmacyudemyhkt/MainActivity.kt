package com.example.pharmacyudemyhkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
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

/**
 * 3-13 補充OKHttp中的POST請求方法
 * 該章節補充說明 :
 * 1.   口罩資料，沒有POST功能，這裡HKT老師另外選用Reqres網站，來練習Post請求如何使用 ; Reqres網站是一個提供假資料API串接的網站，方便練習 RESTful API
 * 2.   提供API假資料的網站 : https://reqres.in/
 * 3.   該章節使用提供假資料API串接的網站 : https://reqres.in/api/user
 * 該章節使用方式 :
 * 1.   使用 提供假資料串接的網站(REQRES , https://reqres.in/ ) EX : https://reqres.in/api/users
 * 2.   在FormBody參數.add方法打自訂義的name,value , 就會在Logcat上回應Json格式,自己所打的資料
 *
 * ------ 注意事項 : 記得使用完POST測試,要註解起來,因為該口罩地圖專案,不會用到POST請求,並且使用非同步(enqueue)方式,如要測試同步(execute),需使用ExecuteDemo.kt,因為同步不能在MainActivity頁面使用,會阻塞主執行緒,相關事項可參考3-12章節------
 */

/**
 * 3-14 使用TextView 和 ScrollView 將口罩資料顯示在畫面上
 * (JSON 資料解析方式參考網址 : https://tw-hkt.blogspot.com/2020/12/android-json.html)
 * 補充 :
 * 1.   build.gradle(Module),添加下列兩行,才能在Activity找到Layout各類元件的ID:
 *          id 'kotlin-kapt'
 *          id 'kotlin-android-extensions'
 * 2.   修改模擬器該APP顯示名稱 & 修改口罩APP的圖標
 * 3.   連線到口罩資料網址，獲取到回應資料，這個動作可以被稱爲是「呼叫 API」,
 *      APP手機裝置端與遠端伺服器互相傳遞資料，我們通常會透過API來溝通,
 *      手機獲取伺服器資料，通常採用 GET 或 POST 方式 EX :
 *          val request : Request = Request.Builder().url("https://raw.githubusercontent.com/thishkt/pharmacies/master/data/info.json").get().build()
 *          val call : Call = okHttpClient.newCall(request)
 *          call.enqueue(object : Callback{
 *              override fun onFailure(call: Call, e: IOException) { }
 *              override fun onResponse(call: Call, response: Response) { }
 *          }
 */

class MainActivity : AppCompatActivity() {
    companion object{
        val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPharmacyData() // 取得口罩地圖資料的方法
    }

    private fun getPharmacyData() {
        // GET請求使用方式 : 口罩地圖是使用GET請求,如要測試POST請求,需註解該方法
        getDemo()

        // POST請求使用方式 : 查看3-13說明及注意事項
//         postDemo()
    }

    /**
     * GET請求 , 口罩地圖是使用Get去請求 , 如要測試POST請求 , 需註解該方法
     */
    private fun getDemo() {
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
                val pharmaciesData : String? = response.body?.string()

                //注意要設定UI (tv_pharmacies_data.text) ，需要執行在UiThread裡面(runOnUiThread { })，否則會噴錯誤
                runOnUiThread {
                    //將 Okhttp 獲取到的回應值，指定到畫面的 TextView 元件中
                    tv_pharmacies_data.text = pharmaciesData
//                Log.d(TAG, "onResponse: $pharmaciesData") // body有可能為空null,所以要加?(問號)
                }
            }
        })
    }

    /**
     * POST請求使用方式,查看3-13說明及注意事項
     */
    private fun postDemo() {
        // POST請求的測試網址 (提供假資料串接的網站)
        val testUrl = "https://reqres.in/api/users"

        //Part 1: 宣告 OkHttpClient
        val okHttpClient = OkHttpClient().newBuilder().build()

        //加入 FormBody 參數 name 和 job 。
        val formBody: FormBody = FormBody.Builder()
            .add("name", "MAXTsai") // 名字
            .add("job", "Engineersss") // 工作
            .build()

        //Part 2: 宣告 Request，要求要連到指定網址
        val request: Request = Request.Builder().url(testUrl).post(formBody).build() // url(連線的網址) ; .post意思是使用post請求,參數放進剛剛所帶入的FormBody 參數

        //Part 3: 宣告 Call
        val call = okHttpClient.newCall(request)

        //執行 Call 連線後，採用 enqueue 非同步方式，獲取到回應的結果資料 , 覆寫以及監聽兩個方法(成功或失敗)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace() // 如果報錯,把錯印出來
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "onResponse: ${response.body?.string()}")
            }

        })
    }

}