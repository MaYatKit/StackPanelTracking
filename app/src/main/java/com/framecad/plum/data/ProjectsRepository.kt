package com.framecad.plum.data

import android.util.Range
import com.framecad.plum.data.api.ProjectsService
import com.framecad.plum.data.response.*
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectsRepository @Inject constructor(private val service: ProjectsService) {



    suspend fun getProjectList(): Response<SearchProjectsResponse> {
        return service.getProjectsByUserId()
    }

    suspend fun getProjectDetail(id: Long): Response<ProjectDetailResponse> {
        return service.getProjectById(id)
    }

    suspend fun getStackDetail(id: Long): Response<StackDetailResponse> {
        return service.getStackById(id)
    }

    suspend fun getPanelDetail(id: Long): Response<PanelDetailResponse> {
        return service.getPanelById(id)
    }

    suspend fun getStackStatus(id: Long): Response<StackStatusResponse> {
        return service.getStackStatusById(id)
    }

    suspend fun getPanelStatus(id: Long): Response<PanelStatusResponse> {
        return service.getPanelStatusById(id)
    }

    suspend fun setPanelStatus(panel: PanelStatusResponse) = service.setPanelStatus(panel)

    suspend fun setStackStatus(stack: StackStatusResponse) = service.setStackStatus(stack)

    suspend fun getPanelDrawing(id: Long) = service.getPanelDrawingById(id)

    suspend fun getStackDrawing(id: Long) = service.getPanelDrawingById(id)

    suspend fun getPlanDrawing(id: Long) = service.getPlanDrawingById(id)

    suspend fun getItemByValidatingCode(encodedCode: String) = service.getItemByValidatingCode(encodedCode)


}