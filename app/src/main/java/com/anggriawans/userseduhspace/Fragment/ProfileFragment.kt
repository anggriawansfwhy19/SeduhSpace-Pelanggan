package com.anggriawans.userseduhspace.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.anggriawans.userseduhspace.LoginActivity
import com.anggriawans.userseduhspace.databinding.FragmentProfileBinding
import com.anggriawans.userseduhspace.model.UserModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        setUserData()

            binding.apply {
                name.isEnabled = false
                email.isEnabled = false
                address.isEnabled = false
                phone.isEnabled = false

                binding.editButton.setOnClickListener {

                name.isEnabled=!name.isEnabled
                email.isEnabled=!email.isEnabled
                address.isEnabled=!address.isEnabled
                phone.isEnabled=!phone.isEnabled
            }
        }

        binding.saveInfoButton.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val address = binding.address.text.toString()
            val phone = binding.phone.text.toString()

            updateUserData(name, email, address, phone)
        }
        binding.imageProfile.setOnClickListener {
            // Panggil fungsi untuk mengganti foto profil
            changeProfilePicture()
        }
        binding.buttonLogout.setOnClickListener {
            // Tampilkan dialog konfirmasi
            showLogoutConfirmationDialog()
        }
        return binding.root
    }

    private fun changeProfilePicture() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Konfirmasi Logout")
        builder.setMessage("Anda yakin ingin keluar?")
        builder.setPositiveButton("OKE") { dialog, which ->
            // Panggil fungsi logout jika pengguna menekan tombol OKE
            logoutUser()
        }
        builder.setNegativeButton("NO") { dialog, which ->
            // Biarkan dialog tertutup jika pengguna menekan tombol NO
            dialog.dismiss()
        }
        builder.show()
    }

    private fun logoutUser() {
        auth.signOut()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private fun updateUserData(name: String, email: String, address: String, phone: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.getReference("user").child(userId)

            val profileImageUrl = binding.imageProfile.tag?.toString() ?: "" // Ambil URL gambar dari tag

            val userData = hashMapOf(
                "name" to name,
                "address" to address,
                "email" to email,
                "phone" to phone,
                "profileImage" to profileImageUrl // Tambahkan URL gambar
            )

            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile Update Successfully 😊", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Profile Update Failed 😢", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.getReference("user").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        if (userProfile != null) {
                            binding.name.setText(userProfile.name)
                            binding.email.setText(userProfile.email)
                            binding.address.setText(userProfile.address)
                            binding.phone.setText(userProfile.phone)

                            // Load gambar profil dengan Glide
                            userProfile.profileImage?.let {
                                Glide.with(requireContext())
                                    .load(it)
                                    .into(binding.imageProfile)

                                // Simpan URL gambar di tag
                                binding.imageProfile.tag = it
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Failed to load user data", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            // Implementasikan logika untuk mengunggah gambar ke Firebase Storage dan memperbarui URL di Firebase Database.
            // Contoh sederhana: (Harap disesuaikan dengan struktur database dan storage Anda)
            val userId = auth.currentUser?.uid
            if (userId != null) {
                val storageRef = storage.reference.child("profileImages").child(userId)
                storageRef.putFile(imageUri!!)
                    .addOnSuccessListener {
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            val userReference = database.getReference("user").child(userId)
                            userReference.child("profileImage").setValue(uri.toString())

                            // Load gambar dengan Glide
                            Glide.with(requireContext())
                                .load(uri)
                                .into(binding.imageProfile)

                            // Simpan URL ke tag sementara
                            binding.imageProfile.tag = uri.toString()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }


    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

}