package com.interview.musicsearch.ui.artist_albums

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.interview.musicsearch.data.model.Album
import com.interview.musicsearch.databinding.AlbumCardViewBinding
import com.interview.musicsearch.ui.getContentIntent
import com.interview.musicsearch.util.ALBUM_TRACKS
import com.squareup.picasso.Picasso

class ArtistAlbumsAdapter :
    ListAdapter<Album, ArtistAlbumsAdapter.AlbumItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumItemViewHolder {
        return AlbumItemViewHolder(
            AlbumCardViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArtistAlbumsAdapter.AlbumItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AlbumItemViewHolder(private val binding: AlbumCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Album) {
            Picasso.get().load(item.cover_medium).into(binding.albumImage)

            binding.titleTextView.text = item.title
            binding.artistNameTextView.text = item.artist?.name

            itemView.setOnClickListener { view ->
                view.context.let {
                    it.startActivity(it.getContentIntent(ALBUM_TRACKS, item.id))
                }
            }
        }
    }
}

private class DiffCallback : DiffUtil.ItemCallback<Album>() {
    override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem == newItem
    }
}