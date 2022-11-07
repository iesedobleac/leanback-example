package com.isaacdelosreyes.googletvleanback.ui.catalog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaacdelosreyes.googletvleanback.BuildConfig
import com.isaacdelosreyes.googletvleanback.data.local.model.MovieBo
import com.isaacdelosreyes.googletvleanback.data.remote.model.toBo
import com.isaacdelosreyes.googletvleanback.data.remote.retrofit.RemoteConnection
import com.isaacdelosreyes.googletvleanback.ui.catalog.utils.CategoriesEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CatalogViewModel: ViewModel() {

    private val getMoviesMutableLiveData = MutableLiveData<Map<CategoriesEnum, List<MovieBo>>>()

    fun getMoviesLiveData(): LiveData<Map<CategoriesEnum, List<MovieBo>>> = getMoviesMutableLiveData

    /**
     * Gracias a Kotlin, tenemos el método associateWith, el cual nos devuelve un Map en el que
     * podemos asociar un objeto a cada elemento que va recorriendo el bucle. En este caso lo
     * estamos utilizando para crear un mapa que contiene por un lado la categoría, y el listado
     * asociado a esta. El resultado lo mandamos con un LiveData.
     */

    fun getMoviesFromRemote() {
        viewModelScope.launch(Dispatchers.Default) {
            getMoviesMutableLiveData.postValue(CategoriesEnum.values().associateWith { categories ->
                RemoteConnection.getMoviesWs.getListMovies(
                    BuildConfig.API_KEY,
                    categories.sortBy
                ).movies.filter { it.posterPath.isNullOrEmpty().not() }.map { it.toBo() }
            })
        }
    }
}