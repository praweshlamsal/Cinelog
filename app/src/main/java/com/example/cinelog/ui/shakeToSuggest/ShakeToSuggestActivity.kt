package com.example.cinelog.ui.shakeToSuggest

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.*
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.cinelog.R
import com.example.cinelog.data.remote.network.RetrofitClient
import com.example.cinelog.data.repository.MovieRepository
import com.example.cinelog.databinding.ActivityShakeToSuggestBinding
import com.example.cinelog.model.Movie
import com.example.cinelog.viewModel.MovieViewModel
import com.example.cinelog.viewModel.MovieViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

class ShakeToSuggestActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityShakeToSuggestBinding
    private var sensorManager: SensorManager? = null
    private var lastShakeTime: Long = 0
    private lateinit var viewModel: MovieViewModel
    private var vibrator: Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShakeToSuggestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieRepository = MovieRepository(
            apiService = RetrofitClient.apiService,
            db = FirebaseFirestore.getInstance()
        )
        val factory = MovieViewModelFactory.MovieViewModelFactory(movieRepository)
        viewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        startInitialAnimation()

        viewModel.randomMovie.observe(this) { movie ->
            updateMovieUI(movie)
        }

        binding.btRetry.setOnClickListener {
            resetProcess()
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(
            this,
            sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val acceleration = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta = acceleration - SensorManager.GRAVITY_EARTH

            if (delta > 8) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastShakeTime > 1000) {
                    lastShakeTime = currentTime
                    onShakeDetected()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun startInitialAnimation() {
        binding.lavCatInHole.visibility = View.VISIBLE
        binding.lavShakeDevice.visibility = View.GONE
        binding.lavCatInBottom.visibility = View.GONE
        binding.tvShakeYourPhone.visibility = View.VISIBLE
        binding.movieCard.visibility = View.GONE
        binding.btRetry.visibility = View.GONE
        binding.lavScream.visibility = View.GONE

        binding.lavCatInHole.postDelayed({
            binding.lavCatInHole.visibility = View.GONE
            binding.lavCatInBottom.visibility = View.VISIBLE
            binding.lavShakeDevice.visibility = View.VISIBLE
        }, 2000)
    }

    private fun onShakeDetected() {
        binding.lavShakeDevice.visibility = View.GONE
        binding.lavCatInBottom.visibility = View.GONE
        binding.tvShakeYourPhone.visibility = View.GONE
        binding.movieCard.visibility = View.GONE
        binding.btRetry.visibility = View.GONE

        binding.lavScream.apply {
            visibility = View.VISIBLE
            progress = 0.4f
            playAnimation()
        }

        vibratePhone()

        viewModel.fetchRandomMovie()

        binding.lavScream.postDelayed({
            binding.lavScream.visibility = View.GONE
            animateCardView()
        }, 5000)
    }

    private fun updateMovieUI(movie: Movie) {
        binding.movieTitle.text = movie.title
        binding.movieOverview.text = movie.type
        binding.movieReleaseDate.text = "Release Date: ${movie.year}"

        Glide.with(this)
            .load(movie.poster)
            .placeholder(R.drawable.ic_movie)
            .into(binding.movieImage)
    }

    private fun animateCardView() {
        binding.movieCard.visibility = View.VISIBLE
        binding.btRetry.visibility = View.VISIBLE

        val fadeIn = ObjectAnimator.ofFloat(binding.movieCard, "alpha", 0f, 1f)
        val slideUp = ObjectAnimator.ofFloat(binding.movieCard, "translationY", 100f, 0f)

        fadeIn.duration = 700
        slideUp.duration = 700
        slideUp.interpolator = DecelerateInterpolator()

        AnimatorSet().apply {
            playTogether(fadeIn, slideUp)
            start()
        }
    }

    private fun resetProcess() {
        binding.movieCard.visibility = View.GONE
        binding.btRetry.visibility = View.GONE

        binding.lavScream.apply {
            visibility = View.GONE
            progress = 0.4f
            cancelAnimation()
        }

        // Make lavCatInHole invisible and show lavShakeDevice & lavCatInBottom
        binding.lavCatInHole.visibility = View.GONE
        binding.lavShakeDevice.visibility = View.VISIBLE
        binding.lavCatInBottom.visibility = View.VISIBLE
        binding.tvShakeYourPhone.visibility = View.VISIBLE
    }

    private fun vibratePhone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator?.vibrate(300)
        }
    }
}
