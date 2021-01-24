package com.framecad.plum.data.model

import android.os.Parcelable
import com.framecad.plum.data.response.StackStatusResponse
import kotlinx.android.parcel.Parcelize

/**
 * Represent the data of the Stack Status Update Page
 */
@Parcelize
data class StackStatusPage(
    var stackStatusResponse: StackStatusResponse,
): StatusPage, Parcelable {
    override fun getType(): StatusPage.StatusPageType {
        return StatusPage.StatusPageType.getEnumByString(stackStatusResponse.itemType)
    }

    override fun getStatusId(): String {
        return stackStatusResponse.id
    }

    override fun getStatusText(): String {
        return stackStatusResponse.status
    }

    override fun getStatusNote(): String {
        return stackStatusResponse.note
    }

    override fun getStatusLongitude(): Double {
        return stackStatusResponse.longitude
    }

    override fun getStatusLatitude(): Double {
        return stackStatusResponse.latitude
    }

    override fun getStatusBoolean(): Boolean {
        return stackStatusResponse.stacked
    }

    override fun setType(type: StatusPage.StatusPageType) {
        stackStatusResponse.itemType = type.name
    }

    override fun setStatusId(id: String) {
        stackStatusResponse.id = id
    }

    override fun setStatusText(text: String) {
        stackStatusResponse.status = text
    }

    override fun setStatusBoolean(boolean: Boolean) {
        stackStatusResponse.stacked = boolean
    }

    override fun setStatusNote(note: String) {
        stackStatusResponse.note = note
    }

    override fun setStatusLongitude(longitude: Double) {
        stackStatusResponse.longitude = longitude
    }

    override fun setStatusLatitude(latitude: Double) {
        stackStatusResponse.latitude = latitude
    }
}