package com.example.example

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class AddObjectRequest (

  @SerialName("name" ) var name : String? = null,
  @SerialName("data" ) var data : AddObjectRequestData?   = AddObjectRequestData()

)