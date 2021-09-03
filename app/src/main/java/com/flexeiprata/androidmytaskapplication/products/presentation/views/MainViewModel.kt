package com.flexeiprata.androidmytaskapplication.products.presentation.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.flexeiprata.androidmytaskapplication.cart.presentation.AddToCartUseCase
import com.flexeiprata.androidmytaskapplication.cart.presentation.ClearCartUseCase
import com.flexeiprata.androidmytaskapplication.cart.presentation.GetCartUseCase
import com.flexeiprata.androidmytaskapplication.common.PAGE_SIZE
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.DeleteFavUseCase
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.GetAllFavsUseCase
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.InsertFavUseCase
import com.flexeiprata.androidmytaskapplication.favorites.presentation.view.FavResult
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import com.flexeiprata.androidmytaskapplication.products.domain.MainDataSource
import com.flexeiprata.androidmytaskapplication.products.presentation.adapter.ProductsAdapter
import com.flexeiprata.androidmytaskapplication.products.presentation.uimodels.ProductPayloads
import com.flexeiprata.androidmytaskapplication.products.presentation.uimodels.ProductUIModel
import com.flexeiprata.androidmytaskapplication.products.presentation.usecases.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getAllFavsUseCase: GetAllFavsUseCase,
    private val insertFavUseCase: InsertFavUseCase,
    private val deleteFavUseCase: DeleteFavUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    private var mState = MutableStateFlow<ProductResult>(ProductResult.Loading(emptyList()))
    val state get() = mState.asStateFlow()

    private var mCartState = MutableStateFlow<FavResult>(FavResult.Loading(emptyList()))
    val cartState get() = mCartState.asStateFlow()

    private var mFavState = MutableStateFlow<FavResult>(FavResult.Loading(emptyList()))
    val favState get() = mFavState.asStateFlow()

    fun initialize(text: String){
        viewModelScope.launch {
            val listFav = getAllFavsUseCase().first()
            listData(text, listFav).collectLatest {
                mState.value = ProductResult.Success(it)
            }
            getAllFavsUseCase().collectLatest {
                mFavState.value = FavResult.Success(it)
            }
        }
    }

    private fun listData(text: String, listFav: List<Product>) = Pager(PagingConfig(PAGE_SIZE)) {
        MainDataSource(getProductsUseCase, text)
    }
        .flow.cachedIn(viewModelScope).map { pagingData ->
            pagingData.map { product ->
                val checker = listFav.contains(product)
                ProductUIModel(product, checker)
            }
        }

    fun insertFav(fav: Product) = viewModelScope.launch {
        insertFavUseCase(fav)
    }

    fun deleteFav(fav: Product) = viewModelScope.launch {
        deleteFavUseCase(fav.id)
    }

    fun getCart() {
        viewModelScope.launch {
            getCartUseCase().collectLatest { collector ->
                val list = mutableListOf<Product>()
                collector.forEach {
                    list.add(it.product)
                }
                mCartState.value = FavResult.Success(list)
            }
        }
    }

    fun clearCart() = viewModelScope.launch {
        clearCartUseCase()
    }

    fun addToCart(product: Product) = viewModelScope.launch {
        addToCartUseCase(product)
    }

    fun findItemInFav(
        inListItem: ProductUIModel,
        favList: List<Product>,
        mainList: List<ProductUIModel>,
        adapter: ProductsAdapter
    ): Boolean {
        var guarantee = false
        favList.forEach { inFavItem ->
            if (inListItem.product.id == inFavItem.id && !inListItem.isFav) {
                inListItem.isFav = true
                adapter.notifyItemChanged(
                    mainList.indexOf(inListItem),
                    mutableListOf(ProductPayloads.FavChanged(true))
                )
                return true
            }
            if (inListItem.product.id == inFavItem.id) guarantee = true
        }
        return guarantee
    }

}