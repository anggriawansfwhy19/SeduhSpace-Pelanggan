package com.anggriawans.userseduhspace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.anggriawans.userseduhspace.adapter.NotificationAdapter
import com.anggriawans.userseduhspace.databinding.FragmentNotificationBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NotificationBottomFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBottomBinding

    // Add companion object to create new instance with arguments
    companion object {
        private const val ARG_ORDER_DETAILS = "order_details"

        fun newInstance(orderDetails: String): NotificationBottomFragment {
            val fragment = NotificationBottomFragment()
            val args = Bundle()
            args.putString(ARG_ORDER_DETAILS, orderDetails)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBottomBinding.inflate(layoutInflater, container, false)

        // Get order details from arguments
        val orderDetails = arguments?.getString(ARG_ORDER_DETAILS)
            ?: "Your order has been successfully placed"

        // Create more detailed notification message
        val notificationMessage = """
            Congratulations! Your order has been successfully checked out.
            
            $orderDetails
            
            Please wait patiently while our staff prepares your order. 
            You'll be notified when your order is ready.
            
            Thank you for choosing our coffee shop!
        """.trimIndent()

        val notifications = listOf(notificationMessage)
        val notificationImages = listOf(R.drawable.congrats)

        val adapter = NotificationAdapter(
            ArrayList(notifications),
            ArrayList(notificationImages)
        )

        binding.notificationRV.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationRV.adapter = adapter

        return binding.root
    }
}