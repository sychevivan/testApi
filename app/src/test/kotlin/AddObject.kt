package com.example.example

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class AddObject (

  @SerialName("id"        ) var id        : String? = null,
  @SerialName("name"      ) var name      : String? = null,
  @SerialName("data"      ) var data      : AddObjectData?   = AddObjectData(),
  @SerialName("createdAt" ) var createdAt : String? = null

)