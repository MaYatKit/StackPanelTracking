package com.framecad.plum.data.model

import android.os.Parcelable
import com.framecad.plum.data.model.StatusPage.StatusPageType.EnumSelector.getEnumByString
import com.framecad.plum.data.response.PanelStatusResponse
import kotlinx.android.parcel.Parcelize


/**
 * Represent the data of the Panel Status Update Page
 */
@Parcelize
data class PanelStatusPage(
    var panelStatusResponse: PanelStatusResponse,
): StatusPage, Parcelable {
    override fun getType(): StatusPage.StatusPageType {
        return getEnumByString(panelStatusResponse.itemType)

    }

    override fun getStatusId(): String {
        return panelStatusResponse.id
    }

    override fun getStatusText(): String {
        return panelStatusResponse.status
    }

    override fun getStatusNote(): String {
        return panelStatusResponse.note
    }

    override fun getStatusLongitude(): Double {
        return panelStatusResponse.longitude
    }

    override fun getStatusLatitude(): Double {
        return panelStatusResponse.latitude
    }

    override fun getStatusBoolean(): Boolean {
        return panelStatusResponse.brokenOut
    }

    override fun setType(type: StatusPage.StatusPageType) {
        panelStatusResponse.itemType = type.name
    }

    override fun setStatusId(id: String) {
        panelStatusResponse.id = id
    }

    override fun setStatusText(text: String) {
        panelStatusResponse.status = text
    }

    override fun setStatusBoolean(boolean: Boolean) {
        panelStatusResponse.brokenOut = boolean
    }

    override fun setStatusNote(note: String) {
        panelStatusResponse.note = note
    }

    override fun setStatusLongitude(longitude: Double) {
        panelStatusResponse.longitude = longitude
    }

    override fun setStatusLatitude(latitude: Double) {
        panelStatusResponse.latitude = latitude
    }
}