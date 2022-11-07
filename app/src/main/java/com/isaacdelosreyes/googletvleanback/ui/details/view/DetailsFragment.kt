package com.isaacdelosreyes.googletvleanback.ui.details.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.app.DetailsSupportFragmentBackgroundController
import androidx.leanback.widget.*
import com.isaacdelosreyes.googletvleanback.data.local.model.MovieBo
import com.isaacdelosreyes.googletvleanback.ui.trailer.view.TrailerActivity
import com.isaacdelosreyes.googletvleanback.utils.KEY_MOVIE_SELECTED
import com.isaacdelosreyes.googletvleanback.utils.loadGlideImageBitmap
import com.isaacdelosreyes.googletvleanback.utils.loadGlideImageDrawable
import com.isaacdelosreyes.googletvleanback.utils.parcelable


class DetailsFragment : DetailsSupportFragment() {

    /**
     * El controlador es una clase disponible en DetailsSupportFragment que nos permite cambiar el
     * fondo de la cabecera que aparece en nuestra aplicación, agregarle efectos, etc.
     */

    private val controller = DetailsSupportFragmentBackgroundController(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Al haberle pasado por un bundle la película seleccionada en el fragmento del catálogo
         * ahora la tenemos disponible para poder pintar los detalles de la misma, asi que lo
         * primero que hacemos es recuperar ese bundle.
         */

        val movieSelected: MovieBo? = activity?.intent?.parcelable(KEY_MOVIE_SELECTED)

        context?.loadGlideImageBitmap(movieSelected?.backdrop.orEmpty()) {

            /**
             * Para poder previsualizar una imagen en el header de nuestro fragment es necesario
             * hacer uso del método enableParallax, que nos habilita esta posibilidad. Si no
             * llamamos a este método, no vamos a poder ver esa imagen.
             */

            controller.enableParallax()
            controller.coverBitmap = it
        }

        /**
         * En el ejemplo vamos a crear esta pantalla sobre un FullWidthDetailOverviewRowPresenter
         * que no es más que una de las formas disponibles que nos brinda la biblioteca de
         * Leanback para mostrar la vista.
         */

        val presenter = FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())

        /**
         * Como ya hemos visto antes, irá montado sobre un objeto de tipo ArrayObjectAdapter, que
         * será nuestro adapter principal, este recibe un presenter y en este caso será el que
         * acabamos de crear. Sobre este vamos a montar la estructura.
         */

        val rowsAdapter = ArrayObjectAdapter(presenter)

        /**
         * Vamos a pintar un apartado para poder mostrar información sobre la película que hemos
         * seleccionado, y así poder mostrar un título, una imagen, una descripción, etc. Para
         * ello ya tenemos una clase predefinida llamada DetailsOverviewRow.
         */

        val detailsOverviewRow = DetailsOverviewRow(movieSelected)

        context?.loadGlideImageDrawable(movieSelected?.posterPath.orEmpty()) {

            /**
             * Uno de los parámetros de esta row es el de colocarle una imagen para nuestro
             * elemento, que en este caso será la portada de la película seleccionada.
             */

            detailsOverviewRow.imageDrawable = it
        }

        /**
         * También vamos a definir otro adapter para nuestra row del detalle en el que podremos
         * configurar botones de acción para hacer la tarea que nosotros le vamos a indicar.
         */

        val actionAdapter = ArrayObjectAdapter()

        actionAdapter.add(
            Action(
                1L,
                "Ver trailer"
            )
        )

        detailsOverviewRow.actionsAdapter = actionAdapter

        /**
         * El método onActionClickedListener nos va a permitir detectar a que acción ha pulsado
         * el usuario y determinar un comportamiento que queramos. En este caso será nuestro
         * punto de navegación a la siguiente pantalla.
         */

        presenter.onActionClickedListener = OnActionClickedListener {
            when(it.id) {
                1L -> {
                    val intent = Intent(requireContext(), TrailerActivity::class.java)
                    requireActivity().startActivity(intent)
                }
            }
        }

        /**
         * Finalmente ya tenemos nuestra row montada con lo que queriamos añadir, asi que la
         * añadimos a nuestro adapter principal al igual que hicimos con las acciones. Finalmente
         * vamos a setear este adapter a nuestro fragment.
         */

        rowsAdapter.add(detailsOverviewRow)
        adapter = rowsAdapter
    }
}

class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {

    /**
     * Esta clase nos va a permitir acceder a los componentes de texto de nuestro presenter.
     */

    override fun onBindDescription(viewHolder: ViewHolder?, item: Any?) {
        val movie = item as? MovieBo
        viewHolder?.apply {
            title.text = movie?.title
            subtitle.text = movie?.releaseDate
            body.text = movie?.overview
        }
    }
}