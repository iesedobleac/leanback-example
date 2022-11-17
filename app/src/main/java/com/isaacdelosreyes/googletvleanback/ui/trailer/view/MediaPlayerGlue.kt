package com.isaacdelosreyes.googletvleanback.ui.trailer.view

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.PlaybackControlsRow
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter
import com.isaacdelosreyes.googletvleanback.R

class MediaPlayerGlue(context: Context, adapter: LeanbackPlayerAdapter) :
        PlaybackTransportControlGlue<LeanbackPlayerAdapter>(context, adapter) {

        /**
         * Dentro de esta clase vamos a definir las acciones, que serán aquellos botones que
         * vamos a tener en nuestro reproductor. Podemos tener acciones ya definidas por la
         * librería, o también podemos definir los nuestros propios gracias a la clase de
         * MoreActions, donde podremos acceder a diversos método para personalizarlo.
         */

        private val actionRewind = PlaybackControlsRow.RewindAction(context)
        private val actionFastForward = PlaybackControlsRow.FastForwardAction(context)
        private val actionStopVideo = PlaybackControlsRow.MoreActions(context)

        init {

            /**
             * En este caso hemos definido un botón de acción personalizado para parar el vídeo
             * y hacerlo retroceder al principio, igual que haría un stop, asi que le hemos
             * definido el icono gracias al setter que nos proporciona la clase MoreActions.
             */

            actionStopVideo.icon = AppCompatResources.getDrawable(context, R.drawable.icon_stop)
        }

        /**
         * Tenemos nuestras acciones creadas, pero no nos servirán de nada si no sobreescribimos el
         * método onCreatePrimaryActions. Por defecto si dejamos el super ya tendremos la función
         * del play/pause integrado, aunque si queremos definirlo nosotros mismos, podemos eliminar
         * el super y darle nosotros la funcionalidad.
         */

        override fun onCreatePrimaryActions(primaryActionsAdapter: ArrayObjectAdapter?) {
            super.onCreatePrimaryActions(primaryActionsAdapter)
            primaryActionsAdapter?.apply {
                add(actionStopVideo)
                add(actionRewind)
                add(actionFastForward)
            }
        }

        /**
         * El método a través del cual vamos a darle un comportamiento a nuestras acciones es
         * onActionClicked, donde en el caso de este ejemplo hemos hecho uso de un when para
         * tener el código más ordenado.
         */

        override fun onActionClicked(action: Action?) = when(action) {
            actionFastForward -> setActionFastForward()
            actionStopVideo -> setActionStopVideo()
            else -> super.onActionClicked(action)
        }

        override fun onHostStop() {
            pause()
            super.onHostStop()
        }

        private fun setActionStopVideo() {
            pause()
            seekTo(0)
        }

        private fun setActionFastForward() {
            seekTo(currentPosition + 10000)
        }
    }