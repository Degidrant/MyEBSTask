package com.flexeiprata.androidmytaskapplication.ui.dialog

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flexeiprata.androidmytaskapplication.databinding.ContactChooserBfBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ContactChooserBottomSheetDialog() : BottomSheetDialogFragment() {

    private var _binding: ContactChooserBfBinding? = null
    private val binding get() = _binding!!

    companion object{
        fun getInstance() = ContactChooserBottomSheetDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactChooserBfBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewSMS.setOnClickListener {
            sendMessage?.invoke()
            dismiss()
        }
        binding.textViewScreenshot.setOnClickListener {
            sendScreenshot?.invoke()
            dismiss()
        }
    }

    private var sendMessage: (() -> Unit)? = null
    private var sendScreenshot: (() -> Unit)? = null
    var screenshot: Bitmap? = null

    fun setMessageAction(action: () -> Unit){
        sendMessage = action
    }

    fun setScreenshotAction(action: () -> Unit){
        sendScreenshot = action
    }

    fun setScreenshotByFun(drawable: Bitmap){
        screenshot = drawable
    }


}