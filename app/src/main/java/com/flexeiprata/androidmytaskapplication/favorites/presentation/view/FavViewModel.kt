package com.flexeiprata.androidmytaskapplication.favorites.presentation.view

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.flexeiprata.androidmytaskapplication.cart.presentation.AddToCartUseCase
import com.flexeiprata.androidmytaskapplication.cart.presentation.ClearCartUseCase
import com.flexeiprata.androidmytaskapplication.cart.presentation.GetCartUseCase
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.*
import com.flexeiprata.androidmytaskapplication.products.data.models.Category
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import com.flexeiprata.androidmytaskapplication.products.presentation.uimodels.ProductUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(
    private val insertFavUseCase: InsertFavUseCase,
    private val deleteFavUseCase: DeleteFavUseCase,
    private val getFavByIdUseCase: GetFavByIdUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val getProductByIdUseCase: GetFavByIdUseCase,
    private val actualizeUseCase: ActualizeUseCase,
    private val getAllFavsRXUseCase: GetAllFavsRXUseCase,
    private val addToFavUseCase: InsertFavUseCase,
    private val insertFavUseCaseCo: InsertFavUseCaseCo
) : ViewModel() {

    private var mState = MutableStateFlow<FavResult>(FavResult.Loading(emptyList()))
    val stateInfo get() = mState.asStateFlow()

    private var mCartState = MutableStateFlow<FavResult>(FavResult.Loading(emptyList()))
    val cartStateInfo get() = mCartState.asStateFlow()

    private var listForActualization = listOf<Product>()
    private val compositeDisposable = CompositeDisposable()

    fun loadAllFavs() {
        addToFavUseCase(Product(Category("", 2, "Pro"), "A", "B", 2, "D", 150, "D", 0, 0))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe().also {
                compositeDisposable.add(it)
            }


        getAllFavsRXUseCase().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mState.value = FavResult.Success(it)
                listForActualization = it
                actualizeData()
            }.also {
                compositeDisposable.add(it)
            }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }

    fun loadCart() {
        getCartUseCase().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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

    fun insertFav(fav: Product) {
        insertFavUseCase(fav).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe().also {
                compositeDisposable.add(it)
            }
    }

    fun deleteFav(fav: Product) {
        deleteFavUseCase(fav.id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe().also {
                compositeDisposable.add(it)
            }
    }

    fun clearCart() {
        clearCartUseCase().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe().also {
                compositeDisposable.add(it)
            }
    }

    fun addToCart(product: Product) {
        addToCartUseCase(product).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe().also {
                compositeDisposable.add(it)
            }
    }

    fun mapTheData(list: List<Product>): PagingData<ProductUIModel> = PagingData.from(list.map {
        ProductUIModel(it, true)
    })

    fun actualizeData() {
        listForActualization.forEach { productInList ->
            try {
                var productActualize: Product? = null
                getProductByIdUseCase(productInList.id).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { productUseCase ->
                        productUseCase?.let { product ->
                            productActualize = product
                        }
                    }.also {
                        compositeDisposable.add(it)
                    }
                val comparator =
                    ProductUIModel(productActualize!!, true).isContentTheSame(
                        ProductUIModel(
                            productInList,
                            true
                        )
                    )
                if (!comparator) actualizeUseCase(productActualize!!)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}