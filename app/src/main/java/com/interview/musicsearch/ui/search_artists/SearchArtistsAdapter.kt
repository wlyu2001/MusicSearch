package com.interview.musicsearch.ui.search_artists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.interview.musicsearch.data.model.Artist
import com.interview.musicsearch.databinding.ArtistHeaderViewBinding
import com.interview.musicsearch.databinding.ArtistListItemViewBinding
import com.interview.musicsearch.ui.getContentIntent
import com.interview.musicsearch.util.ARTIST_ALBUMS
import com.squareup.picasso.Picasso


private const val VIEW_TYPE_HEADER = 1
private const val VIEW_TYPE_ITEM = 2


class SearchArtistAdapter :
    ListAdapter<Artist, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> ArtistHeaderViewHolder(
                ArtistHeaderViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            VIEW_TYPE_ITEM -> ArtistItemViewHolder(
                ArtistListItemViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        val count = super.getItemCount()
        return if (count == 0) 0
        else count + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return VIEW_TYPE_HEADER
        }
        return VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ArtistItemViewHolder) {
            holder.bind(getItem(position - 1))
        }
    }

    class ArtistHeaderViewHolder(binding: ArtistHeaderViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ArtistItemViewHolder(private val binding: ArtistListItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Artist) {
            Picasso.get().load(item.picture_small).into(binding.artistImage)

            binding.nameTextView.text = item.name

            itemView.setOnClickListener { view ->
                view.context.let {
                    it.startActivity(it.getContentIntent(ARTIST_ALBUMS, item.id))
                }
            }
        }
    }
}

private class DiffCallback : DiffUtil.ItemCallback<Artist>() {
    override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
        return oldItem == newItem
    }
}