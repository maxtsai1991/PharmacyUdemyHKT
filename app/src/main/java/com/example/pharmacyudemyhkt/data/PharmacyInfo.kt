package com.example.pharmacyudemyhkt.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PharmacyInfo (
    // API資料網址 : https://raw.githubusercontent.com/thishkt/pharmacies/fafd14667432171227be3e2461cf3b74f9cb9b67/data/info.json
    // 下面欄位,一定要跟API資料同樣欄位名字,不然會報錯

    @SerializedName("type") // 使用映射 , 映射裡面要跟API欄位名稱一樣 , 這樣下面的type欄位名稱 , 可以隨意取自己要的 , 這樣用法主要是可自定義欄位名稱,可以不用跟API欄位名稱一樣
    val my_type: String,

    @SerializedName("features")
    val features: List<Feature>
    ):Serializable

class Feature(
    @SerializedName("properties")
    val property: Property
):Serializable

class Property(
    @SerializedName("name") // 藥局名稱
    val name: String,

    @SerializedName("phone") // 藥局電話
    val phone: String,

    @SerializedName("address") // 藥局地址
    val address: String,

    @SerializedName("mask_adult") // 成人口罩數量
    val mask_adult: Int,

    @SerializedName("mask_child") // 小孩口罩數量
    val mask_child: Int,
):Serializable