package com.example.musicwiki.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicwiki.api.AlbumApiResponse
import com.example.musicwiki.api.AlbumListResponse
import com.example.musicwiki.adapters.TagDetailsRecyclerAdapter
import com.example.musicwiki.api.RetrofitInstance
import com.example.musicwiki.databinding.FragmentAlbumBinding
import retrofit2.Call
import retrofit2.Response

class AlbumFragment : Fragment() {

    lateinit var albumRecyclerView: RecyclerView
    lateinit var adapter: TagDetailsRecyclerAdapter
    lateinit var spinKitMasterView: LinearLayout

    var albumList: MutableList<AlbumListResponse> = arrayListOf()

    private lateinit var ctx: Context

    private var _binding: FragmentAlbumBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)

        albumRecyclerView = binding.albumRecyclerView
        spinKitMasterView = binding.spinKitMasterView

        initTabRecycler()
        getTagsApi()

        return binding.root
    }

    private fun initTabRecycler() {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(ac) as RecyclerView.LayoutManager;
        albumRecyclerView.layoutManager = layoutManager;
        albumRecyclerView.layoutManager = GridLayoutManager(ac, 2)
        adapter = TagDetailsRecyclerAdapter(albumList, ac, "album")
        albumRecyclerView.adapter = adapter
    }

    private fun getTagsApi() {

        spinKitMasterView.visibility = View.VISIBLE
        val api: RetrofitInstance = RetrofitInstance()
        val call: Call<AlbumApiResponse> = api.getAlbumApi(tagName)
        call.enqueue(object : retrofit2.Callback<AlbumApiResponse> {
            override fun onResponse(
                call: Call<AlbumApiResponse>,
                response: Response<AlbumApiResponse>
            ) {

                if (response.code() == 200) {

                    albumList = response.body()!!.albums.album
                    adapter.updateDataset(albumList)
                }

                spinKitMasterView.visibility = View.GONE
            }

            override fun onFailure(call: Call<AlbumApiResponse>, t: Throwable) {
                Log.e("error", t.toString())
                spinKitMasterView.visibility = View.GONE
            }
        })
    }

    companion object {

        private lateinit var tagName: String
        private lateinit var ac: Activity
        fun newInstance(name: String, a: Activity): AlbumFragment {
            val f = AlbumFragment()
            this.tagName = name
            ac = a
            return f
        }
    }
}