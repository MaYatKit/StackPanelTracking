package com.framecad.plum.data.response

import com.framecad.plum.data.model.ScanItem
import com.google.gson.annotations.SerializedName

data class ValidateItemResponse(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("item_type")
    val itemType: ScanItem.ScanItemType,
)