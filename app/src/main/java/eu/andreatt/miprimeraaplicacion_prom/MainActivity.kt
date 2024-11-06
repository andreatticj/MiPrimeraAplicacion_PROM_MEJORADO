package eu.andreatt.miprimeraaplicacion_prom

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.andreatt.miprimeraaplicacion_prom.TaskApplication.Companion.prefs

/**
 * La actividad principal de la aplicación que permite agregar, editar y eliminar tareas.
 * Incluye funcionalidad de deslizamiento para eliminar y la reproducción de un sonido al
 * eliminar una tarea.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var btnAddTask: Button
    private lateinit var etTask: EditText
    private lateinit var rvTasks: RecyclerView
    private lateinit var adapter: TaskAdapter
    private var tasks = mutableListOf<String>()
    lateinit var mediaPlayer: MediaPlayer // Declarar el MediaPlayer para reproducir sonidos

    /**
     * Método que se llama al crear la actividad.
     * Inicializa la interfaz de usuario y configura la funcionalidad de deslizamiento.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
        attachSwipeToDelete()

        // Inicializar MediaPlayer con el archivo de sonido
        mediaPlayer = MediaPlayer.create(this, R.raw.delete_sound)
    }

    /**
     * Inicializa los elementos de la interfaz de usuario, configura el adaptador y
     * recupera las tareas almacenadas en las preferencias.
     */
    private fun initUi() {
        etTask = findViewById(R.id.etTask)
        btnAddTask = findViewById(R.id.btnAddTask)
        rvTasks = findViewById(R.id.rvTasks)

        btnAddTask.setOnClickListener { addTask() }

        tasks = prefs.getTasks()
        rvTasks.layoutManager = LinearLayoutManager(this)

        // Configura el adaptador con la funcionalidad para editar y eliminar tareas
        adapter = TaskAdapter(tasks) { task ->
            val editText = EditText(this)
            editText.setText(task)

            AlertDialog.Builder(this)
                .setTitle("Editar tarea")
                .setView(editText)
                .setPositiveButton("Guardar") { _, _ ->
                    val updatedTask = editText.text.toString()
                    if (updatedTask.isNotEmpty() && updatedTask != task.toString()) {
                        prefs.updateTask(task.toString(), updatedTask) // Actualizar en la base de datos
                        tasks[tasks.indexOf(task.toString())] = updatedTask // Actualizar en la lista de tareas
                        adapter.notifyDataSetChanged() // Notificar al adaptador
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        rvTasks.adapter = adapter
    }

    /**
     * Añade una nueva tarea a la lista si el campo de texto no está vacío.
     * También guarda la tarea en las preferencias.
     */
    private fun addTask() {
        val newTask = etTask.text.toString()
        if (!newTask.isEmpty()) {
            tasks.add(newTask)
            prefs.saveTask(newTask)
            adapter.notifyItemInserted(tasks.size - 1)
            etTask.setText("")

        }
    }

    /**
     * Elimina una tarea de la lista y de las preferencias.
     * También reproduce un sonido de eliminación.
     *
     * @param task La tarea a eliminar.
     */
    private fun deletTask(task: String) {
        prefs.deleteTask(task)
        val position = tasks.indexOf(task)
        if (position >= 0) {
            tasks.removeAt(position)
            adapter.notifyItemRemoved(position) // Notificar solo el ítem eliminado
        }

        // Reproducir sonido de eliminación
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)  // Reiniciar a la posición inicial
        }
        mediaPlayer.start()
    }


    /**
     * Configura la funcionalidad de deslizamiento en el RecyclerView para eliminar tareas.
     * Permite deslizar hacia la izquierda o derecha para eliminar una tarea y cambia el
     * color de fondo durante el deslizamiento.
     */
    private fun attachSwipeToDelete() {
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task = tasks[position]
                deletTask(task)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    itemView.setBackgroundColor(Color.RED)
                    itemView.translationX = dX
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT)
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(rvTasks)
    }

    /**
     * Se llama cuando la actividad se destruye.
     * Libera los recursos utilizados por el MediaPlayer.
     */
    override fun onDestroy() {
        super.onDestroy()
        // Liberar el MediaPlayer cuando la actividad se destruya
        mediaPlayer.release()
    }
}
