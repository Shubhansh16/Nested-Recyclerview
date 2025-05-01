package com.example.caraousel

import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsAnimationCompat.BoundsCompat
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caraousel.adapter.CarouselAdapter
import com.example.caraousel.adapter.OuterAdapter
import com.example.caraousel.databinding.ActivityMainBinding
import com.example.caraousel.model.DisplayableChartItem
import com.example.caraousel.repository.AlbumRepository
import com.example.caraousel.repository.ChartRepository
import com.example.caraousel.repository.ChartRepositoryImpl
import com.example.caraousel.viewmodel.AlbumViewModel
import com.example.caraousel.viewmodel.AlbumViewModelFactory
import com.example.caraousel.viewmodel.ChartViewModel
import com.example.caraousel.viewmodel.ChartViewModelFactory
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CarouselAdapter

    private lateinit var nestedRecyclerView: RecyclerView
    private lateinit var outerChartAdapter: OuterAdapter
    private lateinit var chartViewModel: ChartViewModel
    private lateinit var chartProgressBar: ProgressBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        val repository = AlbumRepository()
        val viewModel = ViewModelProvider(this, AlbumViewModelFactory(repository))[AlbumViewModel::class.java]

        nestedRecyclerView = findViewById(R.id.nested_recycler_view)
        chartProgressBar = findViewById(R.id.chartProgressBar)

        val apiService = RetrofitClient.apiService
        val chartRepository: ChartRepository = ChartRepositoryImpl(apiService)
        val chartViewModelFactory = ChartViewModelFactory(chartRepository)
        chartViewModel = ViewModelProvider(this, chartViewModelFactory)[ChartViewModel::class.java]

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

        setupNestedRecyclerView()
        observeChartViewModel()
    }

    private fun observeChartViewModel() {
        chartViewModel.outerListItems.observe(this) { outerList -> // <-- Change observed LiveData
            outerChartAdapter.submitList(outerList) // Submit the new list type
        }

        chartViewModel.isLoading.observe(this) { isLoading ->
            chartProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            //hide nestedRecyclerView while loading?
             nestedRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        chartViewModel.error.observe(this) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(this, "Chart Error: $it", Toast.LENGTH_LONG).show()
                chartViewModel.errorShow()
            }
        }
    }

    private fun setupNestedRecyclerView() {
        outerChartAdapter = OuterAdapter { outerItem ->
            handleChartItemClick(outerItem) // Handle clicks from nested list
        }
        nestedRecyclerView.layoutManager = LinearLayoutManager(this)
        nestedRecyclerView.adapter = outerChartAdapter
        nestedRecyclerView.setHasFixedSize(true)
        nestedRecyclerView.setItemViewCacheSize(10)
    }

    private fun handleChartItemClick(item: DisplayableChartItem) {
        Toast.makeText(this, "Nested Clicked: ${item.itemType} - ${item.title}", Toast.LENGTH_SHORT).show()
    }
}