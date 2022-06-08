package com.example.a7minutesworkoutapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minutesworkoutapp.databinding.ItemExerciseStatusBinding

// in this adapter we are using our data models type and setting it equal to a variable
class ExerciseStatusAdapter(val items: ArrayList<ExerciseModel>)
    : RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // here we are inflating our view holder
        return ViewHolder(ItemExerciseStatusBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // the holder parameter is the data from our view holder class
        // in this case it will be tvItem variable which is set to our xml view for our recycle view
        val model: ExerciseModel = items[position] // here we are getting data from our exercise model list
        holder.tvItem.text = model.getId().toString() // here we are setting the text of our tv from our
        // xml recycler view file to the exercise model data

        when {
            model.getIsCompleted() ->{
               holder.tvItem.background =  ContextCompat.getDrawable(holder.itemView.context,
                    R.drawable.item_circular_orange_background)
            }
            model.getIsSelected() ->{
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context,
                    R.drawable.item_selected_circular_green_background)
            }
            else ->{

                holder.tvItem.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_circular_gray_background)
                holder.tvItem.setTextColor(Color.parseColor("#212121"))

            }
        }
    }

    override fun getItemCount(): Int {
        // here we are going to return amount of elements in our list
        return items.size
    }

    class ViewHolder(binding: ItemExerciseStatusBinding)
        : RecyclerView.ViewHolder(binding.root){
        // here we are creating a variable and setting it equal to
        // the view from our rv xml file
        val tvItem = binding.tvItem
    }

}