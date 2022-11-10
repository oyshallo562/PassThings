package com.degitalcon.passthings.ui.dashboard

data class UserInfoModel (
    var name :String? = null,
    var uid :String? = null,
    var profileImage :String? = null
){
    constructor(): this("", "","")
}