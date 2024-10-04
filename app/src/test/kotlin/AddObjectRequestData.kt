package com.example.example

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class AddObjectRequestData (

  @SerialName("year"  )      var   year  :   Int?    =    null,              
  @SerialName("price" )      var   price :   Double? =    null,              
  @SerialName("CPU    model" )     var   CPUModel   :    String? = null,
  @SerialName("Hard   disk   size" )     var HardDiskSize    : String? = null

)