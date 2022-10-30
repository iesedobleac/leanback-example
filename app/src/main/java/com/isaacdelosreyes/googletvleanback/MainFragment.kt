package com.isaacdelosreyes.googletvleanback

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.Presenter

class MainFragment: BrowseSupportFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = "Browse"

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

        (1..5).forEach { index ->

            /**
             * Para este ejemplo vamos a pintar cinco categorías, por lo que hacemos un bucle que
             * va a recorrer de la posición 1 a la 5 (1..5) pintando cada una de las cabeceras.
             *
             * El tipo de objeto que necesitamos para pintar cada una de ellas es un HeaderItem
             * que recibe como parámetro un índice y un título.
             */

            val headerItem = HeaderItem(index.toLong(), "Categoría $index")

            /**
             * Después crearemos el listado horizontal que estará contenido dentro de cada una de
             * las categorías. Como vimos antes, también vamos a usar un ArrayObjectAdapter, pero
             * esta vez con un presenter personalizado por nosotros.
             *
             * Para este ejemplo vamos a crear 10 elementos, donde cada uno de ellos será un dato
             * de tipo string que nos servirá para darle un nombre a cada elemento del listado.
             */

            val listRow = ArrayObjectAdapter(CardPresenter())
            listRow.addAll(0, (1..10).map { "Title $it" })

            /**
             * Una vez creamos cada listado lo vamos añadiendo a nuestro adapter vertical en cada
             * iteración del bucle. El tipo de objeto que va a recibir para el ejemplo es un
             * ListRow que recibe el objeto HeaderItem de la categoría y su lista asociada.
             */

            rowsAdapter.add(ListRow(headerItem, listRow))
        }

        adapter = rowsAdapter
    }
}

/**
 * Con esta clase vamos a crear el presenter personalizado que le dará un diseño a cada elemento
 * de nuestro listado horizontal. Esta clase debe extender de Presenter() para que actue como tal.
 */

class CardPresenter: Presenter() {

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
            setMainImageDimensions(130, 200)
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
         * En este caso nuestro item era el String que habiamos mandado al crear cada row.
         */

        val title = item as? String
        (viewHolder.view as? ImageCardView)?.apply {
            this.titleText = title
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        //no-op
    }

}
