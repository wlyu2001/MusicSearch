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
import com.interview.musicsearch.util.EspressoIdlingResource
import dagger.hilt.android.AndroidEntryPoint


private const val ID_KEY = "id"

@AndroidEntryPoint
class ArtistAlbumsFragment : Fragment() {

    private val viewModel: ArtistAlbumsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            arguments?.let {
                it.getString(ID_KEY)?.let { id ->
                    EspressoIdlingResource.increment()
                    viewModel.getArtistAlbums(id)
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

        val binding = FragmentArtistAlbumsBinding.inflate(inflater, container, false)

        val adapter = ArtistAlbumsAdapter()

        binding.albumsRecyclerView.adapter = adapter
        binding.albumsRecyclerView.itemAnimator = null
        binding.albumsRecyclerView.layoutManager = GridLayoutManager(context, 2)

        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowCustomEnabled(false)
            setDisplayShowTitleEnabled(true)
            setTitle(R.string.albums)
        }

        viewModel.artistAlbumsLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
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
        fun newInstance(id: String): ArtistAlbumsFragment {
            return ArtistAlbumsFragment().apply {
                arguments = Bundle().apply {
                    putString(ID_KEY, id)
                }
            }
        }
    }
}