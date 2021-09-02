package com.flexeiprata.androidmytaskapplication.ui.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.flexeiprata.androidmytaskapplication.R
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentBinding
import com.flexeiprata.androidmytaskapplication.ui.adapter.DesciptionAdapterUI
import com.flexeiprata.androidmytaskapplication.ui.dialog.ContactChooserBottomSheetDialog
import com.flexeiprata.androidmytaskapplication.ui.main.DescViewModel
import com.flexeiprata.androidmytaskapplication.ui.models.uimodels.DescUIModel
import com.flexeiprata.androidmytaskapplication.utils.RequestPermissionsHelper
import com.flexeiprata.androidmytaskapplication.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import eu.bolt.screenshotty.ScreenshotBitmap
import eu.bolt.screenshotty.ScreenshotManagerBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


@AndroidEntryPoint
class DescFragment : Fragment() {

    private var _binding: DescFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DescViewModel by viewModels()
    private val args: DescFragmentArgs by navArgs()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requestPermissionLauncher = RequestPermissionsHelper.requestInstance(this, {
            findNavController().navigate(
                DescFragmentDirections.actionDescFragmentToContactFragment(
                    "link",
                    "name"
                )
            )
        }, {
            Toast.makeText(
                requireContext(),
                "Sorry, unable without permission",
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DescFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        binding.buttonAddToCard.setOnClickListener {
            viewModel.addToCart(requireContext())
        }
        binding.apply {

            mainToolbar.setOptionOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    val checker = viewModel.getFavById(args.id).first() == null
                    if (checker) {
                        viewModel.insertFav()
                        withContext(Dispatchers.Main) {
                            mainToolbar.setOptionImage(R.drawable.ns_favorite_full)
                        }
                    } else {
                        viewModel.deleteFav()
                        withContext(Dispatchers.Main) {
                            mainToolbar.setOptionImage(R.drawable.ns_like)
                        }
                    }
                }
            }
            mainToolbar.setHomeOnClickListener {
                findNavController().popBackStack()
            }
            mainToolbar.setSecondOptionOnClickListener {

                when {
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_CONTACTS
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        val dialog = ContactChooserBottomSheetDialog.getInstance().apply {
                            setMessageAction {
                                findNavController().navigate(
                                    DescFragmentDirections.actionDescFragmentToContactFragment(
                                        "link",
                                        "name"
                                    )
                                )
                            }
                            val screenshotManager =
                                ScreenshotManagerBuilder(this@DescFragment.requireActivity()).build()
                            val screenshotResult = screenshotManager.makeScreenshot()
                            screenshotResult.observe(
                                onSuccess = {
                                    setScreenshotByFun(
                                        (it as ScreenshotBitmap).bitmap
                                    )
                                    val file = File(
                                        this@DescFragment.requireContext()
                                            .getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                        "screen.png"
                                    )
                                    val out = FileOutputStream(file)
                                    screenshot?.compress(Bitmap.CompressFormat.PNG, 90, out)
                                    out.close()
                                    this.show(this@DescFragment.parentFragmentManager, "tag")
                                },
                                onError = {}
                            )
                            setScreenshotAction {
                                val shareIntent = Intent(Intent.ACTION_SEND)
                                shareIntent.type = "image/png"
                                val bmpUri = FileProvider.getUriForFile(
                                    this@DescFragment.requireContext(),
                                    "com.flexeiprata.androidmytaskapplication",
                                    File(
                                        this@DescFragment.requireContext()
                                            .getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                        "screen.png"
                                    )
                                )
                                shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
                                shareIntent.putExtra(
                                    Intent.EXTRA_TEXT,
                                    String.format(
                                        this@DescFragment.getString(R.string.share_message),
                                        "title",
                                        "link"
                                    )
                                )
                                startActivity(
                                    Intent.createChooser(
                                        shareIntent,
                                        "Share image using"
                                    )
                                )
                            }
                        }
                    }
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {

                    }
                    else -> {
                        requestPermissionLauncher.launch(
                            Manifest.permission.READ_CONTACTS
                        )
                    }
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.getProductsById(args.id).observe(
            viewLifecycleOwner,
            {
                it?.let {
                    when (it.status) {
                        Status.SUCCESS -> {
                            it.data?.let { productData ->
                                updateUI(productData)
                            }
                            binding.progressBarLoading.visibility = View.GONE
                        }
                        Status.ERROR -> {
                            Toast.makeText(requireContext(), "Loading Error", Toast.LENGTH_SHORT)
                                .show()
                        }
                        Status.LOADING -> {
                            binding.progressBarLoading.visibility = View.VISIBLE
                        }
                    }
                }
            }
        )
    }

    private fun updateUI(listOfModels: MutableList<DescUIModel>) {
        binding.apply {
            recyclerUIDesc.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = DesciptionAdapterUI(listOfModels)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
            lifecycleScope.launch(Dispatchers.IO) {
                val checker = viewModel.getFavById(args.id).first() == null
                val image =
                    if (!checker) R.drawable.ns_favorite_full
                    else R.drawable.ns_like
                withContext(Dispatchers.Main) {
                    mainToolbar.setOptionImage(image)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}