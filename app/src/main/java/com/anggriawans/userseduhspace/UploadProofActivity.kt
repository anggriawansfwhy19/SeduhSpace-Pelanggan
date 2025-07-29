package com.anggriawans.userseduhspace

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anggriawans.userseduhspace.databinding.ActivityUploadProofBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UploadProofActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadProofBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadProofBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("user")

        binding.selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        binding.finishButton.setOnClickListener {
            if (selectedImageUri != null) {
                uploadImageToFirebase(selectedImageUri!!)
            } else {
                Toast.makeText(this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.uploadedImageView.setImageURI(selectedImageUri)

            // Efek animasi ketika gambar berhasil ditampilkan
            binding.uploadedImageView.alpha = 0f
            binding.uploadedImageView.scaleX = 0.8f
            binding.uploadedImageView.scaleY = 0.8f
            binding.uploadedImageView.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(400)
                .start()

            binding.finishButton.isEnabled = true
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = databaseReference.child(userId)
            val paymentProofUrl = "url_to_payment_proof" // Ganti dengan URL yang sesuai setelah upload
            userReference.child("paymentProof").setValue(paymentProofUrl)
                .addOnSuccessListener {
                    Toast.makeText(this, "Bukti pembayaran berhasil diupload", Toast.LENGTH_SHORT).show()
                    val resultIntent = Intent()
                    resultIntent.putExtra("isProofUploaded", true)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal mengupload bukti pembayaran", Toast.LENGTH_SHORT).show()
                }
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}
