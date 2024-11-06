package eu.andreatt.miprimeraaplicacion_prom

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Clase que administra la base de datos de tareas utilizando SQLite.
 * Proporciona métodos para guardar, obtener, actualizar y eliminar tareas.
 *
 * @param context Contexto de la aplicación.
 */
class Preferences(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "TasksDatabase.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "tasks"
        const val COLUMN_ID = "id"
        const val COLUMN_TASK = "task"
    }

    /**
     * Crea la tabla de tareas en la base de datos.
     *
     * @param db Instancia de la base de datos.
     */
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TASK TEXT NOT NULL
            )
        """
        db.execSQL(createTableQuery)
    }

    /**
     * Actualiza la estructura de la base de datos al eliminar la tabla antigua y crear una nueva.
     *
     * @param db Instancia de la base de datos.
     * @param oldVersion Versión anterior de la base de datos.
     * @param newVersion Nueva versión de la base de datos.
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    /**
     * Guarda una nueva tarea en la base de datos.
     *
     * @param task La tarea a guardar.
     */
    fun saveTask(task: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TASK, task)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    /**
     * Obtiene todas las tareas almacenadas en la base de datos.
     *
     * @return Lista mutable de tareas.
     */
    fun getTasks(): MutableList<String> {
        val tasks = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_TASK), null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                tasks.add(getString(getColumnIndexOrThrow(COLUMN_TASK)))
            }
            close()
        }
        db.close()
        return tasks
    }

    /**
     * Elimina una tarea de la base de datos.
     *
     * @param task La tarea a eliminar.
     */
    fun deleteTask(task: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_TASK = ?", arrayOf(task))
        db.close()
    }

    /**
     * Actualiza una tarea existente en la base de datos.
     *
     * @param oldTask La tarea actual a actualizar.
     * @param newTask El nuevo valor de la tarea.
     */
    fun updateTask(oldTask: String, newTask: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TASK, newTask)
        }
        val rowsUpdated = db.update(TABLE_NAME, values, "$COLUMN_TASK = ?", arrayOf(oldTask))
        Log.d("Preferences", "Rows updated: $rowsUpdated") // Agregar log para verificar
        db.close()
    }


}
