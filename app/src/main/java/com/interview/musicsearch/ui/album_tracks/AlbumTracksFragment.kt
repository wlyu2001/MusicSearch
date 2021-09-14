package com.interview.musicsearch.ui.album_tracks

import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint


private const val ID_KEY = ""

@AndroidEntryPoint
class AlbumTracksFragment : Fragment() {

    companion object {
        fun newInstance(id: String): AlbumTracksFragment {
            return AlbumTracksFragment().apply {
                arguments = Bundle().apply {
                    putString(ID_KEY, id)
                }
            }
        }
    }
}