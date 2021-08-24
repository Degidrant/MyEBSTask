package com.flexeiprata.androidmytaskapplication.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.flexeiprata.androidmytaskapplication.MainActivity
import com.flexeiprata.androidmytaskapplication.R
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentBinding
import com.flexeiprata.androidmytaskapplication.ui.adapter.DescUIRecyclerAdapter
import com.flexeiprata.androidmytaskapplication.ui.main.DescViewModel
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

    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        (activity as MainActivity).supportActionBar?.let {
            it.apply {
                title = ""
                setHomeAsUpIndicator(R.drawable.ns_arrow_back)
                setDisplayHomeAsUpEnabled(true)
            }
        }
        binding.buttonAddToCard.setOnClickListener {
            viewModel.addToCart(product)
            Toast.makeText(
                requireContext(),
                "${product.name} has been added to cart!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (!args.isFav) inflater.inflate(R.menu.main_menu, menu)
        else inflater.inflate(R.menu.main_menu_favorite, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favourite -> {
                lifecycleScope.launch(Dispatchers.IO) {
                    val checker = viewModel.getFavById(product.id).first() == null
                    if (checker) {
                        viewModel.insertFav(product)
                        withContext(Dispatchers.Main) {
                            item.setIcon(R.drawable.ns_favorite_full)
                        }
                    } else {
                        viewModel.deleteFav(product)
                        withContext(Dispatchers.Main) {
                            item.setIcon(R.drawable.ns_like)
                        }
                    }
                }
            }
            else -> findNavController().popBackStack()
        }
        return true
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
                        }
                        Status.ERROR -> {
                            Toast.makeText(requireContext(), "Loading Error", Toast.LENGTH_SHORT)
                                .show()
                        }
                        Status.LOADING -> {
                        }
                    }
                }
            }
        )
    }

    private fun updateUI(product: Product) {
        binding.apply {
            this@DescFragment.product = product
            recyclerUIDesc.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = DescUIRecyclerAdapter(product, this@DescFragment)
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