package com.example.musicwiki.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicwiki.api.AlbumDetailsApiResponse
import com.example.musicwiki.api.ArtistDetailsApiResponse
import com.example.musicwiki.api.TagsListResponse
import com.example.musicwiki.adapters.TagREcyclerAdapter
import com.example.musicwiki.api.RetrofitInstance
import com.example.musicwiki.databinding.ActivityAlbumDetailsBinding
import retrofit2.Call
import retrofit2.Response

class AlbumDetailsActivity : AppCompatActivity() {

    var album :String =""
    var artist :String =""
    var imageData :String =""

    var tagList: MutableList<TagsListResponse> =  arrayListOf()
    lateinit var adapter : TagREcyclerAdapter

    private lateinit var binding: ActivityAlbumDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        album = intent.getStringExtra("album")!!
        artist = intent.getStringExtra("artist")!!
        imageData = intent.getStringExtra("image")!!

        binding.tagNametv.text = album
        binding.artistNametv.text = artist

        Glide
            .with(this)
            .load(imageData)
            .centerCrop()

            .into(binding.backImage);

        initTabRecycler()
        getAlbumDetailsApi()
        getArtistDetailsApi()

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initTabRecycler(){
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false) as RecyclerView.LayoutManager;
        binding.tagrecyclerView.layoutManager = layoutManager;
        adapter = TagREcyclerAdapter(tagList,this)
        binding.tagrecyclerView.adapter = adapter
    }

    private fun getAlbumDetailsApi() {

        binding.spinKitMasterView.visibility= View.VISIBLE
        val api: RetrofitInstance = RetrofitInstance()
        val call : Call<AlbumDetailsApiResponse> = api.getAlbumDetailsApi(artist,album)
        call.enqueue(object: retrofit2.Callback<AlbumDetailsApiResponse>{
            override fun onResponse(call: Call<AlbumDetailsApiResponse>, response: Response<AlbumDetailsApiResponse>){

                if (response.code() == 200) {
                    tagList = response.body()!!.album.tags.tag
                    adapter.updateDataset(tagList)
                }

                binding.spinKitMasterView.visibility= View.GONE
            }
            override fun onFailure(call: Call<AlbumDetailsApiResponse>, t: Throwable) {
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