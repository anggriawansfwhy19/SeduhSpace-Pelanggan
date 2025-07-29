package com.anggriawans.userseduhspace

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {

    // Deklarasi ProgressBar
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Inisialisasi ProgressBar
        progressBar = findViewById(R.id.progressBar)

        // Reset progress awal
        progressBar.progress = 0

        // Animasi isi progress bar dari 0 ke 100
        val animator = ObjectAnimator.ofInt(progressBar, "progress", 0, 200)
        animator.duration = 4000  // 5 detik
        animator.interpolator = DecelerateInterpolator()
        animator.start()

        // Mengatur Handler untuk menangani penundaan
        Handler(Looper.getMainLooper()).postDelayed({
            // Intent untuk pindah ke StartActivity
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish() // Menutup SplashScreen setelah StartActivity dimulai
        }, 5000) // Menunda selama 4000 milidetik (4 detik)
    }
}
