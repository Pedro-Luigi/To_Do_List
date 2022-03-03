package com.meu.todolist.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.meu.todolist.R
import com.meu.todolist.databinding.ItemTaskBinding
import com.meu.todolist.model.Task

class TaskListAdapter:ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallback()) {

    var listenerEdit : (Task) -> Unit = {}
    var listenerDelete : (Task) -> Unit = {}

    inner class TaskViewHolder (private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Task){
            binding.tarefa.setOnClickListener {
                listenerEdit(item)
            }
            binding.tvTitleTarefa.text = item.title
            binding.date.text = "${item.date} ${item.hour}"
            binding.btOptions.setOnClickListener {
                showPopup(item)
            }

        }

        private fun showPopup(item: Task) {
            val btOptions = binding.btOptions
            val popupMenu = PopupMenu(btOptions.context, btOptions)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.action_edit -> listenerEdit(item)
                    R.id.action_delete -> listenerDelete(item)
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
class DiffCallback : DiffUtil.ItemCallback<Task>(){
    override fun areItemsTheSame(oldItem: Task, newItem: Task)= oldItem == newItem
    override fun areContentsTheSame(oldItem: Task, newItem: Task)= oldItem.id == newItem.id
}