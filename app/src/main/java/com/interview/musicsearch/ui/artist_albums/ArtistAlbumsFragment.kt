package com.interview.musicsearch.ui.artist_albums

import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint


private const val ID_KEY = ""

@AndroidEntryPoint
class ArtistAlbumsFragment : Fragment() {

    companion object {
        fun newInstance(id: String): ArtistAlbumsFragment {
            return ArtistAlbumsFragment().apply {
                arguments = Bundle().apply {
                    putString(ID_KEY, id)
                }
            }
        }
    }
}