package com.example.musicwiki.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.musicwiki.api.TagDetailsApiResponse
import com.example.musicwiki.fragments.AlbumFragment
import com.example.musicwiki.fragments.ArtistFragment
import com.example.musicwiki.fragments.TracksFragment
import com.example.musicwiki.adapters.ViewPagerAdpater
import com.example.musicwiki.api.RetrofitInstance
import com.example.musicwiki.databinding.ActivityTagDetailsBinding
import retrofit2.Call
import retrofit2.Response

class TagDetailsActivity : AppCompatActivity() {

    var tag: String =""

    private lateinit var binding: ActivityTagDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTagDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        tag = intent.getStringExtra("tagName")!!

        val adapter = ViewPagerAdpater(supportFragmentManager)
        adapter.addFragment(AlbumFragment.newInstance(tag, this), "ALBUMS")
        adapter.addFragment(ArtistFragment.newInstance(tag, this), "ARTISTS")
        adapter.addFragment(TracksFragment.newInstance(tag, this), "TRACKS")

        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)

        getTagsApi()

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getTagsApi() {

        binding.spinKitMasterView.visibility= View.VISIBLE

        val api: RetrofitInstance = RetrofitInstance()
        val call : Call<TagDetailsApiResponse> = api.getTagsDetailsApi(tag.toString())

        call.enqueue(object: retrofit2.Callback<TagDetailsApiResponse>{
            override fun onResponse(call: Call<TagDetailsApiResponse>, response: Response<TagDetailsApiResponse>){

                if (response.code() == 200) {

                    binding.textView3.text  = response.body()!!.tag.wiki!!.summary
                    binding.tagNametv.text = response.body()!!.tag.name
                }

                binding.spinKitMasterView.visibility= View.GONE
            }
            override fun onFailure(call: Call<TagDetailsApiResponse>, t: Throwable) {
                Log.e("error", t.toString())
                binding.spinKitMasterView.visibility= View.GONE
            }
        })
    }
}