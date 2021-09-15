package com.interview.musicsearch.ui.search_artists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.interview.musicsearch.databinding.FragmentSearchArtistsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchArtistsFragment : Fragment() {

    private val viewModel: SearchArtistsViewModel by viewModels()
    private var queryTextChangedJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentSearchArtistsBinding.inflate(inflater, container, false)

        val adapter = SearchArtistAdapter()

        binding.searchArtistRecyclerView.adapter = adapter
        viewModel.searchArtistsLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.searchArtistRecyclerView.scrollToPosition(0)
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

        binding.searchBox.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                queryTextChangedJob?.cancel()

                queryTextChangedJob = lifecycleScope.launch(Dispatchers.Main) {
                    delay(500)
                    newText?.let {
                        viewModel.setQueryText(it)
                    }
                }
                return false
            }
        })

        return binding.root
    }


    companion object {
        fun newInstance(): SearchArtistsFragment {
            return SearchArtistsFragment()
        }
    }
}