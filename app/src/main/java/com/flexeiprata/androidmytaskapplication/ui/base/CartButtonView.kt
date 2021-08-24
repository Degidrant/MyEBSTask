package com.flexeiprata.androidmytaskapplication.ui.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.flexeiprata.androidmytaskapplication.databinding.CartButtonBinding

class CartButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var _binding: CartButtonBinding? =
        CartButtonBinding.inflate(LayoutInflater.from(context), this)
    private val binding get() = _binding!!

    fun setCounter(i: Int){
        binding.textViewCartSize.text = i.toString()
    }

    fun setIcon(drawable: Drawable?){
        binding.imageViewCartIcon.setImageDrawable(drawable)
    }

    override fun onDetachedFromWindow() {
        _binding = null
        super.onDetachedFromWindow()
    }

}