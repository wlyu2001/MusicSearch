package com.interview.musicsearch.ui.search_artists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.interview.musicsearch.data.model.Artist
import com.interview.musicsearch.databinding.ArtistListItemViewBinding
import com.interview.musicsearch.ui.getContentIntent
import com.interview.musicsearch.util.ARTIST_ALBUMS
import com.squareup.picasso.Picasso

class SearchArtistAdapter :
    ListAdapter<Artist, SearchArtistAdapter.ArtistItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistItemViewHolder {
        return ArtistItemViewHolder(
            ArtistListItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    
    override fun onBindViewHolder(holder: ArtistItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

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