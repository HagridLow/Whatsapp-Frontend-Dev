package com.example.metalhead_testap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.metalhead_testap.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException


const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var albumAdapter : AlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()


        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible = true
            val response = try {
                ApiInstance.api.getSearchedAlbums("Damnation")
            } catch (e: IOException) {
                Log.e(TAG, "IOException, you might not have internet connection")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            }
            if(response.isSuccessful && response.body() != null){
                albumAdapter.albums = response.body()!!
            } else {
                Log.e(TAG, "Response not successful")
            }
            binding.progressBar.isVisible = false
        }
    }



    private fun setupRecyclerView() = binding.recyclerView.apply() {
        albumAdapter = AlbumAdapter()
        adapter = albumAdapter
        layoutManager = LinearLayoutManager(this@MainActivity)
    }

 }