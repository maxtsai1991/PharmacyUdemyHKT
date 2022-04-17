package com.example.pharmacyudemyhkt

import okhttp3.*
import java.io.IOException

/**
 * 該OkHttpUtil類別說明:
 *      自定義 OkHttpUtil，封裝OkHttp，簡化繁雜步驟的程式碼，之後呼叫變得很簡單俐落
 *      程式碼可參考該章節老師的講義 : https://tw-hkt.blogspot.com/2021/01/android-okhttp.html
 */
class OkHttpUtil {
    private var mOkHttpClient: OkHttpClient? = null

    companion object {
        val mOkHttpUtil: OkHttpUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            OkHttpUtil()
        }
    }

    init {
        //Part 1: 宣告 OkHttpClient
        mOkHttpClient = OkHttpClient().newBuilder().build()
    }

    //Get 非同步 (宣告(設定)Request，要求要連到指定網址 ,透過GET方式)
    fun getAsync(url: String, callback: ICallback) {
        //Part 2: 宣告 Request，要求要連到指定網址
        val request = with(Request.Builder()) {
            url(url)
            get()
            build()
        }

        //Part 3: 宣告 Call (要連線到哪個網址,用怎麼樣方式去做連線,把上面的request塞給newCall)
        val call = mOkHttpClient?.newCall(request)

        //執行 Call 連線後，採用 enqueue 非同步方式，獲取到回應的結果資料 , 實作兩個覆寫方法(失敗&成功)
        call?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(response)
            }
        })
    }


    interface ICallback {
        fun onResponse(response: Response)

        fun onFailure(e: IOException)
    }
}