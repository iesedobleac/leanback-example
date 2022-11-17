package com.isaacdelosreyes.googletvleanback.ui.catalog.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import com.isaacdelosreyes.googletvleanback.data.local.model.MovieBo
import com.isaacdelosreyes.googletvleanback.ui.catalog.viewmodel.CatalogViewModel
import com.isaacdelosreyes.googletvleanback.ui.details.view.DetailsActivity
import com.isaacdelosreyes.googletvleanback.ui.utils.CardPresenter
import com.isaacdelosreyes.googletvleanback.utils.KEY_MOVIE_SELECTED
import com.isaacdelosreyes.googletvleanback.utils.getName

class VideoCatalogFragment : BrowseSupportFragment() {

    private val catalogViewModel: CatalogViewModel by viewModels()

    /**
     * TODO: Pendiente de escribir una explicación
     */

    private val listRowFragmentFactory = object : FragmentFactory<Fragment>() {

        override fun createFragment(row: Any?) = ListRowsFragment.newInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = "Browse"

        catalogViewModel.getMoviesLiveData().observe(viewLifecycleOwner) { movies ->

            /**
             * Para pintar esta pantalla vamos a definir dos adapter, uno que maneja la lista vertical
             * de categorías y otra horizontal que está contenida dentro de cada elemento de la lista
             * vertical.
             *
             * La clase ArrayObjectAdapter es la que vamos a usar para definir los adapter la mayoría
             * de las veces para pintar nuestras vistas y debemos pasarle un presenter que es el que
             * le indica de que manera se va a pintar ese listado.
             *
             * Para nuestra lista vertical de categorías, vamos a pasarle un ListRowPresenter que más
             * adelante vamos a rellenar con contenido.
             */

            val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

            movies.forEach { (category, movies) ->

                /**
                 * Para este ejemplo vamos a pintar cinco categorías, por lo que hacemos un bucle que
                 * va a recorrer de la posición 1 a la 5 (1..5) pintando cada una de las cabeceras.
                 *
                 * El tipo de objeto que necesitamos para pintar cada una de ellas es un HeaderItem
                 * que recibe como parámetro un índice y un título.
                 */

                val headerItem = HeaderItem(category.ordinal.toLong(), category.getName())

                /**
                 * Después crearemos el listado horizontal que estará contenido dentro de cada una de
                 * las categorías. Como vimos antes, también vamos a usar un ArrayObjectAdapter, pero
                 * esta vez con un presenter personalizado por nosotros.
                 *
                 * Para este ejemplo vamos a crear 10 elementos, donde cada uno de ellos será un dato
                 * de tipo Movie que nos servirá para darle valores a cada elemento del listado.
                 */

                val listRow = ArrayObjectAdapter(CardPresenter())

                listRow.addAll(0, movies)

                /**
                 * Una vez creamos cada listado lo vamos añadiendo a nuestro adapter vertical en cada
                 * iteración del bucle. El tipo de objeto que va a recibir para el ejemplo es un
                 * ListRow que recibe el objeto HeaderItem de la categoría y su lista asociada.
                 */

                rowsAdapter.add(ListRow(headerItem, listRow))
            }

            adapter = rowsAdapter

            /**
             * TODO: Pendiente de escribir una explicación
             */

            mainFragmentRegistry.registerFragment(ListRow::class.java, listRowFragmentFactory)

        }

        catalogViewModel.getMoviesFromRemote()

        /**
         * Con este método podemos detectar cuando se hace click en un elemento de nuestra lista
         * horizontal y podremos hacer alguna acción acorde a ello.
         */

        onItemViewClickedListener = OnItemViewClickedListener { _, item, _, _ ->
            Bundle().apply {
                putParcelable(KEY_MOVIE_SELECTED, item as? MovieBo)
                val intent = Intent(requireContext(), DetailsActivity::class.java)
                intent.putExtras(this)
                requireActivity().startActivity(intent)
            }
        }
    }
}
