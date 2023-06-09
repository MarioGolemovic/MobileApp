package com.example.marioapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.marioapp.R
import com.example.marioapp.data.entity.User

class UserAdapter(var list: List<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>(){

    private lateinit var dialog: Dialog

    fun setDialog(dialog: Dialog){
        this.dialog = dialog
    }

    interface Dialog {
        fun onClick(position: Int)
    }

  inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var playerName: TextView
        var playerPrice: TextView
        var playerNumber: TextView
        var playerYears: TextView
        var playerPosition: TextView
        init{
            playerName = view.findViewById(R.id.name)
            playerPrice = view.findViewById(R.id.price)
            playerNumber = view.findViewById(R.id.number)
            playerYears = view.findViewById(R.id.years)
            playerPosition = view.findViewById(R.id.position)
            view.setOnClickListener{
                dialog.onClick(layoutPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.row_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.playerName.text = list[position].playerName
        holder.playerPrice.text = list[position].playerPrice
        holder.playerNumber.text = list[position].playerNumber
        holder.playerYears.text = list[position].playerYears
        holder.playerPosition.text = list[position].playerPosition
    }


}