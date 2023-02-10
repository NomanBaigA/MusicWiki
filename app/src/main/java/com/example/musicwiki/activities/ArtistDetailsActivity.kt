package com.example.musicwiki.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicwiki.api.*
import com.example.musicwiki.adapters.AlbumRecyclerAdapter
import com.example.musicwiki.adapters.TrackRecyclerAdapter
import com.example.musicwiki.api.RetrofitInstance
import com.example.musicwiki.databinding.ActivityArtistDetailsBinding
import retrofit2.Call
import retrofit2.Response

class ArtistDetailsActivity : AppCompatActivity() {

    var artist:String=""
    var imageData:String=""
    lateinit var adapter: AlbumRecyclerAdapter
    var albumList :MutableList<AlbumListResponse> = arrayListOf()

    lateinit var adapterTracks: TrackRecyclerAdapter
    var trackList :MutableList<TrackListResponse> = arrayListOf()

    private lateinit var binding: ActivityArtistDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        artist = intent.getStringExtra("artist")!!
        imageData = intent.getStringExtra("image")!!

        binding.tagNametv.text = artist

        Glide
            .with(this)
            .load(imageData)
            .centerCrop()
            .into(binding.backImage);

        initAlbumRecycler()
        initTrackRecycler()
        getArtistDetailsApi()
        getArtistTopAlbums()
        getArtistTopTracks()

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initAlbumRecycler(){
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false) as RecyclerView.LayoutManager;
        binding.topAlbumsRecyclerView.layoutManager = layoutManager;
        adapter = AlbumRecyclerAdapter(albumList, this,"album")
        binding.topAlbumsRecyclerView.adapter = adapter
    }

    private fun initTrackRecycler(){
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false) as RecyclerView.LayoutManager;
        binding.topTracksRecyclerView.layoutManager = layoutManager;
        adapterTracks = TrackRecyclerAdapter(trackList, this,"track")
        binding.topTracksRecyclerView.adapter = adapterTracks
    }

    private fun getArtistTopAlbums() {

        binding.spinKitMasterView.visibility= View.VISIBLE
        val api: RetrofitInstance = RetrofitInstance()
        val call : Call<ArtistTopAlbumsApiResponse> = api.getArtistTopAlbums(artist)
        call.enqueue(object: retrofit2.Callback<ArtistTopAlbumsApiResponse>{
            override fun onResponse(call: Call<ArtistTopAlbumsApiResponse>, response: Response<ArtistTopAlbumsApiResponse>){

                if (response.code() == 200) {

                    albumList= response.body()!!.topalbums.album
                    adapter.updateDataset(albumList)
                }

                binding.spinKitMasterView.visibility= View.GONE
            }
            override fun onFailure(call: Call<ArtistTopAlbumsApiResponse>, t: Throwable) {
                Log.e("error", t.toString())
                binding.spinKitMasterView.visibility= View.GONE
            }
        })
    }

    private fun getArtistTopTracks() {

        binding.spinKitMasterView.visibility= View.VISIBLE
        val api: RetrofitInstance = RetrofitInstance()
        val call : Call<ArtistTopTracksApiResponse> = api.getArtistTopTracks(artist)
        call.enqueue(object: retrofit2.Callback<ArtistTopTracksApiResponse>{
            override fun onResponse(call: Call<ArtistTopTracksApiResponse>, response: Response<ArtistTopTracksApiResponse>){

                if (response.code() == 200) {

                    trackList= response.body()!!.toptracks.track
                    adapterTracks.updateDataset(trackList)
                }

                binding.spinKitMasterView.visibility= View.GONE
            }
            override fun onFailure(call: Call<ArtistTopTracksApiResponse>, t: Throwable) {
                Log.e("error", t.toString())
                binding.spinKitMasterView.visibility= View.GONE
            }
        })
    }

    private fun getArtistDetailsApi() {

        binding.spinKitMasterView.visibility= View.VISIBLE
        val api: RetrofitInstance = RetrofitInstance()
        val call : Call<ArtistDetailsApiResponse> = api.getArtistDetailsApi(artist)
        call.enqueue(object: retrofit2.Callback<ArtistDetailsApiResponse>{
            override fun onResponse(call: Call<ArtistDetailsApiResponse>, response: Response<ArtistDetailsApiResponse>){

                if (response.code() == 200) {
                    binding.textView3.text  = response.body()!!.artist.bio.summary
                    binding.playcountTv.text ="${response.body()!!.artist.stats.playcount.substring(0,3)}K"
                    binding.followersTv.text ="${response.body()!!.artist.stats.listeners.substring(0,3)}K"
                }

                binding.spinKitMasterView.visibility= View.GONE
            }
            override fun onFailure(call: Call<ArtistDetailsApiResponse>, t: Throwable) {
                Log.e("error", t.toString())
                binding.spinKitMasterView.visibility= View.GONE

            }
        })
    }
}