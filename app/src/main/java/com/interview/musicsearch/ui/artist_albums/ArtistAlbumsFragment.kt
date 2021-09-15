package com.interview.musicsearch.ui.artist_albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.interview.musicsearch.R
import com.interview.musicsearch.databinding.FragmentArtistAlbumsBinding
import dagger.hilt.android.AndroidEntryPoint


private const val ID_KEY = ""

@AndroidEntryPoint
class ArtistAlbumsFragment : Fragment() {

    private val viewModel: ArtistAlbumsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(true)
            setTitle(R.string.albums)
        }

        arguments?.let {
            it.getString(ID_KEY)?.let { id ->
                viewModel.getArtistAlbums(id)
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentArtistAlbumsBinding.inflate(inflater, container, false)

        val adapter = ArtistAlbumsAdapter()

        binding.albumsRecyclerView.adapter = adapter
        binding.albumsRecyclerView.itemAnimator = null
        binding.albumsRecyclerView.layoutManager = GridLayoutManager(context, 2)

        viewModel.artistAlbumsLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.albumsRecyclerView.scrollToPosition(0)
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
        fun newInstance(id: String): ArtistAlbumsFragment {
            return ArtistAlbumsFragment().apply {
                arguments = Bundle().apply {
                    putString(ID_KEY, id)
                }
            }
        }
    }
}