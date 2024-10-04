package com.example.example

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Data(

    @SerialName("color") var color: String? = null,
    @SerialName("capacity GB") var capacityGB: Int? = null,
    @SerialName("price") var price: Float? = null,
    @SerialName("Screen size") var screenSize: Float? = null
)