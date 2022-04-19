package com.example.pharmacyudemyhkt

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmacyudemyhkt.data.Feature
import com.example.pharmacyudemyhkt.databinding.ItemViewBinding

/**
 * 4-33 如何自定義Adapter資料顯示容器
 *      創建MainAdapter > 繼承Adapter > 放ViewHolder當Adapter參數 > 創建ViewHolder類別 > ViewHolder類別給itemViewBinding參數 > 繼承ViewHolder > 再給剛剛的itemViewBinding參數 >
 *      覆寫/實作Adapter三個方法 > 1. onCreateViewHolder 2. onBindViewHolder 3. getItemCount
 *      1.  繼承Adapter(容器)
 *      2.  Adapter後面定義要長怎樣的view,設計/定義MainAdapter.MyViewHolder ,後面記得要加一個空的建構子()
 *      3.  MyViewHolder有紅線,燈泡熱鍵,建立MyViewHolder類別(選Create class 'MyViewHolder')
 *      4.  MyViewHolder類別,給他參數itemViewBinding EX : (val itemViewBinding: ItemViewBinding) ,注意必須去繼承RecyclerView.ViewHolder,以及給他參數view,因為用View Binding,所以要這樣寫 EX : (itemViewBinding.root)
 *      5.  MyViewHolder類別,不需要大括號{} ,去掉大括號
 *      6.  MainAdapter實作/覆寫Adapter三個方法 (紅線燈泡熱鍵,選Implement members) : 1. onCreateViewHolder 2. onBindViewHolder 3. getItemCount
 *      7.  撰寫三個複寫的方法
 *      8.  onCreateViewHolder : 放item畫面,裡面的程式碼都是固定寫法
 *      9.  onCreateViewHolder : 綁定item裡面的元件(textView(藥局名稱)),如還有其他元件,就需要在寫,該範例老師只有放TextView
 *      10. getItemCount : 放資料大小
 *
 */

class MainAdapter (private val itemClickListener: IItemClickListener) : RecyclerView.Adapter<MainAdapter.MyViewHolder>(){

    var pharmacyList : List<Feature> = emptyList() // 一開始預設是空的列表
        set(value) {
            field = value           // field指的就是pharmacyList
            notifyDataSetChanged()  // 刷新(如果pharmacyList有資料進來,那我們就去通知RecyclerView有資料變化,去做資料的更新)
        }

    /**
     *  View Binding的寫法的好處 : 可省去傳統需要先宣告變數名稱,以及findViewById才能使用元件,要使用元件還需要在onBindViewHolder裡綁定
     */
    class MyViewHolder (val itemViewBinding: ItemViewBinding) : RecyclerView.ViewHolder(itemViewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemViewBinding = ItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        /**
         * tvName 指的就是item_view.xml的藥局名稱(TextView ID) , tvName(TextView的ID)會自動生成是因為View Binding
         * text 指的是Setter/Getter ,在Kotlin則是直接text即可
         * pharmacyList[position].property.name 指的是拿pharmacyList所在位置([position])裡面的property裡面的name(藥局名稱)
         * 如要在添加RecyclerView顯示的資訊 :
         *      1. 在item_view.xml添加新的TextView並給予ID
         *      2. 在MainAdapter的onBindViewHolder添加holder.itemViewBinding.tvPhone.text = pharmacyList[position].property.phone (注意:要去檢查PharmacyInfo.kt)
         */
        holder.itemViewBinding.tvName.text = pharmacyList[position].property.name  // 藥局名稱
        holder.itemViewBinding.tvPhone.text = pharmacyList[position].property.phone // 藥局電話
        holder.itemViewBinding.tvAdultAmount.text = pharmacyList[position].property.mask_adult.toString() // 成人口罩數量
        holder.itemViewBinding.tvChildAmount.text = pharmacyList[position].property.mask_child.toString() // 小孩口罩數量

        /**
         * 點擊item的監聽事件
         * pharmacyList[position] : 各項目位置
         */
        holder.itemViewBinding.layoutItem.setOnClickListener {
            itemClickListener.onItemClickListener(pharmacyList[position])
        }
    }

    override fun getItemCount(): Int {
        return pharmacyList.size
    }

    /**
     * 定義 CallBack 介面
     * MainActivity.kt 跟 MainAdapter.kt 是透過這個IItemClickListener介面來做溝通 ,
     *      MainActivity 怎麼跟 MainAdapter說我要用的是IItemClickListener介面, 是透過MainAdapter參數(private val itemClickListener: IItemClickListener),
     *      並且itemClickListener有item的各項目的位置(pharmacyList[position]) EX : itemClickListener.onItemClickListener(pharmacyList[position])
     */
    interface IItemClickListener{
        fun onItemClickListener(data: Feature)
    }
}