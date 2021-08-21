package com.flexeiprata.androidmytaskapplication.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.flexeiprata.androidmytaskapplication.MainActivity
import com.flexeiprata.androidmytaskapplication.R
import com.flexeiprata.androidmytaskapplication.data.api.ApiHelper
import com.flexeiprata.androidmytaskapplication.data.api.RetrofitBuilder
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentBinding
import com.flexeiprata.androidmytaskapplication.temporary.FavoritesTemp
import com.flexeiprata.androidmytaskapplication.ui.base.DescViewModelFactory
import com.flexeiprata.androidmytaskapplication.ui.main.DescViewModel
import com.flexeiprata.androidmytaskapplication.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DescFragment : Fragment() {

    private var _binding: DescFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DescViewModel
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
        binding.apply {
            scroller.visibility = View.INVISIBLE
        }
        setupViewModel()
        setupObservers()
        (activity as MainActivity).supportActionBar?.let {
            it.apply {
                title = ""
                setHomeAsUpIndicator(R.drawable.ns_arrow_back)
                setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            DescViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(
            DescViewModel::class.java
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (!args.isFav) inflater.inflate(R.menu.main_menu, menu)
        else inflater.inflate(R.menu.main_menu_favorite, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favourite -> {
                if (FavoritesTemp.favoriteList.contains(product)) {
                    FavoritesTemp.favoriteList.remove(product)
                    item.setIcon(R.drawable.ns_like)
                } else {
                    FavoritesTemp.favoriteList.add(product)
                    item.setIcon(R.drawable.ns_favorite_full)
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
                                binding.scroller.visibility = View.VISIBLE
                            }
                        }
                        Status.ERROR -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
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
            textViewName.text = product.name
            textViewDesc.text = String.format("%1s\n%2s", product.size, product.colour)
            val pricePlace = String.format("$%d,-", product.price)
            textViewPrice.text = pricePlace
            textViewPriceSmall.text = pricePlace
            textViewFullDesc.text = product.details
            lifecycleScope.launch(Dispatchers.IO) {
                val glide = Glide.with(mainPhotoImage.context)
                    .load(product.category.icon)
                withContext(Dispatchers.Main) {
                    glide.into(mainPhotoImage)
                    //mainPhotoImage.startAnimation(getFadeInAnimation(500))
                }
            }

            buttonAddToCard.setOnClickListener {
                FavoritesTemp.cart.add(product)
                FavoritesTemp.cartObserver.postValue(FavoritesTemp.cart)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}