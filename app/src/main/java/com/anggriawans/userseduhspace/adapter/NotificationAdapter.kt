package com.anggriawans.userseduhspace.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anggriawans.userseduhspace.databinding.NotificationItemBinding

class NotificationAdapter(
    private var notifications: ArrayList<String>,
    private var notificationImages: ArrayList<Int>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    // Add click listener interface
    interface OnNotificationClickListener {
        fun onNotificationClick(position: Int)
    }

    private var listener: OnNotificationClickListener? = null

    fun setOnNotificationClickListener(listener: OnNotificationClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotificationItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotificationViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val currentNotification = notifications[position]
        val currentNotificationImage = notificationImages[position]
        holder.bind(currentNotification, currentNotificationImage)
    }

    override fun getItemCount(): Int = notifications.size

    // Function to update notifications
    fun updateNotifications(newNotifications: List<String>, newImages: List<Int>) {
        notifications.clear()
        notifications.addAll(newNotifications)

        notificationImages.clear()
        notificationImages.addAll(newImages)

        notifyDataSetChanged()
    }

    inner class NotificationViewHolder(
        private val binding: NotificationItemBinding,
        private val listener: OnNotificationClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onNotificationClick(position)
                }
            }
        }

        fun bind(notificationText: String, notificationImageResource: Int) {
            binding.apply {
                // Enable multi-line text
                notificationTV.text = notificationText
                notificationTV.maxLines = Int.MAX_VALUE // Allow unlimited lines

                notificationImage.setImageResource(notificationImageResource)

                // Add timestamp if needed (you can add this to your data model)
                // notificationTimeTV.text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
                // notificationTimeTV.visibility = View.VISIBLE
            }
        }
    }
}