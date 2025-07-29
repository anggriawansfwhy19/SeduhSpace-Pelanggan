package com.anggriawans.userseduhspace

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.animation.ObjectAnimator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.anggriawans.userseduhspace.adapter.ImagePagerAdapter
import com.anggriawans.userseduhspace.databinding.ActivityStartBinding
import com.google.android.material.tabs.TabLayout

class StartActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var tabDots: TabLayout
    private val binding: ActivityStartBinding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        progressBar = findViewById(R.id.progressBarStart)
        tabDots = findViewById(R.id.tabDots)

        // Inisialisasi ViewPager
        val viewPager = binding.viewPager

        // Buat dan set adapter untuk ViewPager
        val adapter = ImagePagerAdapter(this)
        viewPager.adapter = adapter

        // Menghubungkan ViewPager dengan TabLayout untuk indikator
        tabDots.setupWithViewPager(viewPager, true)

        // Set visibilitas awal ProgressBar menjadi tidak terlihat
        progressBar.visibility = View.INVISIBLE

        // Mengatur listener untuk tombol "Next"
        binding.next.setOnClickListener {
            // Setelah tombol "Next" ditekan, atur ProgressBar menjadi terlihat
            progressBar.visibility = View.VISIBLE

            // Reset progress awal
            progressBar.progress = 0

            // Animasi isi progress bar dari 0 ke 100
            val animator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
            animator.duration = 2000  // 2 detik
            animator.interpolator = DecelerateInterpolator()
            animator.start()

            // Setelah tindakan selesai, atur ProgressBar menjadi tidak terlihat
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, ChooseLocationActivity::class.java)
                startActivity(intent)
                finish() // opsional, agar tidak bisa kembali
            }, 3000)
        }
    }
}


