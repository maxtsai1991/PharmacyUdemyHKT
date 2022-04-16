package com.example.pharmacyudemyhkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pharmacyudemyhkt.data.PharmacyInfo
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.lang.StringBuilder

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

/**
 * 3-15 Json資料格式簡介
 *      JSON 資料格式是一種輕量級的資料交換格式，程式很容易建立與解析，人類也易於閱讀與書寫。JSON 格式存放方式，採物件概念，使用大括號 {} 來包覆，裡面資料為採 key 與 value ，中間使用冒號：來分隔。例如：
 *          {
 *              "name" : "HKT"
 *          }
 *      常見的 JSON 資料格式，有數字、字串、布林值，而同類型資料，可以使用中括號[]來包覆，每筆資料採用逗號做分隔
 *          {
 *              "name" : "HKT",
 *              "age": 18,
 *              "class":["Java","Kotlin","Dart"]
 *          }
 */

/**
 * 3-16 JSON Viewer線上小工具
 *       JSON 線上小工具 :
 *          可以試著將口罩資料轉貼到 Online JSON Viewer(http://jsonviewer.stack.hu/)的網頁右上方的 Text 頁籤中，完成之後可以按左邊的 Viewer 頁籤，即可透過這套線上 JSON 小工具，快速掌握整個 JSON 資料結構
 */

/**
 * 3-17 解析JSON範例一
 *      解析 JSON 資料格式，注意的是層次概念，以 口罩資料 為例，若我們要取得最外層資料，可以直接獲取，例如 「 “type”: “FeatureCollection”」，我們解析方式可以寫成這樣：
 *          從 Okhttp 收到的回應資料 response，取出 body 的部分,注意這裏，response 不能二次使用，不然會噴錯誤。所以我們將他轉存到 pharmaciesData
 *              val pharmaciesData = response.body?.string()
 *          將 pharmaciesData 整包字串資料，轉成 JSONObject 格式
 *              val obj = JSONObject(pharmaciesData)
 *          這個時候，我們就可以透過 getString 的方式，裡面放 key (name) 值， 即可以獲取到最外層的 type 欄位資料值。
 *              Log.d(TAG, "type : ${obj.getString("type")}")
 *      輸出結果 : FeatureCollection
 */

/**
 *  3-18 解析JSON範例二
 *          JsonObject : {}
 *          JsonArray : []
 *      如果我們要獲取的是 features 裡面的 properties 裡面的 name。解析 JSON 資料，除了要注意層次外，還要注意結構。features 是一個陣列 [] ，中括號來包覆資料，就需要將他轉換成 JSONArray。
 *          val pharmaciesData = response.body?.string() // 取API所有資料
 *      將 pharmaciesData 整包字串資料，轉成 JSONObject 格式
 *          val obj = JSONObject(pharmaciesData)
 *      features 是一個陣列 [] ，需要將他轉換成 JSONArray
 *          val featuresArray = JSONArray(obj.getString("features"))
 *      透過 for 迴圈，即可以取出所有的藥局名稱
 *          for (i in 0 until featuresArray.length()) {
 *               val properties = featuresArray.getJSONObject(i).getString("properties")
 *               val property = JSONObject(properties) // 取得properties裡面的name , phone , address
 *               Log.d(TAG, "藥局名稱 : ${property.getString("name")}")
 *               Log.d(TAG, "電話 : ${property.getString("phone")}")
 *               Log.d(TAG, "地址 : ${property.getString("address")}")
 *          }
 */

/**
 *  3-19 使用 StringBuilder組合字串資料
 *       補充說明 : 為何使用StringBuilder而不是String ? A : 因使用 String 處理串接文字，當資料很多時很容易造成 OOM 記憶體不足，建議換成 StringBuilder
 *       藥局名稱變數宣告 EX : val propertiesName = StringBuilder()
 *       將每次獲取到的藥局名稱，多加跳行符號，存到變數中 EX : propertiesName.append(property.getString("name")+"\n")
 *       最後取得所有藥局名稱資料，指定顯示到 TextView 元件中 EX : tv_pharmacies_data.text = propertiesName
 */

/**
 *  3-20 解析Json發生例外處理方式
 *   情境 : 若資料中沒有對應的 key (name)值，會發生例外狀況(Exception ,java.lang.Error: org.json.JSONException: No value for typeeeee)
 *      val pharmaciesData : String? = response.body?.string()
 *      val obj = JSONObject(pharmaciesData)
 *      Log.d("HKT",obj.getString("typeeeee")) //資料沒有 typeeeee 這個 key(name)值，直接會噴錯誤
 *   這個時後可以透過 has 或 isNull 方法來避免例外錯誤：
 *      方法一：使用 has 判斷是否存在這個資料，存在時才獲取資料
 *          if(obj.has("typeeeee")){
 *              Log.d(TAG,obj.getString("typeeeee"))
 *          }else{
 *              Log.d(TAG,"has 判斷沒有這個資料")
 *          }
 *      方法二：使用 isNull 判斷是為空，不為空才獲取資料
 *          if(!obj.isNull("typeeeee")){
 *              Log.d(TAG,obj.getString("typeeeee"))
 *          }else{
 *               Log.d(TAG,"isNull 判斷，沒有這個資料")
 *          }
 *   但例外有時真的出乎意料，所以在解析資料時，為了避免不可預期錯誤造成 APP 閃退，會多加 try…catch 來防止，如：
 *          try {
 *              JSONObject result = new JSONObject();
 *              ...
 *          } catch (e: JSONException) {
 *              throw new RuntimeException(e);
 *          }
 */

/**
 *  3-21 GSON使用介紹
 *  添加 Gson 依賴庫 (dependencies) : implementation 'com.google.code.gson:gson:2.8.6'
 *  JSON 資料轉換成 Gson 是透過 Gson 提供的 fromJson(String json, Class<T> classOfT) 這個方法。其中，fromJson 第一個欄位，要帶入的即是我們透過 Okhttp 獲取到的資料，第二個欄位，需帶入我們自己定義的類別。
 *
 *  pharmaciesData 為 Okhttp 獲取到的資料
 *      val pharmaciesData = response.body?.string()
 *  PharmacyInfo 是我們自定義類別
 *      val pharmacyInfo = Gson().fromJson(pharmaciesData, PharmacyInfo::class.java)
 *
 *  JSON 資料轉換成 Gson，仍須注意的是資料層次概念以 口罩資料 為例，若我們要取得最外層資料，如：「 “type”: “FeatureCollection”」，我們可以寫成這樣：
 *      class PharmacyInfo(
 *          @SerializedName("type")
 *          val type: String
 *      )
 *
 *  SerializedName 裡面要填的是，真實資料中的名稱，我們要獲取 type 所以填 type，所以寫成「@SerializedName(“type”)」以此類推。而如果你不喜歡，原本資料定義的名稱，你可以自定義常數值名稱，例如，想改成 my_type，就可以寫成這樣：
 *      class PharmacyInfo(
 *          @SerializedName("type")
 *          val my_type: String
 *      )
 *  而如果我們想要印出資料，可以寫成這樣： Log.d(TAG, "my_type: ${pharmacyInfo.my_type}")
 *  輸出結果 : my_type: FeatureCollection
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
        /**
         * 口罩資料網址 (資料來源)
         *      部分藥局口罩資料(其中一天) : "https://raw.githubusercontent.com/thishkt/pharmacies/fafd14667432171227be3e2461cf3b74f9cb9b67/data/info.json"
         *      完整藥局口罩資料(其中一天) : "https://raw.githubusercontent.com/thishkt/pharmacies/master/data/info.json"
         */
        val pharmaciesDataUrl = "https://raw.githubusercontent.com/thishkt/pharmacies/fafd14667432171227be3e2461cf3b74f9cb9b67/data/info.json"

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
                // 將整包資料拉下來存起來 【API所有資料】
                val pharmaciesData : String? = response.body?.string()
//                Log.d(TAG, "onResponse: $pharmaciesData") // body有可能為空null,所以要加?(問號)

                /**
                 * Gson寫法
                 * 使用Gson,不用特別處理API資料是否為空值(EX : xxx.isNull(type)) 及 有無欄位的事件處理(EX : xxx.has(type)) , 因為如果該欄位不存在,最多回饋給你null , null有兩種可能 : 1. 一種是真的沒有資料 2. 另一種是欄位名稱(EX : type)打錯,要打得跟API欄位名稱一樣
                 */
                val pharmacyInfo = Gson().fromJson(pharmaciesData,PharmacyInfo::class.java) // .fromJson(資料來源 , 希望把資料來源轉換成怎樣的格式,希望轉換成PharmacyInfo類別)
                Log.d(TAG, "my_type(Gson) : ${pharmacyInfo.my_type}"); // 拿出pharmacyInfo資料裡面的type欄位Values值


                /**
                 * Json寫法
                 */
                // API拿到的字串資料(整包資料),透過JSONOBJECT方法轉換成jsonobject格式,再存到obj 【所有資料轉成jsonObject】
                val obj = JSONObject(pharmaciesData)

                // 當API資料(obj)發生JSONException or Exception , 則印出例外訊息 (防止例外而閃退APP)
                try {
                    // 如果API資料不為空值(!obj.isNull),也有"typeeeee"key值,就LOG出來(才去解析該筆資料),否則就LOG出"沒有這個值"的字串
                    if(!obj.isNull("typeeeee")){
                        Log.d(TAG,"type : ${obj.getString("typeeeee")}")
                    }else{
                        Log.d(TAG,"沒有這個值")
                    }
                } catch (e: JSONException) {
                    Log.d(TAG,"JSONException : $e")
                } catch (e: Exception) {
                    Log.d(TAG,"Exception : $e")
                }

                // obj在透過getString方法,拿取KEY值,就會得到Value (Json格式的整包資料,拿取特定某一筆值)
                Log.d(TAG, "type(Json) : ${obj.getString("type")}")

                // features是一個陣列資料格式,所以轉成JSONArray【jsonObject的所有資料,在去拿features】
                val featuresArray = JSONArray(obj.getString("features"))

                // for迴圈,i介於起訖點從第0筆到最後一筆(featuresArray.length()) 【featuresArray陣列用for迴圈,在去拿每一筆的properties裡面的name & phone & address】
                val propertiesName = StringBuilder() // 字串池(藥局名稱), New出一個StringBuilder物件;錯誤示範(當資料量大時,會OOM)EX : val propertiesName : String
                for (i in 0 until featuresArray.length()){
                    val properties = featuresArray.getJSONObject(i).getString("properties") // 取得features(陣列)裡面的properties(object)
                    val property = JSONObject(properties)// 取得properties(object)裡面的name , phone , address
//                    Log.d(TAG, "第${i}間藥局: ")
//                    Log.d(TAG, "藥局名稱 : ${property.getString("name")}")
//                    Log.d(TAG, "電話 : ${property.getString("phone")}")
//                    Log.d(TAG, "地址 : ${property.getString("address")}")
                    propertiesName.append(property.getString("name")+"\n") // 字串池(藥局名稱),將每一筆藥局名稱添加進去,並且每一筆都要換行
                }

                //注意要設定UI (tv_pharmacies_data.text) ，需要執行在UiThread裡面(runOnUiThread { })，否則會噴錯誤 ; 重點:當解析的資料要呈現到畫面上,必須要用runOnUiThread { }包起來
                runOnUiThread {
                    //將 Okhttp 獲取到的回應值，指定到畫面的 TextView 元件中
                    tv_pharmacies_data.text = propertiesName
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