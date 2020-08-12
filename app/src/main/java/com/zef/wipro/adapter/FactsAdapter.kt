package com.zef.wipro.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import  com.zef.wipro.adapter.FactsAdapter.FactsViewHolder
import com.bumptech.glide.Glide
import com.zef.wipro.R
import com.zef.wipro.data.Rows
import java.util.ArrayList

class FactsAdapter : RecyclerView.Adapter<FactsViewHolder>() {
    var dataList: List<Rows>

    init {
        dataList = ArrayList()
    }

    fun updateData(list: List<Rows>) {
        dataList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.facts_items, null)
        return FactsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FactsViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class FactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView
        var tvDescription: TextView
        var ivPoster: ImageView
        fun bind(row: Rows) {
            for (i in 0..dataList.size) {

                if (!TextUtils.isEmpty(row.title)) {
                    tvTitle.text = row.title
                }

                if (!TextUtils.isEmpty(row.description)) {
                    tvDescription.text = row.description
                }

                Glide.with(itemView.context).load(row.imageHref)
                    .placeholder(R.drawable.ic_launcher_background).into(ivPoster)
            }
        }

        init {
            tvTitle = itemView.findViewById(R.id.tv_row_title)
            tvDescription = itemView.findViewById(R.id.tv_description)
            ivPoster = itemView.findViewById(R.id.imageView)
        }
    }
}