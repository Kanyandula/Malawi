package com.kanyandula.malawi.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kanyandula.malawi.data.Blog
import com.kanyandula.malawi.databinding.BlogListItemBinding

class BlogViewHolder(
    private val binding: BlogListItemBinding,
    private val onItemClick: (Int) -> Unit,
): RecyclerView.ViewHolder(binding.root) {

    fun bind(blog: Blog) {
        binding.apply {
            Glide.with(itemView).load(blog.image).into(blogImage)
            blogTitle.text = blog.title
            blogAuthor.text = blog.userName
            blogUpdateDate.text = blog.date
        }
    }

    init {
        binding.apply {
            root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(position)
                }
            }
        }
    }

}