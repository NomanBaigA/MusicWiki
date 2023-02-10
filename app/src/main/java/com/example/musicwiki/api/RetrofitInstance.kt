package com.example.musicwiki.api

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {

    companion object {

        const val baseURL = "http://ws.audioscrobbler.com/"
        const val apiKey = "YOUR_API_KEY_HERE"
        const val format = "json"

        const val getTagMethod = "chart.gettoptags"
        const val getDetailsMethod = "tag.getinfo"
        const val getAlbum = "tag.gettopalbums"
        var getArtist = "tag.gettopartists"
        var getTracks = "tag.gettoptracks"

        var getAlbumInfo = "album.getinfo"
        var getArtistInfo = "artist.getinfo"

        const val getArtistTopAlbum = "artist.gettopalbums"
        const val getArtistToptrack = "artist.gettoptracks"
    }

    private val api: RetrofitServices

    init {

        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .client(getUnsafeOkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        api = retrofit.create(RetrofitServices::class.java)
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        builder
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
        return builder.build()
    }

    fun getTagsApi(): Call<TagsApiResponse> {
        return api.getTagsApi(apikey = apiKey, method = getTagMethod, format = format)
    }

    fun getTagsDetailsApi(tag: String): Call<TagDetailsApiResponse> {
        return api.getTagsDetailsApi(
            apikey = apiKey,
            method = getDetailsMethod,
            format = format,
            tag = tag
        )
    }

    fun getAlbumApi(tag: String): Call<AlbumApiResponse> {
        return api.getAlbumApi(apikey = apiKey, method = getAlbum, format = format, tag = tag)
    }

    fun getArtistApi(tag: String): Call<ArtistApiResponse> {
        return api.getArtistApi(apikey = apiKey, method = getArtist, format = format, tag = tag)
    }

    fun getTracksApi(tag: String): Call<TrackApiResponse> {
        return api.getTracksApi(apikey = apiKey, method = getTracks, format = format, tag = tag)
    }

    fun getAlbumDetailsApi(artist: String, album: String): Call<AlbumDetailsApiResponse> {
        return api.getAlbumDetailsApi(
            apikey = apiKey,
            method = getAlbumInfo,
            format = format,
            artist = artist,
            album = album
        )
    }

    fun getArtistDetailsApi(artist: String): Call<ArtistDetailsApiResponse> {
        return api.getArtistDetailsApi(
            apikey = apiKey,
            method = getArtistInfo,
            format = format,
            artist = artist
        )
    }

    fun getArtistTopAlbums(artist: String): Call<ArtistTopAlbumsApiResponse> {
        return api.getArtistTopAlbums(
            apikey = apiKey,
            method = getArtistTopAlbum,
            format = format,
            artist = artist
        )
    }

    fun getArtistTopTracks(artist: String): Call<ArtistTopTracksApiResponse> {
        return api.getArtistTopTracks(
            apikey = apiKey,
            method = getArtistToptrack,
            format = format,
            artist = artist
        )
    }
}
