package eu.andreatt.miprimeraaplicacion_prom

import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.andreatt.miprimeraaplicacion_prom.TaskApplication.Companion.prefs

/**
 * MainActivity es la actividad principal de la aplicación, donde se gestionan las tareas.
 * Permite a los usuarios agregar, visualizar y eliminar tareas.
 */
class MainActivity : AppCompatActivity() {

    // Declaración de variables para los elementos de la interfaz
    lateinit var btnAddTask: Button // Botón para agregar una nueva tarea
    lateinit var etTask: EditText // Campo de texto para ingresar la tarea
    lateinit var rvTasks: RecyclerView // RecyclerView para mostrar la lista de tareas

    lateinit var adapter: TaskAdapter // Adaptador para el RecyclerView

    var tasks = mutableListOf<String>() // Lista mutable para almacenar las tareas

    /**
     * Método que se llama al crear la actividad.
     * Inicializa la interfaz y los componentes necesarios.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Establece el diseño de la actividad
        initUi() // Inicializa la interfaz de usuario
        attachSwipeToDelete() // Configura la eliminación de tareas al deslizar
    }

    /**
     * Método que inicializa las variables y configura la interfaz de usuario.
     */
    private fun initUi() {
        initVariables() // Inicializa las variables de los elementos de la interfaz
        initListeners() // Configura los listeners para los botones
        initRecyclerView() // Inicializa el RecyclerView con las tareas
    }

    /**
     * Método que configura el RecyclerView para mostrar las tareas.
     * Se establece el LayoutManager y se asocia el adaptador.
     */
    private fun initRecyclerView() {
        tasks = prefs.getTasks() // Obtiene la lista de tareas guardadas
        rvTasks.layoutManager = LinearLayoutManager(this) // Establece el LayoutManager
        adapter = TaskAdapter(tasks) { deleteTask(it) } // Crea el adaptador con las tareas y la función de eliminación
        rvTasks.adapter = adapter // Asocia el adaptador al RecyclerView
    }

    /**
     * Método que permite eliminar una tarea de la lista.
     * Notifica al adaptador que se ha eliminado un elemento y actualiza las preferencias.
     *
     * @param position La posición de la tarea a eliminar en la lista.
     */
    private fun deleteTask(position: Int) {
        tasks.removeAt(position) // Elimina la tarea de la lista
        adapter.notifyItemRemoved(position) // Notifica al adaptador que se ha eliminado el elemento
        prefs.saveTasks(tasks) // Guarda la lista actualizada de tareas
    }

    /**
     * Método que inicializa las variables de los elementos de la interfaz.
     */
    private fun initVariables() {
        etTask = findViewById(R.id.etTask) // Inicializa el campo de texto para la tarea
        btnAddTask = findViewById(R.id.btnAddTask) // Inicializa el botón para agregar tareas
        rvTasks = findViewById(R.id.rvTasks) // Inicializa el RecyclerView para mostrar las tareas
    }

    /**
     * Método que configura los listeners para los botones de la interfaz.
     */
    private fun initListeners() {
        // Configura el listener para el botón de agregar tarea
        btnAddTask.setOnClickListener { addTask() }
    }

    /**
     * Método que agrega una nueva tarea a la lista.
     * Guarda la lista actualizada de tareas y limpia el campo de texto.
     */
    private fun addTask() {
        val newTask = etTask.text.toString() // Obtiene el texto ingresado como nueva tarea
        tasks.add(newTask) // Agrega la nueva tarea a la lista
        prefs.saveTasks(tasks) // Guarda la lista actualizada de tareas
        adapter.notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
        etTask.setText("") // Limpia el campo de texto
    }

    /**
     * Configura el gesto de deslizamiento para eliminar una tarea.
     */
    private fun attachSwipeToDelete() {
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            /**
             * Método que maneja el movimiento de un elemento dentro del RecyclerView.
             * En este caso, no se utiliza, así que devuelve false.
             *
             * @param recyclerView El RecyclerView donde ocurre el movimiento.
             * @param viewHolder El ViewHolder del elemento que se está moviendo.
             * @param target El ViewHolder del elemento objetivo donde se está moviendo.
             * @return false porque no se usa el movimiento.
             */
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // No se utiliza el movimiento vertical
            }

            /**
             * Método que se llama para dibujar el ítem mientras se desliza.
             * Cambia el color del fondo del ítem a rojo mientras se desliza.
             *
             * @param c Canvas donde se dibuja el RecyclerView.
             * @param recyclerView El RecyclerView.
             * @param viewHolder El ViewHolder del ítem que se desliza.
             * @param dX Desplazamiento en X durante el deslizamiento.
             * @param dY Desplazamiento en Y durante el deslizamiento.
             * @param actionState Estado actual del gesto (deslizar, arrastrar, etc.).
             * @param isCurrentlyActive Indica si el gesto está activo.
             */
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
                    val itemView = viewHolder.itemView // Obtiene la vista del ítem

                    // Cambia el color del fondo del ítem a rojo mientras se desliza
                    itemView.setBackgroundColor(Color.RED)

                    // Aplica la traducción en X para el desplazamiento
                    itemView.translationX = dX
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            /**
             * Método que se llama cuando un ítem ha sido deslizado.
             * Llama a la función de eliminación después del deslizamiento.
             *
             * @param viewHolder El ViewHolder que fue deslizado.
             * @param direction La dirección en la que se deslizó (izquierda o derecha).
             */
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition // Obtiene la posición del ítem deslizado
                deleteTask(position) // Llama a la función de eliminación
            }

            /**
             * Método que se llama para limpiar la vista del ítem después de que se ha deslizado.
             * Restablece el color de fondo al original.
             *
             * @param recyclerView El RecyclerView donde se encuentra el ítem.
             * @param viewHolder El ViewHolder del ítem que fue deslizado.
             */
            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                // Restablece el color de fondo al original después de que el usuario deja de deslizar
                viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT)
            }
        }

        // Asocia el ItemTouchHelper al RecyclerView
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(rvTasks)
    }
}
