package com.anggriawans.userseduhspace.Fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.anggriawans.userseduhspace.MenuBottomSheetFragment
import com.anggriawans.userseduhspace.R
import com.anggriawans.userseduhspace.adapter.MenuAdapter
import com.anggriawans.userseduhspace.databinding.FragmentHomeBinding
import com.anggriawans.userseduhspace.model.MenuItem
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>

    private val fadeIn: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
    }

    private val fadeOut: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        setupImageSlider()
        retrieveAndDisplayPopularItems()
    }

    private fun setupClickListeners() {
        binding.viewAllMenu.setOnClickListener {
            showMenuBottomSheet()
        }

        binding.surveyContainer.setOnClickListener {
            animateRecommendationButton()
        }
    }

    private fun showMenuBottomSheet() {
        val bottomSheetDialog = MenuBottomSheetFragment()
        bottomSheetDialog.show(parentFragmentManager, "MenuBottomSheet")
    }

    private fun animateRecommendationButton() {
        binding.surveyBtn.startAnimation(fadeOut)
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                if (isAdded) {
                    showRecommendationConfirmationDialog()
                    binding.surveyBtn.startAnimation(fadeIn)
                }
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun showRecommendationConfirmationDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Konfirmasi")
            setMessage("Apakah Anda ingin melanjutkan untuk mencari rekomendasi kopi untuk Anda?")
            setPositiveButton("OKE") { _, _ ->
                showLoadingAndNavigateToSurvey()
            }
            setNegativeButton("TIDAK", null)
            create()
            show()
        }
    }

    private fun showLoadingAndNavigateToSurvey() {
        val progressBar = ProgressDialog.show(
            requireContext(),
            "Memuat",
            "Harap tunggu...",
            true
        )

        Handler().postDelayed({
            if (isAdded) {
                progressBar.dismiss()
                navigateToSurveyFragment()
            }
        }, 3000)
    }

    private fun navigateToSurveyFragment() {
        findNavController().navigate(R.id.action_homeFragment_to_surveyFragment)
    }

    private fun setupImageSlider() {
        val imageList = listOf(
            SlideModel(R.drawable.banner1, ScaleTypes.FIT),
            SlideModel(R.drawable.banner2, ScaleTypes.FIT),
            SlideModel(R.drawable.banner3, ScaleTypes.FIT),
            SlideModel(R.drawable.banner4, ScaleTypes.FIT),
            SlideModel(R.drawable.banner5, ScaleTypes.FIT)
        )

        with(binding.imageSlider) {
            setImageList(imageList)
            setItemClickListener(object : ItemClickListener {
                override fun doubleClick(position: Int) {
                    showToast("Double Clicked on Image ${position + 1}")
                }
                override fun onItemSelected(position: Int) {
                    showToast("Selected Image ${position + 1}")
                }
            })
        }
    }

    private fun retrieveAndDisplayPopularItems() {
        val coffeeRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        coffeeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded) return
                menuItems.clear()
                for (coffeeSnapshot in snapshot.children) {
                    coffeeSnapshot.getValue(MenuItem::class.java)?.let { menuItems.add(it) }
                }
                displayRandomPopularItems()
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Gagal memuat menu: ${error.message}")
            }
        })
    }

    private fun displayRandomPopularItems() {
        val shuffledIndices = menuItems.indices.toList().shuffled()
        val subsetMenuItem = shuffledIndices.take(10).map { menuItems[it] }
        setupPopularItemsAdapter(subsetMenuItem)
    }

    private fun setupPopularItemsAdapter(items: List<MenuItem>) {
        binding.PopularRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = MenuAdapter(items, requireContext())
            setHasFixedSize(true)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        binding.imageSlider.stopSliding()
        super.onDestroyView()
    }
}