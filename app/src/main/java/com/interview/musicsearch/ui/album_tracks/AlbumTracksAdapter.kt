package com.interview.musicsearch.ui.album_tracks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.interview.musicsearch.data.model.Album
import com.interview.musicsearch.data.model.Track
import com.interview.musicsearch.databinding.AlbumListHeaderViewBinding
import com.interview.musicsearch.databinding.TrackListItemViewBinding
import com.interview.musicsearch.databinding.VolumeListItemViewBinding
import com.squareup.picasso.Picasso


private const val VIEW_TYPE_ALBUM = 1
private const val VIEW_TYPE_TRACK = 2
private const val VIEW_TYPE_DISK = 3


class AlbumTracksAdapter :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(DiffCallback()) {


    override fun getItemViewType(position: Int): Int {

        return when (getItem(position)) {
            is DataItem.AlbumItem -> VIEW_TYPE_ALBUM
            is DataItem.TrackerItem -> VIEW_TYPE_TRACK
            is DataItem.DiskItem -> VIEW_TYPE_DISK
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            VIEW_TYPE_ALBUM -> {
                AlbumHeaderViewHolder(
                    AlbumListHeaderViewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            VIEW_TYPE_TRACK -> {
                TrackItemViewHolder(
                    TrackListItemViewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            VIEW_TYPE_DISK -> {
                VolumeHeaderViewHolder(
                    VolumeListItemViewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }


            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AlbumHeaderViewHolder -> {
                val item = getItem(position) as DataItem.AlbumItem
                holder.bind(item.album)
            }

            is TrackItemViewHolder -> {
                val item = getItem(position) as DataItem.TrackerItem
                holder.bind(item.track)
            }

            is VolumeHeaderViewHolder -> {
                val item = getItem(position) as DataItem.DiskItem
                holder.bind(item.text)
            }
        }
    }

    class AlbumHeaderViewHolder(private val binding: AlbumListHeaderViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {

            Picasso.get().load(album.cover_big).into(binding.coverImageView)
            binding.artistTextView.text = album.artist?.name
            binding.titleTextView.text = album.title
        }
    }

    class VolumeHeaderViewHolder(private val binding: VolumeListItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(text: String) {
            binding.volumeTextView.text = text
        }
    }


    class TrackItemViewHolder(private val binding: TrackListItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Track) {

            binding.numberTextView.text = item.track_position.toString()
            binding.artistTextView.text = item.artist?.name
            binding.titleTextView.text = item.title
        }
    }
}

sealed class DataItem {
    data class TrackerItem(val track: Track) : DataItem() {
        override val id = track.id
    }

    data class DiskItem(val text: String) : DataItem() {
        override val id = text
    }

    data class AlbumItem(val album: Album) : DataItem() {
        override val id = "Album"
    }

    abstract val id: String
}

private class DiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}