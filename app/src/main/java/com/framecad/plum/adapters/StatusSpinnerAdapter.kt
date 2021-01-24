package com.framecad.plum.adapters

import android.database.DataSetObserver
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.SpinnerAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.framecad.plum.R
import com.framecad.plum.databinding.LayoutStatusSpinnerBinding
import com.framecad.plum.databinding.LayoutStatusSpinnerDropdownBinding

class StatusSpinnerAdapter(private val statusList: List<String>) : SpinnerAdapter {


    override fun registerDataSetObserver(observer: DataSetObserver?) {
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
    }

    override fun getCount(): Int {
        return statusList.size
    }

    override fun getItem(position: Int): Any {
        return statusList[position]
    }

    override fun getItemId(position: Int): Long {
        return statusList[position].hashCode().toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val spinView: View?
        if (convertView == null) {
            val binding = LayoutStatusSpinnerBinding.inflate(LayoutInflater.from(parent?.context))
            binding.status = statusList[position]
            binding.statusIconSelector = ProjectStatus()
            spinView = binding.root
        } else {
            spinView = convertView
        }
        return spinView
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun isEmpty(): Boolean {
        return statusList.isNullOrEmpty()
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val spinView: View?
        parent?.apply{
            val params = layoutParams
            params.height = RecyclerView.LayoutParams.WRAP_CONTENT
            layoutParams = params
        }

        if (convertView == null) {
            val binding = LayoutStatusSpinnerDropdownBinding.inflate(LayoutInflater.from(parent?.context))
            binding.status = statusList[position]
            binding.statusIconSelector = ProjectStatus()
            spinView = binding.root
            val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 200)
            layoutParams.gravity = Gravity.CENTER
            spinView.layoutParams = layoutParams
        } else {
            val binding = DataBindingUtil.findBinding<LayoutStatusSpinnerDropdownBinding>(convertView)
            binding?.status = statusList[position]
            binding?.statusIconSelector = ProjectStatus()
            spinView = convertView
        }
        return spinView
    }

}