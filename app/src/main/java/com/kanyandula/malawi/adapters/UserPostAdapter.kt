package com.kanyandula.malawi.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.kanyandula.malawi.R
import com.kanyandula.malawi.data.Blog
import com.kanyandula.malawi.ui.OnItemClickAction
import kotlinx.android.synthetic.main.blog_list_item.view.*


class UserPostAdapter(
    private val context: Context,
    private var blog: List<Blog>,
    private val onItemClickAction: OnItemClickAction
) :
    RecyclerView.Adapter<MoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.blog_list_item, parent, false)
        return MoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.title.text = blog[position].title


        if (blog[position].userName.isNullOrEmpty()) {
            holder.userName.visibility = View.GONE
        } else {
            holder.userName.text = blog[position].userName
        }

        if (blog[position].date.isNullOrEmpty()) {
            holder.date.text = blog[position].date
        } else {
            holder.date.text = blog[position].date
        }



        holder.item.setOnLongClickListener() {
            val id = blog[position].userName
            onItemClickAction.onItemLongClicked(id)
            true
        }
    }

    override fun getItemCount() = blog.size

    fun dataSetChanged(newDataSet: List<Blog>) {
        blog = newDataSet
        notifyDataSetChanged()
    }

}

class MoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val item = view.findViewById<ConstraintLayout>(R.id.blog_item)

    val title = view.findViewById<TextView>(R.id.blog_title)
    val userName = view.findViewById<TextView>(R.id.blog_author)
    val date = view.findViewById<TextView>(R.id.blog_update_date)

}

