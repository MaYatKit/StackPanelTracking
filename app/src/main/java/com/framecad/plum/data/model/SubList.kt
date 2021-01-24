package com.framecad.plum.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * This type is for a listItem which contains sub items
 * e.g. The "Sticks" listItem in the Panel Detail Page
 */
@Parcelize
class SubList(
    var expanded: Boolean = true,
    val name: String,
    val itemType: ListItem.ViewHolderType,
    val subItems: List<SubListItem>
) : ListItem, Parcelable {
    override fun getListItemType(): ListItem.ViewHolderType {
        return itemType
    }
    override fun getListItemName(): String {
        return name
    }
}