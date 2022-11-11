package com.degitalcon.passthings.ui.chat.chatdetail

data class ArticleModelTmp(
    var sellerId: String,
    var title: String,
    var createdAt: Long,
    var price: String,
    var description :String,
    var tag :String,
    var imageURL: String,
    var Pass: Int
) {
    constructor(): this("", "", 0, "", "","", "",0)
}
