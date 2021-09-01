package com.flexeiprata.androidmytaskapplication.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.flexeiprata.androidmytaskapplication.ui.main.DescViewModel
import com.flexeiprata.androidmytaskapplication.ui.models.uimodels.DescUIModel
import com.flexeiprata.androidmytaskapplication.utils.Status
import dagger.hilt.android.AndroidEntryPoint
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
            try {
                viewModel.addToCart()
                Toast.makeText(
                    requireContext(),
                    "Product has been successfully added to cart!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            catch (ex: UninitializedPropertyAccessException){
                ex.printStackTrace()
            }

        }
        binding.apply {
            val image =
                if (args.isFav) R.drawable.ns_favorite_full
                else R.drawable.ns_like
            mainToolbar.setOptionImage(image)
            mainToolbar.setOptionOnClickListener {

                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
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
                        catch (ex: UninitializedPropertyAccessException){
                            ex.printStackTrace()
                        }
                    }
                }
            mainToolbar.setHomeOnClickListener {
                findNavController().popBackStack()
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}