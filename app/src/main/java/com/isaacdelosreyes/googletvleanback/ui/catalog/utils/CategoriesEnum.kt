package com.isaacdelosreyes.googletvleanback.ui.catalog.utils

import com.isaacdelosreyes.googletvleanback.utils.*

enum class CategoriesEnum(val sortBy: String) {

    /**
     * Este enum será el que nos sirva para obtener el nombre de las categorías para poder
     * pintarlas en nuestro listado del fragmento del catálogo.Gracias a el parámetro sortBy
     * podemos acceder al tipo de ordenación que usaremos en la llamada de la API. Disponemos
     * de una función de extensión en el fichero Extensions.kt por la cual según la categoría,
     * le asigna un nombre u otro.
     */

    POPULAR(DISCOVER_SORT_BY_POPULARITY),
    LEAST_VOTED(DISCOVER_SORT_BY_LEAST_VOTED),
    MOST_VOTED(DISCOVER_SORT_BY_MOST_VOTED),
    REVENUE(DISCOVER_SORT_BY_REVENUE)
}