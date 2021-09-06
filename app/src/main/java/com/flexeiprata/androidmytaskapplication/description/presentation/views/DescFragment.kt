package com.flexeiprata.androidmytaskapplication.description.presentation.views

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.flexeiprata.androidmytaskapplication.R
import com.flexeiprata.androidmytaskapplication.common.RequestPermissionsHelper
import com.flexeiprata.androidmytaskapplication.contacts.presentation.views.ContactChooserBottomSheetDialog
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentBinding
import com.flexeiprata.androidmytaskapplication.description.misc.ScreenshotHelper
import com.flexeiprata.androidmytaskapplication.description.presentation.uiadapter.DescriptionAdapterUI
import com.flexeiprata.androidmytaskapplication.description.presentation.views.uimodels.RowItem
import dagger.hilt.android.AndroidEntryPoint
import eu.bolt.screenshotty.ScreenshotBitmap
import kotlinx.coroutines.flow.collectLatest


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
        binding.mainToolbar.setSecondOptionOnClickListener {
            createBottomDialog(true)
        }
        binding.mainToolbar.setHomeOnClickListener {
            findNavController().popBackStack()
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
        lifecycleScope.launchWhenCreated {
            viewModel.getProductsById(args.id)
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sharedState.collectLatest {
                    when (it) {
                        is DescResult.Error -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                                .show()
                            binding.apply {
                                progressBarLoading.visibility = View.GONE
                                imageViewNoConnection.visibility = View.VISIBLE
                                textViewNoConnection.visibility = View.VISIBLE
                                buttonAddToCard.isEnabled = false
                                buttonBuyNow.isEnabled = false
                                val gray =
                                    ColorStateList.valueOf(requireContext().getColor(R.color.true_gray))
                                buttonBuyNow.backgroundTintList = gray
                                buttonAddToCard.backgroundTintList = gray
                            }

                        }
                        is DescResult.Loading -> {
                            binding.progressBarLoading.visibility = View.VISIBLE
                        }
                        is DescResult.Success -> {
                            updateUI(it.data)
                            binding.progressBarLoading.visibility = View.GONE
                            createFreeOnClickListeners()
                        }
                    }
                }
            }
        }

    }

    private fun createFreeOnClickListeners() {
        binding.buttonAddToCard.setOnClickListener {
            viewModel.addToCart()
        }
        binding.apply {

            mainToolbar.setOptionOnClickListener {
                viewModel.checkIfIsFav(id).subscribe { product ->
                    val checker = (product == null)
                    if (checker) {
                        viewModel.insertFav()
                        mainToolbar.setOptionImage(R.drawable.ns_favorite_full)

                    } else {
                        viewModel.deleteFav()
                        mainToolbar.setOptionImage(R.drawable.ns_like)
                    }
                }
            }
        }
    }

    private fun updateUI(listOfModels: List<RowItem?>) {
        binding.apply {
            recyclerUIDesc.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = DescriptionAdapterUI().apply {
                    submitList(listOfModels)
                }
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
            viewModel.checkIfIsFav(id).subscribe { product ->
                val checker = (product == null)
                val image =
                    if (!checker) R.drawable.ns_favorite_full
                    else R.drawable.ns_like
                mainToolbar.setOptionImage(image)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}