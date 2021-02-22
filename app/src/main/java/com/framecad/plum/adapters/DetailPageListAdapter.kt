package com.framecad.plum.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.framecad.plum.data.model.ListItem
import com.framecad.plum.data.model.SubList
import com.framecad.plum.data.model.ListItem.ViewHolderType.*
import com.framecad.plum.data.model.PropertyItem
import com.framecad.plum.databinding.ListCommonItemPercentageBinding
import com.framecad.plum.databinding.ListCommonItemStatusBinding
import com.framecad.plum.databinding.ListCommonItemSublistBinding
import com.framecad.plum.databinding.ListCommonItemTextBinding


private const val TEXT_ITEM = 0
private const val TIMING_ITEM = 1
private const val STATUS_ITEM = 2
private const val PERCENTAGE_ITEM = 3
private const val SUBLIST_ITEM = 4


enum class PageType {
    PROJECT_DETAIL_PAGE,
    ITEM_DETAIL_PAGE
}

/**
 * This adapter is for the list which in
 * the Project Detail Page or Item Detail Page
 */
class DetailPageListAdapter(val projectId: Long, val pageType: PageType) :
    ListAdapter<ListItem, DetailPageListAdapter.BaseViewHolder>(ProjectDetailDiffCallback()) {

    private val expandedRotateAnimation =
        RotateAnimation(0f, 90f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    private val unexpandedRotateAnimation =
        RotateAnimation(90f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)


    init {
        expandedRotateAnimation.duration = 500
        expandedRotateAnimation.fillAfter = true

        unexpandedRotateAnimation.duration = 100
        unexpandedRotateAnimation.fillAfter = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        return when (viewType) {
            TEXT_ITEM -> ProjectDetailTextViewHolder(
                ListCommonItemTextBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            TIMING_ITEM -> ProjectDetailTextViewHolder(
                ListCommonItemTextBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            STATUS_ITEM -> ProjectDetailStatusViewHolder(
                ListCommonItemStatusBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            PERCENTAGE_ITEM -> ProjectDetailPercentageViewHolder(
                ListCommonItemPercentageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            SUBLIST_ITEM -> ProjectDetailSublistViewHolder(
                ListCommonItemSublistBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> ProjectDetailTextViewHolder(
                ListCommonItemTextBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (currentList[position].getListItemType()) {
            TEXT_TYPE -> TEXT_ITEM
            TIMING_TYPE -> TIMING_ITEM
            STATUS_TYPE -> STATUS_ITEM
            PERCENTAGE_TYPE -> PERCENTAGE_ITEM
            PANELS_TYPE -> SUBLIST_ITEM
            STACKS_TYPE -> SUBLIST_ITEM
            STICKS_TYPE -> SUBLIST_ITEM
            PLANS_TYPE -> SUBLIST_ITEM
            else -> TEXT_ITEM
        }
    }


    open class BaseViewHolder(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        open fun bind(item: ListItem) {}
    }


    class ProjectDetailTextViewHolder(
        private val binding: ListCommonItemTextBinding
    ) : BaseViewHolder(binding) {

        init {
            binding.root.setOnClickListener {
                binding.property?.let { property ->
                }
            }
        }

        override fun bind(item: ListItem) {
            binding.apply {
                property = item as PropertyItem
                executePendingBindings()
            }
        }
    }


    inner class ProjectDetailSublistViewHolder(
        val binding: ListCommonItemSublistBinding
    ) : BaseViewHolder(binding) {

        init {
            // Use same adapter to reused list data
            binding.subList.adapter = SubListAdapter(projectId)

            if (pageType == PageType.ITEM_DETAIL_PAGE) {
                binding.subListItemArrow.visibility = View.INVISIBLE
            }
        }

        override fun bind(item: ListItem) {
            binding.apply {
                subListData = item as SubList
                (binding.subList.adapter as SubListAdapter).submitList(item.subItems)
                executePendingBindings()
            }
        }
    }

    class ProjectDetailPercentageViewHolder(
        private val binding: ListCommonItemPercentageBinding
    ) : BaseViewHolder(binding) {
        init {
            binding.root.setOnClickListener {
                binding.property?.let { property ->
                    // TODO: 11/11/20 Jump to project detail
                }
            }
        }

        override fun bind(item: ListItem) {
            binding.apply {
                property = item as PropertyItem
                executePendingBindings()
            }
        }
    }

    class ProjectDetailStatusViewHolder(
        private val binding: ListCommonItemStatusBinding
    ) : BaseViewHolder(binding) {
        init {
            binding.root.setOnClickListener {
                binding.property?.let { property ->
                    // TODO: 11/11/20 Jump to project detail
                }
            }

        }

        override fun bind(item: ListItem) {
            binding.apply {
                property = item as PropertyItem
                projectStatus = ProjectStatus()
                executePendingBindings()
            }
        }
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        Log.d("onBindViewHolder", "position = $position")
        holder.bind(currentList[position])
        if (holder is ProjectDetailSublistViewHolder && pageType == PageType.PROJECT_DETAIL_PAGE) {
            holder.binding.root.setOnClickListener {
                holder.binding.subListData?.let {
                    val isExpanded = it.expanded
                    it.expanded = !isExpanded
                    notifyItemChanged(position)
                }
            }
        }
    }
}

private class ProjectDetailDiffCallback : DiffUtil.ItemCallback<ListItem>() {

    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem.getListItemName() == newItem.getListItemName() && oldItem.getListItemType() == newItem.getListItemType()
    }

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem.getListItemName() == newItem.getListItemName() && oldItem.getListItemType() == newItem.getListItemType()
    }
}

