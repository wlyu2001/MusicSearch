package com.interview.musicsearch.ui.album_tracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.interview.musicsearch.R
import com.interview.musicsearch.databinding.FragmentAlbumTracksBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


private const val ID_KEY = ""

@AndroidEntryPoint
class AlbumTracksFragment : Fragment() {

    private val viewModel: AlbumTracksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        arguments?.let {
            it.getString(ID_KEY)?.let { id ->
                viewModel.getAlbumTracks(id)
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentAlbumTracksBinding.inflate(inflater, container, false)

        val adapter = AlbumTracksAdapter()

        binding.tracksRecyclerView.adapter = adapter
        binding.tracksRecyclerView.itemAnimator = null

        viewModel.albumTracksLiveData.observe(viewLifecycleOwner) {
            adapter.addHeaderAndSubmitList(it.first, it.second) {
                binding.tracksRecyclerView.scrollToPosition(0)
            }
        }

        viewModel.spinnerLiveData.observe(viewLifecycleOwner) { value ->
            value.let { show ->
                binding.spinner.visibility = if (show) View.VISIBLE else View.GONE
            }
        }
        viewModel.snackbarLiveData.observe(viewLifecycleOwner) { text ->
            text?.let {
                Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
                viewModel.resetSnackbar()
            }
        }

        return binding.root
    }

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