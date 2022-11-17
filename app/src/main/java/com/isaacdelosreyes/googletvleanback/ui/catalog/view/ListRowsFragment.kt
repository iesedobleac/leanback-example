package com.isaacdelosreyes.googletvleanback.ui.catalog.view

import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.isaacdelosreyes.googletvleanback.R
import com.isaacdelosreyes.googletvleanback.data.local.model.MovieBo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Vamos a crear una clase que implementa a RowsSupportFragment de Leanback para poder hacer unos
 * cambios. RowsSupportFragment es un fragmento que contiene un RecyclerView vertical.
 *
 * En la implementación básica, los RecyclerView verticales reciclan los ViewHolders que muestran
 * los títulos y los RecyclerView horizontales.
 */

class ListRowsFragment : RowsSupportFragment() {

    private var jobChangeBackground: Job? = null

    companion object {

        fun newInstance() = ListRowsFragment()
    }

    private var player: ExoPlayer? = null

    override fun onStart() {
        super.onStart()

        /**
         * Gracias a este método podemos realizar alguna accion cada vez que un elemento de la
         * vista es seleccionado, es decir, que pasamos por encima de el. Aquí es donde vamos
         * a realizar el cambio de vídeo iniciando ExoPlayer.
         */

        onItemViewSelectedListener = OnItemViewSelectedListener { _, item, _, _ ->
            (item as? MovieBo)?.let { movie ->
                jobChangeBackground?.cancel()
                jobChangeBackground = viewLifecycleOwner.lifecycleScope.launch {
                    delay(300)

                    /**
                     * La idea es que el vídeo vaya cargandose cada vez que cambiamos de elemento
                     * en nuestro recycler horizontal, si el actual estaba reproduciendose
                     * vamos a deternerlo para que no se reproduzcan al mismo tiempo y no se
                     * mezclen unos con otros.
                     */

                    if (getPlayer()?.player?.isPlaying == true) {
                        getPlayer()?.player?.release()
                    }
                    initializePlayer(movie)
                }
            }
        }
    }

    /**
     * Esta es una implementación básica de ExoPlayer para poder cargar el vídeo a través de la
     * URL que le hemos proporcionado.
     */

    private fun initializePlayer(movieBo: MovieBo) {
        player = ExoPlayer.Builder(requireContext())
            .build()
            .also { safePlayer ->
                getPlayer()?.player = safePlayer
                val mediaItem = MediaItem.fromUri(
                    movieBo.trailerUrl.orEmpty()
                )
                safePlayer.setMediaItem(mediaItem)
                safePlayer.playWhenReady = true
                safePlayer.seekTo(0, 0L)
                safePlayer.prepare()
            }
    }

    override fun setExpand(expand: Boolean) {
        super.setExpand(true)
    }

    /**
     * Con este método obtenemos la vista del player que hemos añadido en el xml. Podriamos
     * hacerlo sin el método y usar la línea con el findViewById directamente, pero de esta
     * forma lo hacemos más ordenado y sin repetir código.
     */

    @Suppress("DEPRECATION")
    private fun getPlayer(): PlayerView? = view?.findViewById(R.id.rows_fragment__label__test)

    /**
     * En el método onPause vamos a pausar la reproducción del vídeo para evitar que siga sonando
     * cuando pasamos la aplicación a segundo plano.
     */

    override fun onPause() {
        getPlayer()?.player?.pause()
        super.onPause()
    }

    /**
     * En el método onStop vamos a liberar el player para liberar memoría y para evitar que siga
     * sonando cuando pasamos la aplicación a segundo plano.
     */

    override fun onStop() {
        getPlayer()?.player?.release()
        super.onStop()
    }
}