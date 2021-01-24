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
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.framecad.plum.R
import com.framecad.plum.data.model.ListItem
import com.framecad.plum.data.model.ScanItem
import com.framecad.plum.data.model.SubListItem
import com.framecad.plum.data.model.SvgItem
import com.framecad.plum.databinding.ListCommonItemSublistItemBinding
import com.framecad.plum.databinding.ListCommonItemSublistItemPlanBinding
import com.framecad.plum.view.drawing.SVGDrawingActivity
import com.framecad.plum.view.projectdetail.ProjectDetailActivity
import com.framecad.plum.view.viewdetail.ViewDetailActivity

/**
 * Adapter for the [RecyclerView] in [ProjectDetailActivity].
 */
private const val PLAN_ITEM = 0
private const val PANEL_STACK_STICK_ITEM = 1

class SubListAdapter : ListAdapter<ListItem, SubListAdapter.BaseViewHolder>(ProjectDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            PLAN_ITEM -> PlanSublistViewHolder(
                ListCommonItemSublistItemPlanBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            PANEL_STACK_STICK_ITEM -> PanelOrStackSublistViewHolder(
                ListCommonItemSublistItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> PlanSublistViewHolder(
                ListCommonItemSublistItemPlanBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    override fun getItemViewType(position: Int): Int {
        return when (currentList[position].getListItemType()) {
            ListItem.ViewHolderType.SUBLIST_PLAN_TYPE -> PLAN_ITEM
            ListItem.ViewHolderType.SUBLIST_STACK_TYPE -> PANEL_STACK_STICK_ITEM
            ListItem.ViewHolderType.SUBLIST_PANEL_TYPE -> PANEL_STACK_STICK_ITEM
            ListItem.ViewHolderType.SUBLIST_STICK_TYPE -> PANEL_STACK_STICK_ITEM
            else -> PLAN_ITEM
        }
    }

    open class BaseViewHolder(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        open fun bind(item: ListItem) {}
    }

    class PanelOrStackSublistViewHolder(
        private val binding: ListCommonItemSublistItemBinding
    ) : SubListAdapter.BaseViewHolder(binding) {
        init {
            binding.setOnClickListener { view ->
                binding.subItem?.let {
                    if (it.getListItemType() == ListItem.ViewHolderType.SUBLIST_STACK_TYPE || it.getListItemType() == ListItem.ViewHolderType.SUBLIST_PANEL_TYPE) {
                        val intent = Intent(view.context, ViewDetailActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        val scanItem = ScanItem(
                            it.getId(),
                            it.getName(),
                            if (it.getListItemType() == ListItem.ViewHolderType.SUBLIST_STACK_TYPE) ScanItem.ScanItemType.STACK else ScanItem.ScanItemType.PANEL
                        )
                        intent.putExtra(
                            binding.root.context.getString(R.string.view_detail_page_item),
                            scanItem
                        )
                        view.context.startActivity(intent)
                    }
                }

            }

        }

        override fun bind(item: ListItem) {
            binding.apply {
                subItem = item as SubListItem
                properties = item.getProperties()
                projectStatus = ProjectStatus()
                executePendingBindings()
            }
        }
    }

    class PlanSublistViewHolder(
        private val binding: ListCommonItemSublistItemPlanBinding
    ) : SubListAdapter.BaseViewHolder(binding) {

        override fun bind(item: ListItem) {
            binding.apply {
                plan = item as SubListItem
                setOnClickListener {
                    val intent = Intent(binding.root.context, SVGDrawingActivity::class.java)
                    intent.putExtra(
                        binding.root.context.getString(R.string.svg_drawing_page_item), SvgItem(
                            item.getId(), item.getName(),
                            SvgItem.SvgItemType.PLAN
                        )
                    )
                    binding.root.context.startActivity(intent)
                }
                executePendingBindings()
            }
        }
    }

    private class ProjectDiffCallback : DiffUtil.ItemCallback<ListItem>() {

        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem.getListItemName() == newItem.getListItemName()
        }

        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem.getListItemName() == newItem.getListItemName()
        }
    }
}



