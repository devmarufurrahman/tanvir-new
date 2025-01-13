package com.adaptixinnovate.tanvirahmedrobin.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.adaptixinnovate.tanvirahmedrobin.R
import com.adaptixinnovate.tanvirahmedrobin.adapter.GalleryAdapter
import com.adaptixinnovate.tanvirahmedrobin.adapter.ShimmerAdapter
import com.adaptixinnovate.tanvirahmedrobin.constants.AppConfig
import com.adaptixinnovate.tanvirahmedrobin.databinding.ActivityGalleryBinding
import com.adaptixinnovate.tanvirahmedrobin.model.GalleryModel
import com.adaptixinnovate.tanvirahmedrobin.network.api.ApiService
import com.adaptixinnovate.tanvirahmedrobin.network.repository.GalleryRepository
import com.adaptixinnovate.tanvirahmedrobin.ui.viewmodel.GalleryViewModel
import com.adaptixinnovate.tanvirahmedrobin.ui.viewmodel.GalleryViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GalleryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityGalleryBinding
    private lateinit var viewModel: GalleryViewModel
    private lateinit var adapter: GalleryAdapter
    private lateinit var shimmerAdapter: ShimmerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Setup the toolbar
        setSupportActionBar(binding.customToolbar)
        // Enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()
        setupViewModel()
        observeData()

    }

    private fun setupViewModel() {
        val apiService = Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val repository = GalleryRepository(apiService)
        val factory = GalleryViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[GalleryViewModel::class.java]
    }

    private fun setupRecyclerView() {
        binding.galleryRecyclerView.layoutManager = GridLayoutManager(this, 2)
        shimmerAdapter = ShimmerAdapter()
        binding.galleryRecyclerView.adapter = shimmerAdapter
    }


    private fun observeData() {
        viewModel.galleryItems.observe(this) { items ->
            if (items.isEmpty()) {
                showShimmerEffect()
            } else {
                adapter = GalleryAdapter(items) { item ->
                    val intent = Intent(this, FullscreenActivity::class.java)
                    intent.putParcelableArrayListExtra("GALLERY_ITEMS", ArrayList(items))
                    intent.putExtra("SELECTED_ITEM_ID", item.id)
                    startActivity(intent)
                }
                binding.galleryRecyclerView.adapter = adapter
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.fetchGalleryItems()
        }, 700)

    }

    private fun showShimmerEffect() {
        binding.galleryRecyclerView.adapter = shimmerAdapter
    }


    override fun onSupportNavigateUp(): Boolean {
        // This method is called when the up button is pressed. Just finish the activity
        finish()
        return true
    }


}