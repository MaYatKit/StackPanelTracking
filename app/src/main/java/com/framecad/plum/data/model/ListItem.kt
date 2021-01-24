package com.framecad.plum.data.model

/**
 * Generic type for list item
 */
interface ListItem {
    fun getListItemType(): ViewHolderType
    fun getListItemName(): String

    /**
     * Enumeration of different data type
     */
    public enum class ViewHolderType(s: String) {
        // Below shown are data types of the list in Project Detail Page and Item Detail Page
        TEXT_TYPE("text"),
        TIMING_TYPE("timing"),
        STATUS_TYPE("status"),
        PERCENTAGE_TYPE("percentage"),
        PANELS_TYPE("panels"),
        STACKS_TYPE("stacks"),
        STICKS_TYPE("sticks"),
        PLANS_TYPE("plans"),

        // Below shown are data types of sublist
        SUBLIST_PLAN_TYPE("plan"),
        SUBLIST_STACK_TYPE("stack"),
        SUBLIST_PANEL_TYPE("panel"),
        SUBLIST_STICK_TYPE("stick"),

        UNKNOWN_TYPE("unknown");

        companion object EnumSelector {
            public fun getEnumByString(s: String?): ViewHolderType {
                return when (s) {
                    "text" -> TEXT_TYPE
                    "timing" -> TIMING_TYPE
                    "status" -> STATUS_TYPE
                    "percentage" -> PERCENTAGE_TYPE
                    "panels" -> PANELS_TYPE
                    "stacks" -> STACKS_TYPE
                    "sticks" -> STICKS_TYPE
                    "plans" -> PLANS_TYPE
                    "plan" -> SUBLIST_PLAN_TYPE
                    "stack" -> SUBLIST_STACK_TYPE
                    "panel" -> SUBLIST_PANEL_TYPE
                    "stick" -> SUBLIST_STICK_TYPE
                    null -> SUBLIST_PLAN_TYPE
                    else -> UNKNOWN_TYPE
                }
            }
        }
    }


}