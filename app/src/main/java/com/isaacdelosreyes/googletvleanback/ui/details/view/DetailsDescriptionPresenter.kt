package com.isaacdelosreyes.googletvleanback.ui.details.view

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter
import com.isaacdelosreyes.googletvleanback.data.local.model.MovieBo

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