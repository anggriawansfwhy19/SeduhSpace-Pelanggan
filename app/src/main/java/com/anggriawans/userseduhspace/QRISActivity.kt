package com.anggriawans.userseduhspace

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.anggriawans.userseduhspace.databinding.ActivityQrisBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class QRISActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrisBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var isProofUploaded = false // Flag to check if proof is uploaded

    // Request code untuk UploadProofActivity
    private val UPLOAD_PROOF_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this)
            .load(R.drawable.qris_coffee) // Ganti dengan URL jika gambar diambil dari internet
            .override(800, 600) // Ganti dengan ukuran yang diinginkan
            .into(binding.qrisImage)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("user")

        binding.uploadProofButton.setOnClickListener {
            // Arahkan ke UploadProofActivity
            val intent = Intent(this, UploadProofActivity::class.java)
            startActivityForResult(intent, UPLOAD_PROOF_REQUEST_CODE) // Menggunakan startActivityForResult
        }

        binding.donePaymentButton.setOnClickListener {
            if (isProofUploaded) {
                // Arahkan ke CongratsBottomSheet
                val bottomSheetDialog = CongratsBottomSheet()
                bottomSheetDialog.show(supportFragmentManager, "CongratsBottomSheet")
            } else {
                Toast.makeText(this, "Upload bukti pembayaran terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Menangani hasil dari UploadProofActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPLOAD_PROOF_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Mendapatkan hasil dari UploadProofActivity
            val isUploaded = data?.getBooleanExtra("isProofUploaded", false) ?: false
            if (isUploaded) {
                isProofUploaded = true // Set flag to true
                binding.donePaymentButton.isEnabled = true // Enable the done payment button
            }
        }
    }
}