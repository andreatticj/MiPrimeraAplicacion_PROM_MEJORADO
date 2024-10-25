package eu.andreatt.miprimeraaplicacion_prom

import android.app.Application

/**
 * TaskApplication inicializa la aplicación y crea una instancia de Preferences para el almacenamiento persistente.
 */
class TaskApplication : Application() {

    companion object {
        // Instancia global de Preferences para acceso en toda la aplicación
        lateinit var prefs: Preferences
    }

    /**
     * Se ejecuta al iniciar la aplicación, inicializando las preferencias.
     */
    override fun onCreate() {
        super.onCreate()
        prefs = Preferences(baseContext)
    }
}
