package com.flexeiprata.androidmytaskapplication.ui.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.flexeiprata.androidmytaskapplication.R
import com.flexeiprata.androidmytaskapplication.databinding.CustomToolbarBinding

class MainCustomToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = CustomToolbarBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MainCustomToolbar,
            0, 0
        ).apply {
            try {
                binding.apply {
                    val image1 = getResourceId(R.styleable.MainCustomToolbar_homeIcon, 0)
                    val image2 = getResourceId(R.styleable.MainCustomToolbar_secondHomeIcon, 0)
                    val image3 = getResourceId(R.styleable.MainCustomToolbar_optionIcon, 0)
                    val imageCenter = getResourceId(R.styleable.MainCustomToolbar_centralPicture, 0)
                    if (image1 != 0) imageViewToolbar1.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            image1
                        )
                    )
                    if (image2 != 0) imageViewToolbar2.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            image2
                        )
                    )
                    if (image3 != 0) imageViewToolbar3.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            image3
                        )
                    )
                    if (imageCenter != 0) toolbarMainView.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            imageCenter
                        )
                    )
                }
            } finally {
                recycle()
            }
        }
    }

    fun setHomeOnClickListener(onClickListener: (View) -> Unit){
        binding.imageViewToolbar1.setOnClickListener(onClickListener)
    }

    fun setSecondHoneOnClickListener(onClickListener: (View) -> Unit){
        binding.imageViewToolbar2.setOnClickListener(onClickListener)
    }

    fun setOptionOnClickListener(onClickListener: (View) -> Unit){
        binding.imageViewToolbar3.setOnClickListener(onClickListener)
    }

    fun setOptionImage(drawable: Drawable?){
        binding.imageViewToolbar3.setImageDrawable(drawable)
    }

    fun setOptionImage(res: Int){
        binding.imageViewToolbar3.setImageDrawable(AppCompatResources.getDrawable(context, res))
    }

}