package com.framecad.plum.data.response

import com.google.gson.annotations.SerializedName

data class SearchProjectsResponse(
    @field:SerializedName("name") val name: String,
    @field:SerializedName("projects") val projects: List<SimpleProjectResponse>,
)