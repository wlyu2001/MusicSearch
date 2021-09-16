package com.interview.musicsearch.di

import com.interview.musicsearch.api.ContentAPI
import com.interview.musicsearch.data.model.*
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.delay


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
class TestNetworkModule {

    private val artist1 = Artist(
        "119",
        "Metallica",
        "https://cdns-images.dzcdn.net/images/artist/b4719bc7a0ddb4a5be41277f37856ae6/56x56-000000-80-0-0.jpg",
        "https://cdns-images.dzcdn.net/images/artist/b4719bc7a0ddb4a5be41277f37856ae6/250x250-000000-80-0-0.jpg",
        "https://cdns-images.dzcdn.net/images/artist/b4719bc7a0ddb4a5be41277f37856ae6/500x500-000000-80-0-0.jpg"
    )

    private val artist2 = Artist(
        "1241367",
        "Metallica Tribute Band",
        "https://cdns-images.dzcdn.net/images/artist//56x56-000000-80-0-0.jpg",
        "https://cdns-images.dzcdn.net/images/artist//250x250-000000-80-0-0.jpg",
        "https://cdns-images.dzcdn.net/images/artist//500x500-000000-80-0-0.jpg"
    )

    private val artist3 = Artist(
        "117054162",
        "SaD - Symphony and Metallica",
        "https://cdns-images.dzcdn.net/images/artist/27930b1ae19d59bb9286647ef69209a3/56x56-000000-80-0-0.jpg",
        "https://cdns-images.dzcdn.net/images/artist/27930b1ae19d59bb9286647ef69209a3/250x250-000000-80-0-0.jpg",
        "https://cdns-images.dzcdn.net/images/artist/27930b1ae19d59bb9286647ef69209a3/500x500-000000-80-0-0.jpg"
    )

    private val album1 = Album(
        "85669472",
        "Helping Hands…Live & Acoustic At The Masonic",
        "https://cdns-images.dzcdn.net/images/cover/05901c9237ada281beadfb04adbf2bb1/56x56-000000-80-0-0.jpg",
        "https://cdns-images.dzcdn.net/images/cover/05901c9237ada281beadfb04adbf2bb1/250x250-000000-80-0-0.jpg",
        "https://cdns-images.dzcdn.net/images/cover/05901c9237ada281beadfb04adbf2bb1/500x500-000000-80-0-0.jpg"
    )

    private val album2 = Album(
        "49896702",
        "Live At House Of Vans, London, 18.11.16",
        "https://cdns-images.dzcdn.net/images/cover/1da3ace9182ae64431bfac1dfdb17e67/56x56-000000-80-0-0.jpg",
        "https://cdns-images.dzcdn.net/images/cover/1da3ace9182ae64431bfac1dfdb17e67/250x250-000000-80-0-0.jpg",
        "https://cdns-images.dzcdn.net/images/cover/1da3ace9182ae64431bfac1dfdb17e67/500x500-000000-80-0-0.jpg"
    )

    private val album3 = Album(
        "14581202",
        "Hardwired…To Self-Destruct (Deluxe)",
        "https://cdns-images.dzcdn.net/images/cover/22df6212ca5a43b3ec83caa814e8da16/56x56-000000-80-0-0.jpg",
        "https://cdns-images.dzcdn.net/images/cover/22df6212ca5a43b3ec83caa814e8da16/250x250-000000-80-0-0.jpg",
        "https://cdns-images.dzcdn.net/images/cover/22df6212ca5a43b3ec83caa814e8da16/500x500-000000-80-0-0.jpg"
    )

    private val metallica = SimpleArtist("Metallica")

    private val tracks1 = Track(
        "136332796",
        "Hardwired",
        1,
        1
    ).apply { artist = metallica }

    private val tracks2 = Track(
        "136332798",
        "Atlas, Rise!",
        2,
        1
    ).apply { artist = metallica }

    private val tracks3 = Track(
        "136332808",
        "Confusion",
        1,
        2
    ).apply { artist = metallica }

    private val tracks4 = Track(
        "136332820",
        "Lords Of Summer",
        1,
        3
    ).apply { artist = metallica }


    @Provides
    fun provideContentAPI(): ContentAPI {

        val service = mockk<ContentAPI>()

        coEvery { service.searchArtists(any()) } coAnswers {
            delay(500)
            Data(listOf(artist1, artist2, artist3))
        }

        coEvery { service.fetchArtist(artist1.id) } coAnswers {
            delay(500)
            artist1
        }

        coEvery { service.fetchArtistAlbums(artist1.id) } coAnswers {
            delay(500)
            Data(listOf(album1, album2, album3))
        }

        coEvery { service.fetchAlbum(album3.id) } coAnswers {
            delay(500)
            album3
        }
        coEvery { service.fetchAlbumTracks(album3.id) } coAnswers {
            delay(500)
            Data(listOf(tracks1, tracks2, tracks3, tracks4))
        }

        return service
    }
}