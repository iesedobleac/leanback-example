package com.isaacdelosreyes.googletvleanback.ui.utils

import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.isaacdelosreyes.googletvleanback.data.local.model.MovieBo
import com.isaacdelosreyes.googletvleanback.utils.loadUrl

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