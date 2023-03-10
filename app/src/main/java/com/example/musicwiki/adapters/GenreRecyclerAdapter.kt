package com.example.musicwiki.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.musicwiki.api.TagsListResponse
import com.example.musicwiki.R
import com.example.musicwiki.activities.TagDetailsActivity


class GenreRecyclerAdapter(var data: MutableList<TagsListResponse>, private val c: Activity) :
    RecyclerView.Adapter<GenreRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val v: View = inflater.inflate(R.layout.genre_item_layout, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = data[p1]
        p0.itemBtn.text = item.name

        p0.itemBtn.setOnClickListener {
            val intent = Intent(c, TagDetailsActivity::class.java)
            intent.putExtra("tagName", item.name)
            c.startActivity(intent)
        }
    }

    fun updateDataset(dataList: MutableList<TagsListResponse>) {
        data = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemBtn: Button = itemView.findViewById(R.id.itemButton)
    }
}
