package com.adaptixinnovate.tanvirahmedrobin.constants

import com.adaptixinnovate.tanvirahmedrobin.services.SharedPrefereneService

object AppConfig {

    lateinit var BASE_URL : String
    lateinit var IMG_URL : String


    fun setUrl(url: String) {
        BASE_URL = "${url}api/"
        IMG_URL = url

    }
}