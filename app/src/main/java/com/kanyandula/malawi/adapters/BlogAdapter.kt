package com.kanyandula.malawi.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kanyandula.malawi.data.model.Blog
import com.kanyandula.malawi.databinding.BlogListItemBinding


class BlogAdapter(
    private val onItemClick: (Blog) -> Unit,
    private val onBookmarkClick: (Blog) -> Unit,
): ListAdapter<Blog, BlogViewHolder>(BlogArticleComparator()) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val binding =
            BlogListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BlogViewHolder(binding,
            onItemClick = { position ->
                val article = getItem(position)
                if (article != null) {
                    onItemClick(article)
                }
            },
            onBookmarkClick = { position ->
                val article = getItem(position)
                if (article != null) {
                    onBookmarkClick(article)



                }
            },

        )
    }




    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

}