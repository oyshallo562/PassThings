package com.degitalcon.passthings.ui.chat.chatdetail

data class ChatItem(
    val senderId: String,
    val message: String,
) {
    constructor(): this("", "")
}
