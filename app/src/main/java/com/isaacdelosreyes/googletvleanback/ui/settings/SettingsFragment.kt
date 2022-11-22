package com.isaacdelosreyes.googletvleanback.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.leanback.preference.LeanbackPreferenceFragmentCompat
import androidx.leanback.preference.LeanbackSettingsFragmentCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import com.isaacdelosreyes.googletvleanback.R
import com.isaacdelosreyes.googletvleanback.utils.KEY_ABOUT_DEV_GOOGLE_PLAY_SETTINGS

/**
 * Usamos esta clase para poder crear una pantalla de preferencias. Al final por detrás estamos
 * haciendo uso de las preferencias que nos brinda Android para las apps móviles, pero adaptada a
 * TV.
 */

class SettingsFragment : LeanbackSettingsFragmentCompat() {

    companion object {

        private const val PREFERENCE_RESOURCE_ID = "preferenceResource"
        private const val PREFERENCE_ROOT = "root"
    }

    private var preferencesFragment: PreferenceFragmentCompat? = null

    /**
     * Se llama para instanciar el PreferenceFragment inicial que se mostrará en este fragmento.
     * Se espera que las implementaciones llamen a startPreferenceFragment.
     */

    override fun onPreferenceStartInitialScreen() {
        preferencesFragment = buildPreferenceFragment(R.xml.settings_compat, null)
        preferencesFragment?.let {
            startPreferenceFragment(it)
        }
    }

    /**
     * Se llama cuando el usuario ha hecho clic en una preferencia que tiene un nombre de
     * clase de fragmento asociado. La implementación debe instanciar y cambiar a una instancia
     * del fragmento dado.
     */

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {
        return false
    }

    /**
     * Se llama cuando el usuario ha hecho clic en una PreferenceScreen para navegar a una nueva
     * pantalla de preferencias.
     */

    override fun onPreferenceStartScreen(
        caller: PreferenceFragmentCompat,
        preferecenScreen: PreferenceScreen
    ): Boolean {
        val destinationFragment = buildPreferenceFragment(
            R.xml.settings_compat,
            preferecenScreen.key
        )
        startPreferenceFragment(destinationFragment)
        return true
    }

    /**
     * Usaremos este método para crear la instancia del fragmento que vamos a pintar en la pantalla
     * de preferencias. Le vamos a pasar la raiz, que será nula o no dependiendo de si estamos en
     * la pantalla principal de preferencias (será nula) o no (no será nula).
     *
     * También debemos pasarle el xml donde se encuentra nuestra configuración de preferencias para
     * que la aplicación sepa como montar las categorías y subcategorías.
     */

    private fun buildPreferenceFragment(
        preferenceResId: Int,
        root: String?
    ): PreferenceFragmentCompat {
        val preferencesFragment = PreferencesFragment()
        Bundle().apply {
            putInt(PREFERENCE_RESOURCE_ID, preferenceResId)
            putString(PREFERENCE_ROOT, root)
            preferencesFragment.arguments = this
        }
        return preferencesFragment
    }

    /**
     * Necesitamos esta clase que será el fragmento encargado de instanciar todas las pantallas
     * de nuestras preferencias. Por un lado comprobamos si la raiz es nula, si esto es así
     * quiere decir que es la primera vez que abrimos las preferencias y por ende estamos en la
     * raiz de esta.
     *
     * Cuando estamos desarrollando para TV es importante estar atentos a que extendemos la clase
     * de LeanbackPreferenceFragmentCompat y no de PreferenceFragmentCompat ya que de lo contrario
     * los estilos no estarán bien aplicados y se verá mal.
     */

    class PreferencesFragment : LeanbackPreferenceFragmentCompat() {

        /**
         * Cuando vamos navegando también entramos en el método onCreatePreferences, pero en esta
         * ocasión si venimos de un destino por lo que existe una raiz y en vez de usar el método
         * addPreferencesFromResource, usamos el setPreferencesFromResource:
         *
         * addPreferencesFromResource: Infla el recurso XML y añade la jerarquía de preferencias a
         * la jerarquía de preferencias actual.
         *
         * setPreferencesFromResource: Infla el recurso XML y sustituye la jerarquía de preferencias
         * actual (si la hay) por la jerarquía de preferencias con raíz en la clave.
         */

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            val root = arguments?.getString(PREFERENCE_ROOT, rootKey)
            val prefResId = arguments?.getInt(PREFERENCE_RESOURCE_ID)

            root?.let {
                prefResId?.let {
                    setPreferencesFromResource(
                        it,
                        root
                    )
                }

            } ?: run {
                prefResId?.let {
                    addPreferencesFromResource(it)
                }
            }
        }

        /**
         * Gracias a este método y a las keys que hemos definido en el fichero xml, podemos acceder
         * a ellas y controlar los clicks que queramos dar un comportamiento concreto. En este caso
         * estamos recuperando la key prefs_about_developer_google_play y dándole un comportamiento
         * que para la prueba ha sido un simple mensaje con Toast.
         */

        override fun onPreferenceTreeClick(preference: Preference): Boolean {
            if (preference.key.equals(KEY_ABOUT_DEV_GOOGLE_PLAY_SETTINGS)) {
                Toast.makeText(context, "Hola", Toast.LENGTH_SHORT).show()
            }
            return super.onPreferenceTreeClick(preference)
        }

    }

}