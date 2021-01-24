package com.framecad.plum.data.model

import android.os.Parcelable
import com.framecad.plum.data.response.SubListItemResponse
import kotlinx.android.parcel.Parcelize

/**
 * This class represents the listItem in a sublist which contain a list of properties
 * e.g. Every stick in a Panel Detail Page
 */
@Parcelize
class SubListItem(
    private val subItemResponse: SubListItemResponse,
    private var propertyItems: ArrayList<PropertyItem> = ArrayList()
) : ListItem, Parcelable {

    fun getName(): String {
        return subItemResponse.name
    }


    fun getStatus(): String {
        return subItemResponse.status
    }


    fun getId(): String {
        return subItemResponse.id
    }

    fun getUrl(): String? {
        return subItemResponse.url
    }


    fun getProperties(): List<PropertyItem> {
        if (propertyItems.isEmpty()) {
            val items = ArrayList<PropertyItem>()
            subItemResponse.properties?.forEach {
                items.add(PropertyItem(it))
            }
            propertyItems.addAll(items)
        }
        return propertyItems
    }


    override fun getListItemType(): ListItem.ViewHolderType {
        return ListItem.ViewHolderType.getEnumByString(subItemResponse.itemType)
    }

    override fun getListItemName(): String {
        return subItemResponse.name
    }
}
