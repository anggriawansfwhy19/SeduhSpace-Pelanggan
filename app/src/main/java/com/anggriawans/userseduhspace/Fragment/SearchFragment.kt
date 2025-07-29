package com.anggriawans.userseduhspace.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.anggriawans.userseduhspace.adapter.MenuAdapter
import com.anggriawans.userseduhspace.databinding.FragmentSearchBinding
import com.anggriawans.userseduhspace.model.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter
    private lateinit var database: FirebaseDatabase
    private val originalMenuItems = mutableListOf<MenuItem>()
    private var isDataLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize components
        database = FirebaseDatabase.getInstance()

        // Setup search view
        setUpSearchView()

        // Load data only if not already loaded
        if (!isDataLoaded) {
            retrieveMenuItem()
        }
    }

    private fun retrieveMenuItem() {
        val coffeeReference: DatabaseReference = database.reference.child("menu")
        coffeeReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                originalMenuItems.clear()
                for (coffeeSnapshot in snapshot.children) {
                    val menuItem = coffeeSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { originalMenuItems.add(it) }
                }
                isDataLoaded = true
                if (isAdded && view != null) {
                    showAllMenu()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                if (isAdded) {
                    // Handle error appropriately
                }
            }
        })
    }

    private fun showAllMenu() {
        val filteredMenuItem = ArrayList(originalMenuItems)
        setAdapter(filteredMenuItem)
    }

    private fun setAdapter(filteredMenuItem: List<MenuItem>) {
        if (!isAdded || context == null) return

        adapter = MenuAdapter(filteredMenuItem, requireContext())
        binding.menuSearchRV.layoutManager = LinearLayoutManager(requireContext())
        binding.menuSearchRV.adapter = adapter
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItems(newText)
                return true
            }
        })
    }

    private fun filterMenuItems(query: String) {
        if (!isAdded) return

        val filteredMenuItem = if (query.isEmpty()) {
            originalMenuItems
        } else {
            originalMenuItems.filter {
                it.coffeeName?.contains(query, ignoreCase = true) == true
            }
        }
        setAdapter(filteredMenuItem)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear references to avoid memory leaks
        binding.menuSearchRV.adapter = null
    }
}