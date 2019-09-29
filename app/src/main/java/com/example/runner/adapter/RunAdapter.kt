package com.example.runner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.runner.R
import com.example.runner.data.model.Run
import kotlinx.android.synthetic.main.run_recyclerview_item.view.*
import java.text.DecimalFormat

class RunAdapter(
    private val context: Context,
    private var list: List<Run> = ArrayList()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val contactRow = layoutInflater.inflate(R.layout.run_recyclerview_item, parent, false)
        return RunViewHolder(contactRow)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       val distance = holder.itemView.distance_item_text_view
        val time = holder.itemView.time_item_text_view
        val averageSpeed = holder.itemView.average_speed_item_text_view
        val maxSpeed = holder.itemView.max_speed_item_text_view
        val date = holder.itemView.date_item_text_view

        val df = DecimalFormat("0.0")

        val dist: Double = list[position].distance.toString().toDouble()
        val aveSpeed: Double = list[position].averageSpeed.toString().toDouble()
        val mSpeed: Double = list[position].maxSpeed.toString().toDouble()

        distance.text = df.format(dist).plus(" m")
        time.text = list[position].time
        averageSpeed.text = df.format(aveSpeed).plus(" km/h")
        maxSpeed.text = df.format(mSpeed).plus(" km/h")
        date.text = list[position].date
    }

    fun updateList(list: ArrayList<Run>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder) {
        notifyItemRemoved(viewHolder.adapterPosition)
    }

    fun getRunPosition(position: Int): Run {
        return list[position]
    }
}

 class RunViewHolder(contactRow: View) : RecyclerView.ViewHolder(contactRow) {

}
