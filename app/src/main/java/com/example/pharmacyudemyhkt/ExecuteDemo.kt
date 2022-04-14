package com.example.pharmacyudemyhkt

import android.util.Log
import okhttp3.*
import java.io.IOException

/**
 * 3-12 實戰OKHttp中的execute同步處理GET請求方法
 * 該檔案是測試同步(execute)方式取得資料
 * 要察看結果 , 要使用左邊的執行鍵 , 結果會在Run標籤
 * 會這樣測試是因為同步方式不能在Android主執行緒測試 , 所以才會另開ExecuteDemo.kt檔案測試
 */
fun main() {
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

    // 同步方式 (execute())
    val result : Response = call.execute()
    println("onResponse(執行同步方式) : ${result.body?.string()}" )

}