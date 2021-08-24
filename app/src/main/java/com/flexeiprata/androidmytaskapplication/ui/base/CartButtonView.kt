package com.flexeiprata.androidmytaskapplication.ui.base

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.flexeiprata.androidmytaskapplication.R
import com.flexeiprata.androidmytaskapplication.databinding.CartButtonBinding

class CartButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var binding: CartButtonBinding =
        CartButtonBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CartButtonView,
            0, 0
        ).apply {
            try {
                binding.apply {
                    val image1 = getResourceId(R.styleable.CartButtonView_leftIcon, 0)
                    val text = getResourceId(R.styleable.CartButtonView_mainText, 0)
                    if (image1 != 0) imageViewCartIcon.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            image1
                        )
                    )
                    if (text != 0) cartButtonBody.setText(text)
                }
            } finally {
                recycle()
            }
        }
    }

    fun setCounter(i: Int){
        binding.textViewCartSize.text = i.toString()
    }

}