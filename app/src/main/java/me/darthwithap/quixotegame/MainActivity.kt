package me.darthwithap.quixotegame

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import me.darthwithap.quixotegame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var animator: ObjectAnimator
    private val start = 0f
    private val end = 360f
    private var isAnimating = false
    private var isPaused = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.inclinationLiveData.observe({ lifecycle }) {
            it?.let {
                binding.tvInclination.text = it.toString()
                // HORIZONTAL CASE (On a Flat surface)
                if (it < 15 || it > 165) {
                    enableButtons()
                    if (isPaused) resumeAnimatingArrow()
                    if (!isAnimating) startAnimatingArrow()
                }
                // VERTICAL CASE (Perpendicular to Flat Surface)
                else if (it in (80..100)) {
                    disableButtons()
                    cancelAnimatingArrow()
                    binding.ivArrow.rotation = 270f
                } else {
                    disableButtons()
                    pauseAnimatingArrow()
                    resumeAnimatingArrow()
                    pauseAnimatingArrow()
                }
            }
        }
        initAnimator()
        setContentView(binding.root)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun getTouchListener(rotationAngle: Float): View.OnTouchListener {
        return object : TouchListener() {
            override fun onSingleTouch() {
                animator.pause()
                binding.ivArrow.rotation = rotationAngle
            }

            override fun onDoubleTouch() {
                animator.pause()
                binding.ivArrow.rotation = rotationAngle + 180f
            }

            override fun onRelease() {
                resumeAnimatingArrow()
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun enableButtons() {
        with(binding) {
            btnTopLeft.setOnTouchListener(getTouchListener(240f))
            btnTopRight.setOnTouchListener(getTouchListener(295f))
            btnBottomRight.setOnTouchListener(getTouchListener(60f))
            btnBottomLeft.setOnTouchListener(getTouchListener(115f))
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun disableButtons() {
        with(binding) {
            btnTopLeft.setOnTouchListener(null)
            btnTopRight.setOnTouchListener(null)
            btnBottomRight.setOnTouchListener(null)
            btnBottomLeft.setOnTouchListener(null)
        }
    }

    private fun initAnimator() {
        animator = ObjectAnimator.ofFloat(binding.ivArrow, View.ROTATION, start, end)
        animator.apply {
            duration = 10 * 1000
            interpolator = LinearInterpolator()
            repeatCount = Animation.INFINITE
        }
    }

    private fun startAnimatingArrow() {
        isAnimating = true
        animator.start()
    }

    private fun pauseAnimatingArrow() {
        isPaused = true
        animator.pause()
    }

    private fun resumeAnimatingArrow() {
        isPaused = false
        animator.resume()
    }

    private fun cancelAnimatingArrow() {
        isAnimating = false
        animator.cancel()
    }
}