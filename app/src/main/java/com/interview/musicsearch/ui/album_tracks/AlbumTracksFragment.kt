package com.interview.musicsearch.ui.album_tracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.interview.musicsearch.databinding.FragmentAlbumTracksBinding
import com.interview.musicsearch.util.EspressoIdlingResource
import dagger.hilt.android.AndroidEntryPoint


private const val ID_KEY = "id"

@AndroidEntryPoint
class AlbumTracksFragment : Fragment() {

    private val viewModel: AlbumTracksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            arguments?.let {
                it.getString(ID_KEY)?.let { id ->
                    EspressoIdlingResource.increment()
                    viewModel.getAlbumTracks(id)
                }
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

        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
            setDisplayShowCustomEnabled(false)
        }

        viewModel.albumTracksLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.tracksRecyclerView.scrollToPosition(0)
            }

            EspressoIdlingResource.decrement()
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