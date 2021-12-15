package com.example.simpletodo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/*
* A bridge that tells the recyclerView how to display the data we give it
*/
class TaskItemAdapter(val listOfItems: List<String>, val longClickListener: OnLongClickListener, val clickListener: OnClickListener) : RecyclerView.Adapter<TaskItemAdapter.ViewHolder>(){

    interface OnClickListener {
        fun onItemClicked(position: Int)
        fun onDeleteClicked(position: Int)
        fun onEditClicked(position: Int)
    }
    interface OnLongClickListener {
        fun onItemLongClicked(position: Int)
    }

    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.task_item, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    //Involves populating data into the item through holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val item = listOfItems.get(position)

        holder.textView.text = item
    }

    override fun getItemCount(): Int {
        return listOfItems.size
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // store references to elements in our layout view
        val textView: TextView
        val editButton: ImageButton
        val deleteButton: ImageButton

        init {
            textView = itemView.findViewById(R.id.tv_task)

            editButton = itemView.findViewById(R.id.bt_edit)
            deleteButton = itemView.findViewById(R.id.bt_delete)

            itemView.setOnClickListener {
                clickListener.onItemClicked(adapterPosition)
            }

            editButton.setOnClickListener {
                clickListener.onEditClicked(adapterPosition)
            }
            deleteButton.setOnClickListener {
                clickListener.onDeleteClicked(adapterPosition)
            }

            itemView.setOnLongClickListener {
                longClickListener.onItemLongClicked(adapterPosition)
                true
            }
        }

    }
}