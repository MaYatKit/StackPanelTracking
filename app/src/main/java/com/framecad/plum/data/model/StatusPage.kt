package com.framecad.plum.data.model


/**
 * Generic type for the Status Update Page
 */
interface StatusPage {
    fun getType(): StatusPageType
    fun getStatusId(): String
    fun getStatusText(): String
    fun getStatusBoolean(): Boolean
    fun getStatusNote(): String
    fun getStatusLongitude(): Double
    fun getStatusLatitude(): Double

    fun setType(type: StatusPageType)
    fun setStatusId(id: String)
    fun setStatusText(text: String)
    fun setStatusBoolean(boolean: Boolean)
    fun setStatusNote(note: String)
    fun setStatusLongitude(longitude: Double)
    fun setStatusLatitude(latitude: Double)


    /**
     * Enumeration of Status Page Type
     */
    enum class StatusPageType(val s: String) {
        PANEL("panel"),
        STACK("stack"),
        UNKNOWN("unknown");


        companion object EnumSelector {
            public fun getEnumByString(s: String): StatusPageType {
                return when (s) {
                    "panel" -> PANEL
                    "stack" -> STACK
                    else -> UNKNOWN
                }
            }
        }

    }


}