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
import com.example.musicwiki.api.TrackApiResponse
import com.example.musicwiki.api.TrackListResponse
import com.example.musicwiki.adapters.TrackRecyclerAdpater
import com.example.musicwiki.api.RetrofitInstance
import com.example.musicwiki.databinding.FragmentTracksBinding
import retrofit2.Call
import retrofit2.Response

class TracksFragment : Fragment() {

    lateinit var tracksRecyclerView: RecyclerView
    lateinit var adapter: TrackRecyclerAdpater
    lateinit var spinKitMasterView: LinearLayout
    var trackList: MutableList<TrackListResponse> = arrayListOf()

    private lateinit var ctx: Context

    private var _binding: FragmentTracksBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTracksBinding.inflate(inflater, container, false)

        tracksRecyclerView = binding.tracksRecyclerView
        spinKitMasterView = binding.spinKitMasterView

        initTabRecycler()
        getTracksApi()

        return binding.root
    }

    private fun initTabRecycler() {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(ac) as RecyclerView.LayoutManager;
        tracksRecyclerView.layoutManager = layoutManager;
        tracksRecyclerView.layoutManager = GridLayoutManager(ac, 2)
        adapter = TrackRecyclerAdpater(trackList, ac, "track")
        tracksRecyclerView.adapter = adapter
    }

    private fun getTracksApi() {

        spinKitMasterView.visibility = View.VISIBLE
        val api: RetrofitInstance = RetrofitInstance()
        val call: Call<TrackApiResponse> = api.getTracksApi(tagName)
        call.enqueue(object : retrofit2.Callback<TrackApiResponse> {
            override fun onResponse(
                call: Call<TrackApiResponse>,
                response: Response<TrackApiResponse>
            ) {

                if (response.code() == 200) {

                    trackList = response.body()!!.tracks.track
                    adapter.updateDataset(trackList)
                }

                spinKitMasterView.visibility = View.GONE
            }

            override fun onFailure(call: Call<TrackApiResponse>, t: Throwable) {
                Log.e("error", t.toString())
                spinKitMasterView.visibility = View.GONE
            }
        })
    }

    companion object {

        private lateinit var tagName: String
        private lateinit var ac: Activity
        fun newInstance(name: String, a: Activity): TracksFragment {
            val f = TracksFragment()
            this.tagName = name
            ac = a
            return f
        }
    }
}