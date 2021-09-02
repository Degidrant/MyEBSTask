package com.flexeiprata.androidmytaskapplication.ui.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
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
import com.flexeiprata.androidmytaskapplication.utils.ScreenshotHelper
import com.flexeiprata.androidmytaskapplication.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import eu.bolt.screenshotty.ScreenshotBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class DescFragment : Fragment() {

    private var _binding: DescFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DescViewModel by viewModels()
    private val args: DescFragmentArgs by navArgs()

    private lateinit var requestLauncherMessages: ActivityResultLauncher<String>
    private lateinit var requestLauncherScreenshots: ActivityResultLauncher<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requestLauncherMessages = RequestPermissionsHelper.requestInstanceDefault(this) {
            findNavController().navigate(
                DescFragmentDirections.actionDescFragmentToContactFragment(
                    "link",
                    "name"
                )
            )
        }
        requestLauncherScreenshots = RequestPermissionsHelper.requestInstanceWithElse(this,
            {
                createBottomDialog(true)
            },
            {
                createBottomDialog(false)
            }
        )
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
                createBottomDialog(true)
            }
        }
    }

    private fun createBottomDialog(isRequested: Boolean) {
        ContactChooserBottomSheetDialog.getInstance().apply {
            setTextColor(false)
            setMessageAction {
                RequestPermissionsHelper.requestPermission(
                    requireContext(),
                    Manifest.permission.READ_CONTACTS,
                    requestLauncherMessages,
                    {
                        findNavController().navigate(
                            DescFragmentDirections.actionDescFragmentToContactFragment(
                                "link",
                                "name"
                            )
                        )
                    }
                )
            }
            if (isRequested) RequestPermissionsHelper.requestPermission(
                this@DescFragment.requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                requestLauncherScreenshots,
                {
                    val screenshotHelper =
                        ScreenshotHelper(this@DescFragment.requireActivity())
                    screenshotHelper.screenshotHelperFun {
                        val bitmap = (it as ScreenshotBitmap).bitmap
                        setScreenshotByFun(bitmap)
                        screenshotHelper.proceedScreenshotSave(screenshot)
                        setTextColor(true)
                        this.show(this@DescFragment.parentFragmentManager, "tag")
                    }
                    setScreenshotAction {
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = "image/png"
                        val bmpUri = screenshotHelper.getUriFromScreenshot()
                        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
                        val message = String.format(
                            this@DescFragment.getString(R.string.share_message),
                            "title",
                            "link"
                        )
                        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
                        startActivity(
                            Intent.createChooser(
                                shareIntent,
                                this@DescFragment.getString(R.string.share)
                            )
                        )
                    }
                }
            ) else {
                this.show(this@DescFragment.parentFragmentManager, "tag")
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