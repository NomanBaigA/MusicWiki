package com.example.musicwiki.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicwiki.api.TagsApiResponse
import com.example.musicwiki.api.TagsListResponse
import com.example.musicwiki.R
import com.example.musicwiki.adapters.GenreRecyclerAdapter
import com.example.musicwiki.api.RetrofitInstance
import com.example.musicwiki.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Response

// TODO -> Put api key in "RetrofitInstance" Class

class MainActivity : AppCompatActivity() {
    lateinit var adapter : GenreRecyclerAdapter
    var tagList: MutableList<TagsListResponse> =  arrayListOf()

    var openList = false

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initTabRecycler()

        binding.dropDownBtn.setOnClickListener{

            if(openList){
                openList = false
                binding.dropDownBtn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_keyboard_arrow_down_24, null))
                adapter.updateDataset(tagList.subList(0,10))
            }else{
                openList = true
                binding.dropDownBtn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_keyboard_arrow_up_24, null))
                adapter.updateDataset(tagList)
            }
        }

        getTagsApi()
    }

    private fun initTabRecycler(){
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager;
        binding.genereRecyclerView.layoutManager = layoutManager;
        binding.genereRecyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = GenreRecyclerAdapter(tagList,this)
        binding.genereRecyclerView.adapter = adapter
    }

    private fun getTagsApi() {

        binding.spinKitMasterView.visibility= View.VISIBLE

        val api: RetrofitInstance = RetrofitInstance()
        val call : Call<TagsApiResponse> = api.getTagsApi()

        call.enqueue(object: retrofit2.Callback<TagsApiResponse>{
            override fun onResponse(call: Call<TagsApiResponse>, response: Response<TagsApiResponse>){

                if (response.code() == 200) {
                   tagList= response.body()!!.tags.tag
                   if(tagList.size>10){
                       adapter.updateDataset(tagList.subList(0,10))
                       binding.linearLayout.visibility = View.VISIBLE
                   }else{
                       adapter.updateDataset(tagList)
                       binding.linearLayout.visibility = View.GONE

                   }
                }
                binding.spinKitMasterView.visibility= View.GONE
            }
            override fun onFailure(call: Call<TagsApiResponse>, t: Throwable) {
                Log.e("error", t.toString())
                binding.spinKitMasterView.visibility= View.GONE
            }
        })
    }
}
