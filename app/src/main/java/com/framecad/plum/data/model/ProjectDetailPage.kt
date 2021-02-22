package com.framecad.plum.data.model

import android.os.Parcelable
import android.util.Log
import com.framecad.plum.data.response.ProjectDetailResponse
import kotlinx.android.parcel.Parcelize


/**
 * A class represent the data in the Project Detail Page
 */
@Parcelize
class ProjectDetailPage(
    val response: ProjectDetailResponse,
    private val propertyItems: ArrayList<PropertyItem> = ArrayList(),
    private val subLists: ArrayList<SubList> = ArrayList()
) : Parcelable {


    fun getName(): String {
        return response.name
    }


    fun getProperties(): List<PropertyItem> {
        propertyItems.isEmpty().let {
            response.properties.forEach { pr ->
                propertyItems.add(PropertyItem(pr))
            }
        }
        return propertyItems
    }





    /**
     * Return a list of Sublist,
     * e.g., a list contains panels and sticks
     */
    fun getSubLists(): List<SubList> {
        if (subLists.isEmpty()){
            var subListItems = ArrayList<SubListItem>()
            response.panels.forEach { panel ->
                subListItems.add(SubListItem(panel))
            }
            subLists.add(SubList(false, "Panels", ListItem.ViewHolderType.PANELS_TYPE, subListItems))

            subListItems = ArrayList<SubListItem>()
            response.stacks.forEach { stack ->
                subListItems.add(SubListItem(stack))
            }
            subLists.add(SubList(false, "Stacks", ListItem.ViewHolderType.STACKS_TYPE, subListItems))

            subListItems = ArrayList<SubListItem>()
            response.plans.forEach { plan ->
                subListItems.add(SubListItem(plan))
            }
            subLists.add(SubList(false, "Plans", ListItem.ViewHolderType.PLANS_TYPE, subListItems))
        }
        return subLists
    }


}
