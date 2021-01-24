/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.framecad.plum.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.framecad.plum.R
import com.framecad.plum.data.response.SimpleProjectResponse
import com.framecad.plum.databinding.ListItemProjectsBinding
import com.framecad.plum.view.home.HomeActivity
import com.framecad.plum.view.login.LoginActivity.Companion.sRequestCode
import com.framecad.plum.view.projectdetail.ProjectDetailActivity

/**
 * Adapter for the [RecyclerView] in [ProjectsFragment].
 */
class ProjectsListAdapter(private val activity: HomeActivity) :
    ListAdapter<SimpleProjectResponse, RecyclerView.ViewHolder>(ProjectDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProjectViewHolder(
            ListItemProjectsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val project = getItem(position)
        (holder as ProjectViewHolder).bind(project)
    }

    inner class ProjectViewHolder(
        val binding: ListItemProjectsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { view ->
                binding.project?.let {
                    val intent = Intent(view.context, ProjectDetailActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra(binding.root.context.getString(R.string.project_id), it.id.toLong())
                    activity.startActivityForResult(intent, sRequestCode)
                }
            }

        }


        fun bind(item: SimpleProjectResponse) {
            binding.apply {
                project = item
                projectStatus = ProjectStatus()
                executePendingBindings()
            }
        }
    }
}

private class ProjectDiffCallback : DiffUtil.ItemCallback<SimpleProjectResponse>() {

    override fun areItemsTheSame(
        oldItem: SimpleProjectResponse,
        newItem: SimpleProjectResponse
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: SimpleProjectResponse,
        newItem: SimpleProjectResponse
    ): Boolean {
        return oldItem == newItem
    }
}


class ProjectStatus {

    fun chooseIcon(status: String?): Int {
        return when (status) {
            "Awaiting Production" -> R.drawable.ic_projects_status_icon_awaiting_production
            "In Production" -> R.drawable.ic_projects_status_icon_in_production
            "Awaiting Rework" -> R.drawable.ic_projects_status_icon_awaiting_rework
            "Production Completed" -> R.drawable.ic_projects_status_icon_production_completed
            "Assembled" -> R.drawable.ic_projects_status_icon_assembled
            "In Inventory" -> R.drawable.ic_projects_status_icon_inventory
            "In Transit" -> R.drawable.ic_projects_status_icon_in_transit
            "On Site" -> R.drawable.ic_projects_status_icon_onsite
            "Installed" -> R.drawable.ic_projects_status_icon_installed
            else -> R.drawable.ic_projects_status_icon_awaiting_production
        }
    }
}
