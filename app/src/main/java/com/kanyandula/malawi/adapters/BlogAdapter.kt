package com.kanyandula.malawi.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kanyandula.malawi.data.model.Blog
import com.kanyandula.malawi.databinding.BlogListItemBinding
import java.lang.ref.WeakReference

class BlogAdapter(
    private val onItemClick: (Blog) -> Unit,
    private val onBookmarkClick: (Blog) -> Unit,
    private val callbackWeakRef: WeakReference<NewsFeedItemInterface>
): ListAdapter<Blog, BlogViewHolder>(BlogArticleComparator()) {

    interface NewsFeedItemInterface {
        fun onFavoriteStatusChanged(newsFeedItemId: String, newStatus: Boolean)
    }



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
                   // article.title?.let { callbackWeakRef.get()?.onFavoriteStatusChanged(it, newStatus = equals(Any()) ) }


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