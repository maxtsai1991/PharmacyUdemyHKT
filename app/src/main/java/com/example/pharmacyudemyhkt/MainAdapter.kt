package com.example.pharmacyudemyhkt

import android.view.LayoutInflater
import android.view.ViewGroup
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

class MainAdapter :RecyclerView.Adapter<MainAdapter.MyViewHolder>(){

    var pharmacyList : List<Feature> = emptyList()
        set(value) {
            field = value           // field指的就是pharmacyList
            notifyDataSetChanged()  // 刷新(如果pharmacyList有資料進來,那我們就去通知RecyclerView有資料變化,去做資料的更新)
        }


    class MyViewHolder (val itemViewBinding: ItemViewBinding) : RecyclerView.ViewHolder(itemViewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemViewBinding = ItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        /**
         * tvName指的就是item_view.xml的藥局名稱(TextView ID) , tvName(TextView的ID)會自動生成是因為View Binding
         * text指的是Setter/Getter ,在Kotlin則是直接text即可
         */
        holder.itemViewBinding.tvName.text = ""
    }

    override fun getItemCount(): Int {
        return pharmacyList.size
    }

}