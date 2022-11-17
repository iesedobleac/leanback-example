package com.isaacdelosreyes.googletvleanback.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.leanback.app.GuidedStepSupportFragment
import androidx.leanback.widget.GuidanceStylist
import androidx.leanback.widget.GuidedAction
import com.isaacdelosreyes.googletvleanback.R
import com.isaacdelosreyes.googletvleanback.ui.catalog.view.CatalogActivity

/**
 * Un GuidedStepSupportFragment se utiliza para guiar al usuario a través de una decisión o una
 * serie de decisiones. Se compone de una vista de orientación a la izquierda y una vista a la
 * derecha que contiene una lista de posibles acciones.
 */

class DialogFragment : GuidedStepSupportFragment() {

    companion object {
        private const val ACTION_ID_POSITIVE = 1L
        private const val ACTION_ID_NEGATIVE = 2L
    }

    /**
     * Entre una de las varias cosas que podemos editar de esta clase, se encuentra el background
     * de la parte izquierda, donde podremos inflar nuestro propio XML para darle una imagen de
     * fondo.
     */

    override fun onCreateBackgroundView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(R.layout.dialog_background, container, false)
    }

    /**
     * Guidance es más referido a la parte izquierda de esta pantalla, la que contiene el texto
     * que ayudará al usuario a que sepa que debe hacer y porque debe hacerlo.
     *
     * Podemos sobreescribir dos métodos:
     *
     * El básico, llamado onCreateGuidance() donde devolveremos un objeto de tipo Guidance que
     * recibe un título, descripción y otros parámetros opcionales.
     *
     * El customizado que es onCreateGuidanceStylist() el cual podemos crear nuestra propia clase
     * donde inflaremos una vista customizada por nosotros donde podremos tener muchas más
     * posibilidades de personalización y lógica de la clase. En este caso hemos creado una nueva
     * clase llamada GuidanceCustom que recibe un título y una descripción.
     */

    override fun onCreateGuidanceStylist(): GuidanceStylist {
        return GuidanceCustom(
            getString(R.string.dialog_fragment_title),
            getString(R.string.dialog_fragment_desc)
        )
    }

    /**
     * Las acciones es lo que veremos en la parte derecha de la pantalla y son las opciones que
     * vamos a dar al usuario dependiendo de la lógica que queramos hacerle.
     *
     * También podemos agregar subacciones a una propia acción creando otra lista de acciones
     * que queremos que actuen como tal y con el setter de subActions le añadimos la lista.
     */

    override fun onCreateActions(
        actions: MutableList<GuidedAction>,
        savedInstanceState: Bundle?
    ) {
        val enterAction = GuidedAction.Builder(requireContext())
            .build()
            .apply {
                id = ACTION_ID_POSITIVE
                title = getString(R.string.dialog_fragment_action_enter)
            }

        val cancelAction = GuidedAction.Builder(requireContext())
            .build()
            .apply {
                id = ACTION_ID_NEGATIVE
                title = getString(R.string.dialog_fragment_action_cancel)
            }

        actions.addAll(listOf(enterAction, cancelAction))
    }

    /**
     * Otra cosa esencial de este fragmento es poder controlar el click de las acciones y de las
     * subacciones, para ello le hemos asignado previamente un id que ahora podremos usar para
     * identificar la que estamos pulsando y realizar lo que queramos.
     */

    override fun onGuidedActionClicked(action: GuidedAction?) {
        if (action?.id == ACTION_ID_POSITIVE) {
            val intent = Intent(context, CatalogActivity::class.java)
            startActivity(intent)
            activity?.finish()

        } else {
            activity?.finish()
        }
    }
}