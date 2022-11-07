package com.isaacdelosreyes.googletvleanback.ui.catalog.view

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.isaacdelosreyes.googletvleanback.data.local.model.MovieBo
import com.isaacdelosreyes.googletvleanback.ui.catalog.utils.CategoriesEnum
import com.isaacdelosreyes.googletvleanback.ui.catalog.viewmodel.CatalogViewModel
import com.isaacdelosreyes.googletvleanback.ui.details.view.DetailsActivity
import com.isaacdelosreyes.googletvleanback.utils.KEY_MOVIE_SELECTED
import com.isaacdelosreyes.googletvleanback.utils.getName
import com.isaacdelosreyes.googletvleanback.utils.loadGlideImageBitmap
import com.isaacdelosreyes.googletvleanback.utils.loadUrl
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainFragment : BrowseSupportFragment() {

    private val catalogViewModel: CatalogViewModel by viewModels()

    private var jobChangeBackground: Job? = null

    private val getMovieListObserverBo = Observer<Map<CategoriesEnum, List<MovieBo>>> { movies ->

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = "Browse"

        catalogViewModel.getMoviesLiveData().observe(viewLifecycleOwner, getMovieListObserverBo)
        catalogViewModel.getMoviesFromRemote()

        /**
         * Gracias a este método podemos realizar alguna accion cada vez que un elemento de la
         * vista es seleccionado, es decir, que pasamos por encima de el. Aquí es donde vamos
         * a realizar el cambio de imagen de fondo.
         */

        onItemViewSelectedListener = OnItemViewSelectedListener { _, item, _, _ ->
            (item as? MovieBo)?.let { movie ->
                context?.loadGlideImageBitmap(movie.backdrop.orEmpty()) {
                    updateBackground(it)
                }
            }
        }

        onItemViewClickedListener = OnItemViewClickedListener { _, item, _, _ ->
            Bundle().apply {
                putParcelable(KEY_MOVIE_SELECTED, item as? MovieBo)
                val intent = Intent(requireContext(), DetailsActivity::class.java)
                intent.putExtras(this)
                requireActivity().startActivity(intent)
            }
        }
    }

    /**
     * Vamos a usar este método para cargar una imagen de fondo cuando vamos navegando por los
     * diferentes elementos que tiene cada listado, en este caso lo que vamos a cargar es una
     * imagen de la película que estamos cargando.
     *
     * Esto podemos hacerlo gracias a la clase BackgroundManager de la librería de Leanback. Para
     * que funcione debemos adjuntarla (attach) a la ventana que queremos que aparezca, en este
     * caso es a la de nuestra actividad principal.
     *
     * Por defecto tiene un problema y es que se va a cargar el fondo cada vez que pasemos por un
     * elemento, pudiendo dejar una cola que ocasione que se cargen varios cada poco tiempo si
     * nos hemos movido rápido. Una posible solución a esto es usar corrutinas, encerrando la
     * tarea en un job conseguimos, por una parte optimizar la aplicación al mandarlo a otro hilo
     * y por otro lado nos permite cancelar la ejecución de la corrutina constantemente de forma
     * que no se va a terminar de ejecutar hasta que estemos encima de un elemento al menos los
     * 300 milisegundos que le hemos asignado al delay.
     */

    private fun updateBackground(resource: Bitmap) {
        jobChangeBackground?.cancel()
        jobChangeBackground = viewLifecycleOwner.lifecycleScope.launch {
            delay(300)
            BackgroundManager.getInstance(requireActivity()).apply {

                if (isAttached.not()) attach(requireActivity().window)
                setBitmap(resource)
            }
        }
    }
}

/**
 * Con esta clase vamos a crear el presenter personalizado que le dará un diseño a cada elemento
 * de nuestro listado horizontal. Esta clase debe extender de Presenter() para que actue como tal.
 */

class CardPresenter : Presenter() {

    /**
     * Una vez extendemos de la clase, vamos a estar obligados a sobreescribir tres métodos que
     * son necesarios para pintar nuestra vista. Son bastante parecidos a los que hemos en un
     * adapter de un RecyclerView en mobile.
     *
     * onCreateViewHolder: va a devolver el ViewHolder que vamos a pintar. Este ViewHolder recibe
     * como parámetro una vista, que en nuestro caso será una ya definida por la librería.
     *
     * onBindViewHolder: aquí le damos el valor a los elementos de nuestra vista, como el nombre,
     * la imagen, etc.
     *
     * onUnbindViewHolder: para liberar recursos cuando ya no estamos utilizando el presenter.
     */

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {

        /**
         * ImageCardView es una vista predefinida de la biblioteca de Leanback y es la que vamos a
         * usar para nuestro ejemplo. Podemos proporcionarle un título, subtitulo y una imagen.
         *
         * Es importante darle valores a los parámetros isFocusable e isFocusableInTouchMode para
         * asegurarnos que estos elementos puedan tener foco, que al final es el objetivo en TV
         * al manejar la aplicación con el mando.
         *
         * Con el método setMainImageDimensions le vamos a dar unos valores de ancho y alto a cada
         * una de las tarjetas.
         */

        val imageCardView = ImageCardView(parent.context).apply {
            isFocusable = true
            isFocusableInTouchMode = true
            setMainImageDimensions(180, 220)
        }
        return ViewHolder(imageCardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {

        /**
         * Una de las complejidades de usar la librería de Leanback es tener que hacer muchos
         * casteos ya que como podemos observar, no recibimos el tipo de objeto que hemos
         * enviado, sino que siempre recibimos Any y tenemos que realizar el casteo con el
         * objetivo de obtener aquello que necesitamos.
         *
         * En este caso nuestro item era la Movie que habiamos mandado al crear cada row.
         */

        val movieBo = item as? MovieBo
        (viewHolder.view as? ImageCardView)?.apply {
            this.mainImageView.loadUrl(movieBo?.posterPath)
            this.titleText = movieBo?.title
            this.contentText = movieBo?.releaseDate
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        //no-op
    }

}
