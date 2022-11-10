package com.degitalcon.passthings.ui.home

data class ArticleModel(
    val sellerId: String,
    val title: String,
    val createdAt: Long,
    val price: String,
    val description :String,
    val tag :String,
    val imageURL: String,
    val Pass: Int
) {
    constructor(): this("", "", 0, "", "","", "",0)
}
