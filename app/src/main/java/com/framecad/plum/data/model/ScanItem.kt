package com.framecad.plum.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * This type is the object information after scanning a qr code
 */
@Parcelize
data class ScanItem(
    val id: String,
    val name: String,
    val itemType: ScanItemType
) : Parcelable {


    /**
     * Enumeration of Scan Item Type
     */
    @Parcelize
    enum class ScanItemType(val s: String): Parcelable {
        @SerializedName("panel")
        PANEL("panel"),

        @SerializedName("stack")
        STACK("stack"),

        @SerializedName("plan")
        PLAN("plan"),
        UNKNOWN("unknown");


        @Parcelize
        companion object EnumSelector:  Parcelable {
            public fun getEnumByString(s: String): ScanItemType {
                return when (s) {
                    "panel" -> PANEL
                    "stack" -> STACK
                    "plan" -> PLAN
                    else -> UNKNOWN
                }
            }
        }

    }
}