package com.flexeiprata.androidmytaskapplication.description.presentation.views

import android.util.Log
import androidx.lifecycle.ViewModel
import com.flexeiprata.androidmytaskapplication.cart.presentation.AddToCartUseCase
import com.flexeiprata.androidmytaskapplication.common.LOG_DEBUG
import com.flexeiprata.androidmytaskapplication.description.presentation.uiadapter.RowDescUI
import com.flexeiprata.androidmytaskapplication.description.presentation.uiadapter.RowHeaderUI
import com.flexeiprata.androidmytaskapplication.description.presentation.uiadapter.RowMainUI
import com.flexeiprata.androidmytaskapplication.description.presentation.usecases.GetProductUseCase
import com.flexeiprata.androidmytaskapplication.description.presentation.views.uimodels.RowItem
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.DeleteFavUseCase
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.GetFavByIdUseCase
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.InsertFavUseCase
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DescViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val getFavByIdUseCase: GetFavByIdUseCase,
    private val insertFavUseCase: InsertFavUseCase,
    private val deleteFavUseCase: DeleteFavUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {
    private lateinit var product: Product
    private var mutableSharedFlow =
        MutableStateFlow<DescResult>(DescResult.Loading(emptyList<RowItem>()))
    val sharedState get() = mutableSharedFlow.asStateFlow()

    private val compositeDisposable = CompositeDisposable()

    fun getProductsById(id: Int) {
        getProductUseCase(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                product = it
                Log.d(LOG_DEBUG, "On Success")
                val listOfModels = mutableListOf<RowItem>()
                listOfModels.add(RowHeaderUI("Header", product.category.icon))
                listOfModels.add(
                    RowMainUI(
                        "Main",
                        product.name,
                        String.format("%1s\n%2s", product.size, product.colour),
                        product.price
                    )
                )
                listOfModels.add(RowDescUI("Desc", product.details))
                mutableSharedFlow.value = DescResult.Success(listOfModels)
            }, {
                mutableSharedFlow.value =
                    DescResult.Error("Error acquired during loading: ${it.message}")
            })
            .also {
                compositeDisposable.add(it)
            }
    }

    fun checkIfIsFav(id: Int): Single<Product?> =
        getFavByIdUseCase(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    fun insertFav() {
        try {
            insertFavUseCase(product).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe().also {
                    compositeDisposable.add(it)
                }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun deleteFav() {
        try {
            deleteFavUseCase(product.id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe().also {
                    compositeDisposable.add(it)
                }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun addToCart() {
        try {
            addToCartUseCase(product).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe().also {
                    compositeDisposable.add(it)
                }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onCleared() {
        compositeDisposable.apply {
            dispose()
            clear()
        }
        super.onCleared()
    }
}

