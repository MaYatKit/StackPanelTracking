package com.framecad.plum.data.response

import com.google.gson.annotations.SerializedName

data class SimpleProjectResponse(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("percentage") val percentage: String,
    @field:SerializedName("item_type") val itemType: String,
    @field:SerializedName("status") val status: String,
    @field:SerializedName("timing") val timing: String
)