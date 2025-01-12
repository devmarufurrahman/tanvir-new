package com.adaptixinnovate.tanvirahmedrobin.constants

import com.adaptixinnovate.tanvirahmedrobin.services.SharedPrefereneService

object AppConfig {

    var BASE_URL = ""
    var IMG_URL = ""


    fun setUrl(url: String) {
        BASE_URL = "${url}api/"
        IMG_URL = url

    }
}