package com.interview.musicsearch.ui.search_artists

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.interview.musicsearch.databinding.FragmentSearchArtistsBinding
import com.interview.musicsearch.databinding.SearchLayoutBinding
import com.interview.musicsearch.util.EspressoIdlingResource
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

        val searchBox = SearchLayoutBinding.inflate(inflater, null, false).searchBox
        val binding = FragmentSearchArtistsBinding.inflate(inflater, container, false)

        val adapter = SearchArtistAdapter()

        binding.searchArtistRecyclerView.adapter = adapter
        binding.searchArtistRecyclerView.itemAnimator = null

        viewModel.searchArtistsLiveData.observe(viewLifecycleOwner) {
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

        viewModel.searchQueryLiveData.observe(viewLifecycleOwner) { query ->
            searchBox.setQuery(query, false)
        }

        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            val params = ActionBar.LayoutParams( //Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
            ).apply {
                setMargins(10)
            }

            setCustomView(searchBox, params)
            setDisplayShowCustomEnabled(true)
            setDisplayShowHomeEnabled(false)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }

        searchBox.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                queryTextChangedJob?.let {
                    it.cancel()
                    EspressoIdlingResource.decrement()
                }

                queryTextChangedJob = lifecycleScope.launch(Dispatchers.Main) {
                    EspressoIdlingResource.increment()
                    delay(500)
                    EspressoIdlingResource.decrement()
                    newText?.let {
                        // Prevent IdlingResource from increment when coming back
                        if (viewModel.searchQueryLiveData.value != it) {
                            EspressoIdlingResource.increment()
                            viewModel.searchArtists(it)
                        }
                    }
                    queryTextChangedJob = null
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