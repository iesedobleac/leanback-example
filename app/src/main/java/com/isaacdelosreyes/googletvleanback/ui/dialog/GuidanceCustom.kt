package com.isaacdelosreyes.googletvleanback.ui.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.leanback.widget.GuidanceStylist
import com.isaacdelosreyes.googletvleanback.R

/**
 * Esta clase extiende de la propia clase de GuidanceStylist ya que para comenzar es lo más sencillo
 * y nos permite editar el diseño de ese apartado añadiendo lo que necesitemos.
 */

class GuidanceCustom(private val title: String, private val desc: String) : GuidanceStylist() {

    private var guidanceCustomTitleLabel: TextView? = null
    private var guidanceCustomDescLabel: TextView? = null

    /**
     * Es importante comentar que este método onCreateView() no es el mismo que el que vemos en un
     * fragment, sino que es uno creado en la clase GuidanceStylist que estamos sobreescribiendo.
     */

    override fun onCreateView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        guidance: Guidance?
    ): View? {
        val view = inflater?.inflate(R.layout.guidance_custom, container, false)

        guidanceCustomTitleLabel = view?.findViewById(R.id.guidance_title)
        guidanceCustomDescLabel = view?.findViewById(R.id.guidance_description)

        guidanceCustomTitleLabel?.text = title
        guidanceCustomDescLabel?.text = desc

        return view
    }
}