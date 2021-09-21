package com.interview.musicsearch.ui

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.interview.musicsearch.R
import com.interview.musicsearch.databinding.ActivityMainBinding
import com.interview.musicsearch.ui.album_tracks.AlbumTracksFragment
import com.interview.musicsearch.ui.artist_albums.ArtistAlbumsFragment
import com.interview.musicsearch.ui.search_artists.SearchArtistsFragment
import com.interview.musicsearch.util.ALBUM_TRACKS
import com.interview.musicsearch.util.ARTIST_ALBUMS
import com.interview.musicsearch.util.SEARCH_ARTIST
import com.interview.musicsearch.util.UriUtil
import dagger.hilt.android.AndroidEntryPoint

fun Context.getContentIntent(type: Int, id: String = ""): Intent {
    return Intent(this, MainActivity::class.java).apply {
        action = ACTION_VIEW
        data = UriUtil.getContentUri(type, id)
    }
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            onNewIntent(intent)
        }

        setSupportActionBar(binding.toolbar)
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        var fragment: Fragment? = null

        if (intent.action == ACTION_VIEW) {

            intent.data?.let { uri ->

                val type = UriUtil.getType(uri)

                when (type) {
                    SEARCH_ARTIST ->
                        fragment = SearchArtistsFragment.newInstance()
                    ARTIST_ALBUMS ->
                        UriUtil.getId(uri)?.let {
                            fragment = ArtistAlbumsFragment.newInstance(it)
                        }
                    ALBUM_TRACKS ->
                        UriUtil.getId(uri)?.let {
                            fragment = AlbumTracksFragment.newInstance(it)
                        }
                    else ->
                        throw Exception("Invalid Uri: type")

                }
            }
        } else {
            fragment = SearchArtistsFragment.newInstance()
        }

        fragment?.let {

            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(R.id.fragment_container_view, it)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}