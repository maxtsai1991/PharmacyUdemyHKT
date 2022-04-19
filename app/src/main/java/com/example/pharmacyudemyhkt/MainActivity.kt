package com.example.pharmacyudemyhkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmacyudemyhkt.OkHttpUtil.Companion.mOkHttpUtil
import com.example.pharmacyudemyhkt.data.PharmacyInfo
import com.example.pharmacyudemyhkt.databinding.ActivityMainBinding
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

/**
 *  3-22 使用GSON解析每間藥局名稱與電話
 *      層次中最需要注意的是，大括號 object {} 與中括號 array []，層次概念。瞭解之後，我們就可以透過 for 迴圈，取出每一間藥局名稱,電話,地址。
 *          val pharmaciesData = response.body?.string()
 *          val pharmacyInfo = Gson().fromJson(pharmaciesData, PharmacyInfo::class.java)
 *
 *          for (i in pharmacyInfo.features) {
 *              Log.d(TAG, "藥局名稱: ${i.property.name}")
 *              Log.d(TAG, "藥局電話 : ${i.property.phone}");
 *              Log.d(TAG, "藥局地址 : ${i.property.address}");
 *          }
 *       輸出結果 :
 *              D/MainActivity: 藥局名稱 : 理得藥局
 *              D/MainActivity: 藥局電話 : (02)28289680
 *              D/MainActivity: 藥局地址 : 臺北市北投區石牌路１段１５０號
 */

/**
 *  3-23 JSON To Class外掛套件安裝與使用介紹
 *      安裝外掛套件：「JSON To Kotlin Class」，快速將 JSON 資料轉換成 Class
 *      此AutoGeneratePharmacyInfo.kt檔案,是由JSON To Kotlin Class外掛套件產生出來,因已經有PharmacyInfo.kt,所以就不需要使用自動產生的,只是練習自動產生的方式,因而註解掉
 *      有一個重點 !!! -- 這些 data class在Kotlin裡面,是可以寫在同一個類別裡面 , 而在Java 都需要各別寫在不同類別檔案 --
 */

/**
 *  3-24 補充課程:如何使用元件綁定View Binding
 *       1. build.gradle(Module) 添加 :
 *          //Android Studio 4.0 或更高版本
 *              android {
                        ...
                        buildFeatures {
                        viewBinding true
                        }
                }
 *
 *       2. activity_main.xml 放一個測試用的TextView , 新增一個 id 屬性，屬性值設定為為 tv1
 *       3. 在MainActivity 使用方式 :
 *          4.  添加屬性 : private lateinit var binding: ActivityMainBinding
 *          5.  添加在onCreate裡,並註解調原本的設定Layout程式碼 :
 *                   //setContentView(R.layout.activity_main)
 *
 *                   binding = ActivityMainBinding.inflate(layoutInflater)
 *                   setContentView(binding.root) // 點擊root可進到MainActivity Layout畫面 , 等同activity_main ,因改用View Binding的原因
 *
 *       6. 測試TextView使用ViewBing方式更改原文字 :
 *                   binding.tv1.text = "MAX專案實作"
 */

/**
 *  3-25 如何顯示(ProgressBar)讀取API資料顯示忙碌圈圈
 *          1. 畫面佈局中，加入忙碌圈圈(ProgressBar)元件 :
                    <ProgressBar
                    android:id="@+id/progressBar"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    app:layout_constraintBottom_toBottomOf="parent"
                    app:layout_constraintLeft_toLeftOf="parent"
                    app:layout_constraintRight_toRightOf="parent"
                    app:layout_constraintTop_toTopOf="parent" />
            2.  顯示忙碌圈圈
                    binding.progressBar.visibility = View.VISIBLE
            3.  關閉忙碌圈圈
                    binding.progressBar.visibility = View.GONE
 */

/**
 *  3-27 如何封裝 OKHttp ? (參考講義:https://tw-hkt.blogspot.com/2021/01/android-okhttp.html)
 *       A : 使用單例模式(Singleton)確保 OkHttpClient只有一個實例存在，減少連線反應延遲與降低記憶體空間，改善提高整體運行效能。這次只簡單封裝 get 功能
 *       Util命名補充 :
 *          如果是一個很常用的工具,命名規則就會在類別名後面加個Util
 *
 *       companion object 補充說明 :
 *           1.  類似Java的靜態(Static關鍵字,因為Kotlin沒有類似Java的Static關鍵字用法)
 *           2.  通常單例也會寫在裡面 EX :   val mOkHttpUtil: OkHttpUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {  OkHttpUtil()  }
 *
 *       init 補充說明 :
 *           1.  要初始化(New出物件)的程式碼可以寫在裡面 EX : mOkHttpClient = OkHttpClient().newBuilder().build()    // 宣告 OkHttpClient
 *           2.  只有在第一次的時候,被呼叫起來的時候才會去跑init{}方法,之後的人都不會去跑init{}方法,除非init裡面的物件被銷毀掉,如果被銷毀掉就回再重作一遍
 *
 *       Callback補充 :
 *          假設A去呼叫B,B會透過一個介面(interface),再把資料帶回去給A
 *          以該章節為例,Activity就是A,Util就是B ,Util這個工具,如何回傳給Activity,就是透過interface回傳
 */

/**
 *  3-28 如何使用我們封裝的OKHttp ? (參考講義:https://tw-hkt.blogspot.com/2021/01/android-okhttp.html)
 *          自定義 OkHttpUtil，封裝 OkHttp，簡化繁雜步驟的程式碼，之後呼叫變得很簡單俐落
 *          程式碼可參考該章節老師的講義 : https://tw-hkt.blogspot.com/2021/01/android-okhttp.html
 *          使用方式 :
                    val your_url_name = "your_url"
                    mOkHttpUtil.getAsync(your_url_name, object : OkHttpUtil.ICallback {
                        override fun onResponse(response: Response) {
                            ...
                        }
                        override fun onFailure(e: okio.IOException) {
                            ...
                        }
                    })
 */

/**
 *  3-29 固定常數檔案Constants(常數)用途? (參考講義:https://tw-hkt.blogspot.com/2021/01/android-okhttp.html)
 *          該章節變動補充說明 : 將OKHttp封裝起來(OkHttpUtil.kt) & 增加 Constants(Constants.kt)
 *          未來會有很多常數固定資料，為了更好管理與維護，我們習慣會將常數資料，特別獨立出去放到如：Constants.kt。以這次口罩資料網址為例，我們就可以將它整理歸納寫到這個檔案裡。
 *          而固定常數的命名風格，習慣全大寫，單字與單字之間透過底線區隔，如：PHARMACIES_DATA_URL
 */

/**
 *  4-34 如何使用RecyclerView結合自定義Adapter資料顯示
 *      1. 拿掉先前章節在activity_main.xml裡面的ScrollView & TextView(id:tv_pharmacies_data), 加上RecyclerView(將ScrollView取代成RecyclerView , id : recycler_view)
 *      2. 設定RecyclerView EX : initView()
 *      3. 指定API資料給Adapter EX : viewAdapter.pharmacyList = pharmacyInfo.features
 *      4. 確認Adapter的onBindViewHolder EX : holder.itemViewBinding.tvName.text = pharmacyList[position].property.name  // 藥局名稱
 */

/**
 *  4-35 如何設定RecyclerView顯示分隔線與點擊漣漪效果 (https://tw-hkt.blogspot.com/2021/01/android-recyclerview.html)
 *          設定項目分隔線 (divider) : addItemDecoration(DividerItemDecoration(this@MainActivity,DividerItemDecoration.VERTICAL))
 *          點擊漣漪效果(Ripple Effect) (在item_view.xml設定):
 *                                      android:clickable="true"
 *                                      android:focusable="true"
 *                                      android:background="?android:attr/selectableItemBackground"
 */

/**
 *  4-36 RecyclerView 進階項目佈局 (https://tw-hkt.blogspot.com/2021/01/android-recyclerview_22.html)
 *
 */
class MainActivity : AppCompatActivity() {
    /**
     * companion object 補充說明 :
     *          1.  類似Java的靜態(Static關鍵字,因為Kotlin沒有類似Java的Static關鍵字用法)
     *          2.  通常單例也會寫在裡面
     */
    companion object{

    }

    val TAG = MainActivity::class.java.simpleName
    // lateinit 補充說明 : 暫時先宣告變數,實際使用它的時候,在把它初始化
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main) // 因使用View Binding,所以不須使用原始寫法來設定Layout

        /**
         * 使用元件綁定View Binding的MainActivity Layout綁定畫面宣告
         */
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // 點擊root可進到MainActivity Layout畫面 , 等同activity_main ,因改用View Binding的原因

        // 測試TextView的View Binding方式
        binding.tv1.text = "-Max口罩地圖專案-"

        // RecyclerView設定
        initView()

        // 取得口罩地圖資料的方法
        getPharmacyData()
    }

    private fun initView() {
        // 定義 LayoutManager 為 LinearLayoutManager (決定RecyclerView如何顯示)
        viewManager =  LinearLayoutManager(this)
        // 自定義 Adapte 為 MainAdapter，稍後再定義 MainAdapter 這個類別
        viewAdapter = MainAdapter()

        /**
         * 定義從佈局當中，拿到 recycler_view 元件 , 透過 kotlin 的 apply 語法糖，設定 LayoutManager 和 Adapter
         *      binding.recyclerView.apply { } 說明:
         *          這樣寫可省去裡面程式碼還要加binding.recyclerView EX :  binding.recyclerView.layoutManager or binding.recyclerView.adapter ,
         *          就不用寫成傳統寫法
         */
        binding.recyclerView.apply {
            layoutManager = viewManager // 傳統寫法 : binding.recyclerView.layoutManager = viewManager
            adapter = viewAdapter // 傳統寫法 : binding.recyclerView.adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(this@MainActivity,DividerItemDecoration.VERTICAL)) // 每個Item下的分隔線
        }
    }

    private fun getPharmacyData() {
        // 當還在讀取API資料時,顯示讀取圈圈
        binding.progressBar.visibility = View.VISIBLE

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
         * 使用OKHttp封裝,並且用了Callback,以及將API URL放在常用常數檔裡,PHARMACIES_DATA_URL(口罩資料網址)
         */
        mOkHttpUtil.getAsync(PHARMACIES_DATA_URL, object : OkHttpUtil.ICallback {
            override fun onResponse(response: Response) {
                // 藥局名稱變數宣告 (New出一個StringBuilder物件;錯誤示範(當資料量大時,會OOM)EX : val propertiesName : String)
                val propertiesName = StringBuilder()

                // 將整包資料拉下來存起來 【API所有資料】
                val pharmaciesData : String? = response.body?.string() // body有可能為空null,所以要加?(問號)

                /**
                 * Gson寫法 :
                 * 使用Gson,不用特別處理API資料是否為空值(EX : xxx.isNull(type)) 及 有無欄位的事件處理(EX : xxx.has(type)) ,
                 * 因為如果該欄位不存在,最多回饋給你null , null有兩種可能 : 1. 一種是真的沒有資料 2. 另一種是欄位名稱(EX : type)打錯,要打得跟API欄位名稱一樣
                 */
                val pharmacyInfo = Gson().fromJson(pharmaciesData,PharmacyInfo::class.java) // .fromJson(資料來源 , 希望把資料來源轉換成怎樣的格式,希望轉換成PharmacyInfo類別)
//                Log.d(TAG, "my_type(Gson) : ${pharmacyInfo.my_type}"); // 拿出pharmacyInfo資料裡面的type欄位Values值

                //會註解掉是因為在第四章節前用ScrollView ,第四章開始用RecyclerView就不需要這些程式碼
//                for(i in pharmacyInfo.features){
//                    propertiesName.append(i.property.name + "\n")
//                    Log.d(TAG, "藥局名稱 : ${i.property.name}");
//                    Log.d(TAG, "藥局電話 : ${i.property.phone}");
//                    Log.d(TAG, "藥局地址 : ${i.property.address}");
//                }

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
                for (i in 0 until featuresArray.length()){
                    val properties = featuresArray.getJSONObject(i).getString("properties") // 取得features(陣列)裡面的properties(object)
                    val property = JSONObject(properties)// 取得properties(object)裡面的name , phone , address
//                    Log.d(TAG, "第${i}間藥局: ")
//                    Log.d(TAG, "藥局名稱 : ${property.getString("name")}")
//                    Log.d(TAG, "電話 : ${property.getString("phone")}")
//                    Log.d(TAG, "地址 : ${property.getString("address")}")
                    propertiesName.append(property.getString("name")+"\n") // propertiesName字串池(藥局名稱),將每一筆藥局名稱添加進去,並且每一筆都要換行
                }

                //注意要設定UI (tv_pharmacies_data.text) ，需要執行在UiThread裡面(runOnUiThread { })，否則會噴錯誤 ; 重點:當解析的資料要呈現到畫面上,必須要用runOnUiThread { }包起來 (控制UI上面操作要寫在runOnUiThread{})
                runOnUiThread {
                    //將 Okhttp 獲取到的回應值，指定到畫面的 TextView 元件中 (註解掉是因為在第四章節前用ScrollView裡面有包一個TextView(tv_pharmacies_data),第四章開始用RecyclerView就不需要這些程式碼)
//                    tv_pharmacies_data.text = propertiesName

                    //將下載的口罩資料，指定給 MainAdapter , pharmacyInfo是下載回來的資料,主要是拿features的資料
                    viewAdapter.pharmacyList = pharmacyInfo.features

                    // 當讀到API資料成功時,不顯示讀取圈圈
                    binding.progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(e: okio.IOException) {
                Log.d(TAG,"onFailure: $e")

                // 當讀到API資料失敗時,不顯示讀取圈圈 , 控制UI上面操作要寫在runOnUiThread{}
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
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