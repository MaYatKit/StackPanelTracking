package com.framecad.plum.data.model

import android.os.Parcelable
import com.framecad.plum.data.response.PropertyResponse
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PropertyItem(
    val property: PropertyResponse,
    val name: String = property.name,
    val value: String = (property.value?:""),
    val type: String = property.type
) : ListItem, Parcelable {

    override fun getListItemType(): ListItem.ViewHolderType {
        return ListItem.ViewHolderType.getEnumByString(property.type)
    }

    override fun getListItemName(): String {
        return property.name
    }
}