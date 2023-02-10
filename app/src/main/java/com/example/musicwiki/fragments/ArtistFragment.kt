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
import com.example.musicwiki.api.ArtistApiResponse
import com.example.musicwiki.api.ArtistListResponse
import com.example.musicwiki.adapters.ArtistRecyclerAdapter
import com.example.musicwiki.api.RetrofitInstance
import com.example.musicwiki.databinding.FragmentArtistBinding
import retrofit2.Call
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [ArtistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArtistFragment : Fragment() {

    lateinit var artistRecyclerView: RecyclerView
    lateinit var adapter: ArtistRecyclerAdapter
    lateinit var spinKitMasterView: LinearLayout
    var artistList :MutableList<ArtistListResponse> = arrayListOf()

    private lateinit var ctx: Context

    private var _binding: FragmentArtistBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArtistBinding.inflate(inflater, container, false)

        artistRecyclerView= binding.artistRecyclerView
        spinKitMasterView= binding.spinKitMasterView

        initTabRecycler()
        getArtistApi()

        return binding.root
    }

    private fun initTabRecycler(){
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(ac) as RecyclerView.LayoutManager;
        artistRecyclerView.layoutManager = layoutManager;
        artistRecyclerView.layoutManager = GridLayoutManager(ac, 2)
        adapter = ArtistRecyclerAdapter(artistList, ac, "artist")
        artistRecyclerView.adapter = adapter
    }

    private fun getArtistApi() {

        spinKitMasterView.visibility= View.VISIBLE
        val api: RetrofitInstance = RetrofitInstance()
        val call : Call<ArtistApiResponse> = api.getArtistApi(tagName)
        call.enqueue(object: retrofit2.Callback<ArtistApiResponse>{
            override fun onResponse(call: Call<ArtistApiResponse>, response: Response<ArtistApiResponse>){

                if (response.code() == 200) {

                    artistList= response.body()!!.topartists.artist
                    adapter.updateDataset(artistList)
                }
                spinKitMasterView.visibility= View.GONE
            }
            override fun onFailure(call: Call<ArtistApiResponse>, t: Throwable) {
                Log.e("error", t.toString())
                spinKitMasterView.visibility= View.GONE
            }
        })
    }

    companion object {

        private lateinit var tagName: String
        private lateinit var ac: Activity
        fun newInstance(name : String, a: Activity): ArtistFragment {
            val f = ArtistFragment()
            this.tagName = name
            ac = a
            return f
        }
    }
}