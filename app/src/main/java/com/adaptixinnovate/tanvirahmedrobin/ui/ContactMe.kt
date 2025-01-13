package com.adaptixinnovate.tanvirahmedrobin.ui

import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.adaptixinnovate.tanvirahmedrobin.databinding.ActivityContactMeBinding
import com.adaptixinnovate.tanvirahmedrobin.network.retrofit.RetrofitClient
import com.adaptixinnovate.tanvirahmedrobin.utils.FilePickerUtility
import com.adaptixinnovate.tanvirahmedrobin.services.SendData
import com.adaptixinnovate.tanvirahmedrobin.services.SharedPrefereneService
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.Objects

class ContactMe : AppCompatActivity() {
    private lateinit var binding: ActivityContactMeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactMeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.customToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val settings = SharedPrefereneService.getSettingsFromPreferences(this)

        binding.swipeRefreshLayout.setOnRefreshListener {
            recreate()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        if (settings.email.isEmpty()) binding.emailLayout.visibility = View.GONE else binding.contactEmail.text = settings.email
        if (settings.phone.isEmpty()) binding.phoneLayout.visibility = View.GONE else binding.contactPhone.text = settings.phone
        if (settings.address.isEmpty()) binding.addressLayout.visibility = View.GONE else binding.address.text = settings.address

        binding.contactEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:${binding.contactEmail.text}")
            startActivity(intent)
        }

        binding.contactPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${binding.contactPhone.text}")
            startActivity(intent)
        }

        binding.facebookButton.setOnClickListener {
            openUrl(settings.facebook)
        }

        binding.linkedinButton.setOnClickListener {
            openUrl(settings.linkedin)
        }

        binding.youtubeButton.setOnClickListener {
            openUrl(settings.youtube)
        }

        binding.whatsappButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://wa.me/+88${settings.phone}")
                setPackage("com.whatsapp")
            }
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "WhatsApp is not installed on this device", Toast.LENGTH_SHORT).show()
            }
        }

        binding.instagramButton.setOnClickListener {
            openUrl(settings.instagram)
        }

        binding.webButton.setOnClickListener {
            openUrl(settings.website)
        }

    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
