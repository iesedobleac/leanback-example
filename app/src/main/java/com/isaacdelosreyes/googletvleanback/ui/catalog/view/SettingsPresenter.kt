package com.isaacdelosreyes.googletvleanback.ui.catalog.view

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.isaacdelosreyes.googletvleanback.R

/**
 * Creamos este presenter personalizado dedicado a la opción de Ajustes, ya que no queremos que
 * siga la misma estructura que el resto y también queremos ocultar el título y descripción.
 */

class SettingsPresenter: Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val imageCardView = ImageCardView(parent?.context).apply {
            infoVisibility = View.GONE
            isFocusable = true
            isFocusableInTouchMode = true
            setMainImageDimensions(180, 220)
        }
        return ViewHolder(imageCardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {
        (viewHolder?.view as? ImageCardView)?.apply {
            this.mainImageView.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.settings_icon
                )
            )
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {
        //no-op
    }

}
