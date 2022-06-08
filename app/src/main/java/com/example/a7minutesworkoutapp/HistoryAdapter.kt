package com.example.a7minutesworkoutapp


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minutesworkoutapp.databinding.ListHistoryRvBinding
import com.example.roomdemo.HistoryEntity

// this class doesn't implement on click listeners
// that's why we made the lambda functions and we gonna pass
// our click listener to it

class HistoryAdapter(private val items: ArrayList<String>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(binding: ListHistoryRvBinding): RecyclerView.ViewHolder(binding.root) {

        val llMain = binding.llMain
        var tvId = binding.tvId
        var tvDate = binding.tvDate


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( ListHistoryRvBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false) )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context =  holder.itemView.context
        val date = items[position]
        holder.tvId.text = (position + 1).toString()
        holder.tvDate.text = date

        if (position % 2 == 0) {

            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey))

        } else {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

}