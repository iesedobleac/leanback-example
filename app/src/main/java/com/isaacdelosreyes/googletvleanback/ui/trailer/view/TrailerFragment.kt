package com.isaacdelosreyes.googletvleanback.ui.trailer.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.PlaybackControlsRow
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter
import com.isaacdelosreyes.googletvleanback.R

class TrailerFragment : VideoSupportFragment() {

    /**
     * La clase VideoSupportFragment es un tipo de fragmento en el que podremos tener un reproductor
     * donde mostrar contenido de nuestra aplicación. Tiene un comportamiento especial, ya que
     * vamos a tener por un lado el reproductor, por otro lado los controles del reproductor, y
     * con otra clase auxiliar vamos a unir y sincronizar el comportamiento de ambos para poder
     * reaccionar.
     *
     * Leanback separa la IU que contiene los controles de transporte del reproductor del video.
     * Esto se logra con dos componentes: un fragmento de compatibilidad de reproducción para
     * mostrar los controles de transporte (y, opcionalmente, el video) y un adaptador de reproductor
     * para encapsular un reproductor multimedia.
     */

    private var mediaPlayer: ExoPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Para este ejemplo se ha utilizado la biblioteca Exoplayer, ya que es la que a día de
         * hoy se está utilizando ya que ha sido desarrollada por Google, por lo que se recomienda
         * por compatibilidad.
         *
         * En primer lugar debemos crear una instancia de nuestro Exoplayer encargado de que el
         * vídeo se reproduzca.
         */

        mediaPlayer = ExoPlayer.Builder(requireContext()).build()

        /**
         * Como ya hemos visto anteriormente en otros fragmentos similares de la librería Leanback,
         * necesitaremos un adapter. En este caso este adapter nos lo proporciona la propia
         * librería de ExoPlayer, llamado LeanbackPlayerAdapter.
         *
         * Este recibe tres parámetros, por un lado el contexto de la aplicación, por otro lado
         * el player que hemos creado arriba, y por otro lado el intervalo de actualización de
         * nuestro player (en milisegundos).
         */

        val playerAdapter = mediaPlayer?.let { LeanbackPlayerAdapter(view.context, it, 100) }

        /**
         * Ahora vamos a definir la clase que va a recibir la definición de los controles del
         * reproductor, aunque estará explicado más en detalle dentro de la propia clase.
         */

        val mediaPlayerGlue = playerAdapter?.let { MediaPlayerGlue(requireContext(), it) }

        /**
         * También tienes que especificar un "host de unión" que vincule la unión con el fragmento
         * de reproducción, que dibuje los controles de transporte de la IU y mantenga su estado, y
         * que devuelva los eventos de control de transporte a la unión. El host debe coincidir con
         * el tipo de fragmento de reproducción. Usa PlaybackSupportFragmentGlueHost con un
         * PlaybackSupportFragment y VideoSupportFragmentGlueHost con un VideoSupportFragment.
         */

        mediaPlayerGlue?.host = VideoSupportFragmentGlueHost(this)
        mediaPlayerGlue?.playWhenPrepared()

        /**
         * Y como ya hemos hecho en numerosas ocasiones en otros fragmentos similares, vamos
         * a crear nuestro adapter al que le vamos a pasar el presenter que lo tenemos en el
         * mediaPlayerGlue que habiamos creado, a través del método getPlaybackRowPresenter.
         *
         * A este adapter le vamos a añadir una row, que será la que contiene los controles
         * del reproductor, también dispinible con la clase mediaPlayerGlue en el método
         * getConstrolsRow que nos devuelve una clase de tipo PlaybackControlsRow.
         */

        val mediaPlayerAdapter = ArrayObjectAdapter(mediaPlayerGlue?.playbackRowPresenter)
        mediaPlayerAdapter.add(mediaPlayerGlue?.controlsRow)

        /**
         * Por último seteamos nuestro adapter y preparamos nuestro reproductor dandole un
         * MediaSource con la url del vídeo que vamos a reproducir. Para profundizar más en
         * esto mejor ojear la documentación de ExoPlayer, donde se explica que es un MediaItem
         * y las diferentes formar de crearlo y que opciones nos ofrece.
         */

        adapter = mediaPlayerAdapter
        preparePlayer(mediaPlayer, getString(R.string.video_test_mp4))
    }

    private fun preparePlayer(exoPlayer: ExoPlayer?, videoUrl: String) {
        val mediaSource = MediaItem.fromUri(videoUrl)
        exoPlayer?.addMediaItem(mediaSource)
        exoPlayer?.prepare()
    }

    override fun onStop() {
        mediaPlayer?.release()
        super.onStop()
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        super.onDestroy()
    }

    inner class MediaPlayerGlue(context: Context, adapter: LeanbackPlayerAdapter) :
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
}