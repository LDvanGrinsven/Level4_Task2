package com.example.rps4.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.history_item.view.*
import java.util.*
import com.example.rps4.R
import com.example.rps4.database.RPS4Repository
import com.example.rps4.model.RPS4

class ProductAdapter(private val Games: List<RPS4>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(Games[position])
    }

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called simple_list_item_1.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        )
    }

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return Games.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(game: RPS4) {
            MainActivity.changeImage(game.computerPick,itemView.ivComputer)
            MainActivity.changeImage(game.computerPick,itemView.ivYou)
            MainActivity.changeStatus(game.status,itemView.tvStatus,itemView.context)
            itemView.tvDate.text =  Date(game.date).toString()

        }
    }
}