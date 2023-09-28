package com.spongycode.servicenet.data.model

data class University(
    val domains: List<String>? = null,
    val name: String,
    val stateProvince: String? = null,
    val web_pages: List<String>? = null,
    val country: String? = null,
    val alphaTwoCode: String? = null
)
