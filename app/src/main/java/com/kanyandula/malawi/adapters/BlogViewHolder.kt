package com.kanyandula.malawi.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kanyandula.malawi.R
import com.kanyandula.malawi.data.model.Blog
import com.kanyandula.malawi.databinding.BlogListItemBinding

class BlogViewHolder(
    private val binding: BlogListItemBinding,
    private val onItemClick: (Int) -> Unit,
    private val onBookmarkClick: (Int) -> Unit,
): RecyclerView.ViewHolder(binding.root) {


    fun bind(blog: Blog) {
        binding.apply {
            Glide.with(itemView).load(blog.image)
                .error(R.drawable.image_placeholder)
                .into(blogImage)
            blogTitle.text = blog.title ?: ""
            blogAuthor.text = blog.userName?: ""
            blogUpdateDate.text = blog.date?: ""

            imageViewBookmark.setImageResource(
                when {
                    blog.favorite
                    -> R.drawable.ic_bookmark_selected
                    else -> R.drawable.ic_bookmark_unselected

                }
            )
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
            imageViewBookmark.setOnClickListener {

                val position = bindingAdapterPosition

                if (position != RecyclerView.NO_POSITION) {
                    onBookmarkClick(position)


            }





        }
    }
}
}