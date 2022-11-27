package com.ather.mytimer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ather.mytimer.databinding.ItemTasksBinding

class TaskAdapter(val context: Context,
                  var list: List<TaskModal>,
                  val mCallback: (position:Int) -> Unit):
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
        R.layout.item_tasks, parent, false) as ItemTasksBinding
        return ViewHolder(binding.root, binding, viewType, context, list)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvCount.text = (position + 1).toString()
        holder.binding.item = list.get(position)
    }

    override fun getItemCount(): Int {
     return list.count()
    }

    inner class ViewHolder(
        root: View,
       val binding: ItemTasksBinding,
       val viewType: Int,
        context: Context,
        list: List<TaskModal>
    ): RecyclerView.ViewHolder(root) {}
}