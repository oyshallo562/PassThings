package com.degitalcon.passthings.ui.home

data class ArticleModel(
    val sellerId: String,
    val title: String,
    val createdAt: Long,
    val price: String,
    val description :String,
    val tag :String,
    val imageURL: String
) {
    constructor(): this("", "", 0, "", "","", "")
}