package com.framecad.plum.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * This type is the object for svg item: Panel or Plan
 */
@Parcelize
data class SvgItem(
    val id: String,
    val name: String,
    val itemType: SvgItemType
) : Parcelable {


    /**
     * Enumeration of Scan Item Type
     */
    @Parcelize
    enum class SvgItemType(val s: String): Parcelable {
        PANEL("panel"),
        PLAN("plan"),
        UNKNOWN("unknown");


        @Parcelize
        companion object EnumSelector:  Parcelable {
            public fun getEnumByString(s: String): SvgItemType {
                return when (s) {
                    "panel" -> PANEL
                    "plan" -> PLAN
                    else -> UNKNOWN
                }
            }
        }

    }
}