package com.anggriawans.userseduhspace

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.anggriawans.userseduhspace.Fragment.HistoryFragment
import com.anggriawans.userseduhspace.databinding.FragmentCongratsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CongratsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentCongratsBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCongratsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tombol "Go Home" - Navigasi ke MainActivity
        binding.goHome.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            dismiss()
        }

        // Tombol "View Order" - Buka RecentOrderItemsActivity
        binding.viewOrder.setOnClickListener {
            val intent = Intent(requireContext(), HistoryFragment::class.java)
            startActivity(intent)
            dismiss()
        }

        // TextView "Need Help?" - Buka WhatsApp
        val whatsappUrl = "https://wa.me/6289698058946"
        val needHelpText1: TextView = binding.needHelp
        val needHelpText2: TextView = binding.root.findViewById(R.id.needHelp) // second clickable

        val openWhatsApp = View.OnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl))
            startActivity(intent)
        }

        needHelpText1.setOnClickListener(openWhatsApp)
        needHelpText2.setOnClickListener(openWhatsApp)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
