package com.framecad.plum.data.model

import android.os.Parcelable
import com.framecad.plum.data.response.DetailResponse
import com.framecad.plum.data.response.PanelDetailResponse
import com.framecad.plum.data.response.StackDetailResponse
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


/**
 * A class represent the data in the Item Detail Page
 */
@Parcelize
class ItemDetailPage(
    val response: @RawValue DetailResponse,
    private val propertyItems: ArrayList<PropertyItem> = ArrayList(),
    private val subListItems: ArrayList<SubListItem> = ArrayList()
) : Parcelable {

    fun getId(): String {
        return response.getDetailId()
    }

    fun getName(): String {
        return response.getDetailName()
    }

    fun getItemType(): ItemDetailPageType {
        return response.getDetailItemType()
    }


    fun getPlanUrl(): String {
        return response.getDetailPlanUrl()
    }


    fun getProperties(): List<PropertyItem> {
        propertyItems.isEmpty().let {
            response.getDetailProperties().forEach { pr ->
                propertyItems.add(PropertyItem(pr))
            }
        }
        return propertyItems
    }


    /**
     * Return a list of Sublist item,
     * each one is a sublist item,
     * e.g., panel of the panels Sublist, stick of the sticks Sublist
     */
    private fun getSubListItems(): List<SubListItem>? {
        subListItems.isEmpty().let {
            when (getItemType()) {
                ItemDetailPageType.STACK -> {
                    (response as StackDetailResponse).panels.forEach { panel ->
                        subListItems.add(SubListItem(panel))
                    }
                }

                ItemDetailPageType.PANEL -> {
                    (response as PanelDetailResponse).sticks.forEach { stick ->
                        subListItems.add(SubListItem(stick))
                    }
                }
                ItemDetailPageType.UNKNOWN -> {
                    return null
                }
            }
        }
        return subListItems
    }


    /**
     * Return a Sublist,
     * e.g., panels, sticks
     */
    fun getSubList(): SubList? {
        getSubListItems()?.let {
            return when (getItemType()) {
                ItemDetailPageType.STACK -> {
                    SubList(true, "Panels", ListItem.ViewHolderType.PANELS_TYPE , it)
                }

                ItemDetailPageType.PANEL -> {
                    SubList(true, "Sticks", ListItem.ViewHolderType.STICKS_TYPE, it)

                }
                ItemDetailPageType.UNKNOWN -> {
                    null
                }
            }
        }
        return null
    }


    /**
     * Currently the item detail page is Stack or Panel
     */
    @Parcelize
    enum class ItemDetailPageType: Parcelable {
        @SerializedName("panel")
        PANEL,
        @SerializedName("stack")
        STACK,
        UNKNOWN
    }

}
