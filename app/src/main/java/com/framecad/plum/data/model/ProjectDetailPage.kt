package com.framecad.plum.data.model

import android.os.Parcelable
import com.framecad.plum.data.response.ProjectDetailResponse
import kotlinx.android.parcel.Parcelize


/**
 * A class represent the data in the Project Detail Page
 */
@Parcelize
class ProjectDetailPage(
    val response: ProjectDetailResponse,
    private val propertyItems: ArrayList<PropertyItem> = ArrayList(),
    private val subListItems: ArrayList<SubListItem> = ArrayList(),
    private val sublists: ArrayList<SubList> = ArrayList()
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
     * Return a list of Sublist item,
     * each one is a sublist item,
     * e.g., panel of the panels Sublist, stick of the sticks Sublist
     */
    fun getSubListItems(): List<SubListItem> {
        if(subListItems.isEmpty()){
            response.panels.forEach { panel ->
                subListItems.add(SubListItem(panel))
            }
            response.stacks.forEach { stack ->
                subListItems.add(SubListItem(stack))
            }
        }
        return subListItems
    }


    /**
     * Return a list of Sublist,
     * e.g., a list contains panels and sticks
     */
    fun getSubLists(): List<SubList> {
        if (sublists.isEmpty()){
            var subListItems = ArrayList<SubListItem>()
            response.panels.forEach { panel ->
                subListItems.add(SubListItem(panel))
            }
            sublists.add(SubList(false, "Panels", ListItem.ViewHolderType.PANELS_TYPE, subListItems))

            subListItems = ArrayList<SubListItem>()
            response.stacks.forEach { stack ->
                subListItems.add(SubListItem(stack))
            }
            sublists.add(SubList(false, "Stacks", ListItem.ViewHolderType.STACKS_TYPE, subListItems))

            subListItems = ArrayList<SubListItem>()
            response.plans.forEach { plan ->
                subListItems.add(SubListItem(plan))
            }
            sublists.add(SubList(false, "Plans", ListItem.ViewHolderType.PLANS_TYPE, subListItems))
        }
        return sublists
    }


}
