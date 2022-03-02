package com.kanyandula.malawi.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kanyandula.malawi.R
import com.kanyandula.malawi.data.Blog
import com.kanyandula.malawi.ui.OnItemClickAction

import kotlinx.android.synthetic.main.blog_list_item.view.*
import java.lang.ref.WeakReference


class BlogAdapter(

) : RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {


    inner class BlogViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Blog>() {
        override fun areItemsTheSame(oldItem: Blog, newItem: Blog): Boolean {
            //image url is also unique to each article just like the id
            return oldItem.image == newItem.image
        }

        override fun areContentsTheSame(oldItem: Blog, newItem: Blog): Boolean {
            return  oldItem.hashCode() == newItem.hashCode()
        }

    }

    val differ = AsyncListDiffer(this, differCallback)
    var blogs: List<Blog>
            get() = differ.currentList
            set(value) =  differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        return BlogViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.blog_list_item,
                parent,
                false
            )

        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun dataSetChanged(newDataSet: List<Blog>) {
        blogs = newDataSet
        notifyDataSetChanged()
    }

    private var onItemClickListener: ((Blog) -> Unit)? = null



    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val blog = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(blog.image).into(blog_image)
            blog_title.text = blog.title
            blog_author.text = blog.userName
            blog_update_date.text = blog.date

            setOnClickListener{
                onItemClickListener?.let { it(blog) }
            }
        }
    }




    fun setOnItemClickListener( listener: (Blog) -> Unit){
        onItemClickListener = listener
    }


}