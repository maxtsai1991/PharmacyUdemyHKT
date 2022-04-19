package com.example.pharmacyudemyhkt.data

/**
 * 此 data class 裡面包含多個 data class 都是由JSON To Kotlin Class外掛套件產生出來 ,因已經有在前面章節手動新增PharmacyInfo.kt類別 , 所以該3-23 外掛套件產生出來,就不需要使用,因而註解掉
 * 有一個重點 !!!
 * 這些 data class在Kotlin裡面,是可以寫在同一個類別裡面 , 而在Java 都需要各別寫在不同類別檔案
 */
//data class AutoGeneratePharmacyInfo(
//    val type: String,
//    val features: List<Feature>
//):java.io.Serializable
//
//data class Feature(
//    val type: String,
//    val properties: Properties,
//    val geometry: Geometry
//):java.io.Serializable
//
//data class Geometry(
//    val type: String,
//    val coordinates: List<Double>
//):java.io.Serializable
//
//data class Properties(
//    val id: String,
//    val name: String,
//    val phone: String,
//    val address: String,
//    val mask_adult: Int,
//    val mask_child: Int,
//    val updated: String,
//    val available: String,
//    val note: String,
//    val custom_note: String,
//    val website: String,
//    val county: String,
//    val town: String,
//    val cunli: String,
//    val service_periods: String
//):java.io.Serializable