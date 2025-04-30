package com.example.caraousel

import android.os.Bundle
import android.view.animation.BounceInterpolator
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsAnimationCompat.BoundsCompat
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.caraousel.adapter.CarouselAdapter
import com.example.caraousel.databinding.ActivityMainBinding
import com.example.caraousel.repository.AlbumRepository
import com.example.caraousel.viewmodel.AlbumViewModel
import com.example.caraousel.viewmodel.AlbumViewModelFactory
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CarouselAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        val repository = AlbumRepository()
        val viewModel = ViewModelProvider(this, AlbumViewModelFactory(repository))[AlbumViewModel::class.java]

        adapter = CarouselAdapter(emptyList()){ album->
            Toast.makeText(this, "Clicked: Album${album.title}", Toast.LENGTH_SHORT).show()

        }
        recyclerView.layoutManager = CarouselLayoutManager()
        recyclerView.adapter = adapter

        viewModel.albums.observe(this) {
            adapter.updateList(it)
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }
}