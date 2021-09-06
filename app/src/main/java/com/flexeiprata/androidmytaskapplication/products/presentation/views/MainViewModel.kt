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
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.GetAllFavsRXUseCase
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.InsertFavUseCase
import com.flexeiprata.androidmytaskapplication.favorites.presentation.view.FavResult
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import com.flexeiprata.androidmytaskapplication.products.domain.MainDataSource
import com.flexeiprata.androidmytaskapplication.products.presentation.adapter.ProductsAdapter
import com.flexeiprata.androidmytaskapplication.products.presentation.uimodels.ProductPayloads
import com.flexeiprata.androidmytaskapplication.products.presentation.uimodels.ProductUIModel
import com.flexeiprata.androidmytaskapplication.products.presentation.usecases.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getAllFavsUseCaseRX: GetAllFavsRXUseCase,
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

    private val compositeDisposable = CompositeDisposable()

    fun initialize(text: String) {
        viewModelScope.launch {
            var listFav = emptyList<Product>()
            getAllFavsUseCaseRX()
                .subscribeOn(Schedulers.io()).subscribe {
                    listFav = it
                    mFavState.value = FavResult.Success(it)
                }.also {
                    compositeDisposable.add(it)
                }
            listData(text, listFav).collectLatest {
                mState.value = ProductResult.Success(it)
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
        getCartUseCase().subscribeOn(Schedulers.io())
            .subscribe { collector ->
                val list = mutableListOf<Product>()
                collector.forEach {
                    list.add(it.product)
                }
                mCartState.value = FavResult.Success(list)
            }.also {
                compositeDisposable.add(it)
            }
    }

    fun clearCart() {
        clearCartUseCase().subscribeOn(Schedulers.io())
            .subscribe().also {
                compositeDisposable.add(it)
            }
    }

    fun addToCart(product: Product) {
        addToCartUseCase(product).subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io()).subscribe().also {
                compositeDisposable.add(it)
            }
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

    override fun onCleared() {
        compositeDisposable.apply {
            dispose()
            clear()
        }
        super.onCleared()
    }

}