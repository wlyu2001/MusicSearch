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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val VIEW_TYPE_VOLUME = 1
private const val VIEW_TYPE_TRACK = 2
private const val VIEW_TYPE_HEADER = 3


class AlbumTracksAdapter :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(DiffCallback()) {

    val adapterScope = CoroutineScope(Dispatchers.Default)
    var album: Album? = null

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }


    fun addHeaderAndSubmitList(album: Album, list: List<Track>, callback: Runnable? = null) {
        this.album = album
        adapterScope.launch {

            val items = mutableListOf<DataItem>()

            val sorted = list.sortedWith(compareBy({ it.disk_number }, { it.track_position }))

            var currentDiskNumber = 0

            sorted.forEach {
                if (it.disk_number != currentDiskNumber) {
                    currentDiskNumber = it.disk_number
                    items.add(DataItem.Header("Volume${it.disk_number}"))
                }
                items.add(DataItem.TrackerItem(it))
            }

            if(currentDiskNumber == 1) {
                items.removeAt(0)
            }

            withContext(Dispatchers.Main) {
                submitList(items, callback)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return VIEW_TYPE_HEADER
        }
        return when (getItem(position - 1)) {
            is DataItem.TrackerItem -> VIEW_TYPE_TRACK
            is DataItem.Header -> VIEW_TYPE_VOLUME
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            VIEW_TYPE_HEADER -> {
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

            VIEW_TYPE_VOLUME -> {
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
                album?.let {
                    holder.bind(it)
                }
            }

            is TrackItemViewHolder -> {
                val item = getItem(position - 1) as DataItem.TrackerItem
                holder.bind(item.track)
            }

            is VolumeHeaderViewHolder -> {
                val item = getItem(position - 1) as DataItem.Header
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

    data class Header(val text: String) : DataItem() {
        override val id = text
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