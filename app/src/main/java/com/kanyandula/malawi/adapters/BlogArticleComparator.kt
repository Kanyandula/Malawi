package com.kanyandula.malawi.adapters

import androidx.recyclerview.widget.DiffUtil
import com.kanyandula.malawi.data.model.Blog

class BlogArticleComparator : DiffUtil.ItemCallback<Blog>() {

    override fun areItemsTheSame(oldItem: Blog, newItem: Blog) =
        oldItem.image == newItem.image

    override fun areContentsTheSame(oldItem: Blog, newItem: Blog) =
        oldItem == newItem
}