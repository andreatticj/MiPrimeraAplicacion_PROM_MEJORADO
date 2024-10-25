package eu.andreatt.miprimeraaplicacion_prom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.andreatt.miprimeraaplicacion_prom.TaskApplication.Companion.prefs
/**
 * MainActivity es la pantalla principal que permite a los usuarios agregar y gestionar sus tareas.
 */
class MainActivity : AppCompatActivity() {

    // Botón para añadir una nueva tarea
    lateinit var btnAddTask: Button

    // Campo de texto donde el usuario escribe la nueva tarea
    lateinit var etTask: EditText

    // RecyclerView para mostrar la lista de tareas
    lateinit var rvTasks: RecyclerView

    // Adaptador para el RecyclerView
    lateinit var adapter: TaskAdapter

    // Lista de tareas actuales
    var tasks = mutableListOf<String>()

    /**
     * Se ejecuta al iniciar la pantalla principal.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi() // Inicializa la interfaz de usuario
    }

    /**
     * Inicializa las variables, listeners y el RecyclerView.
     */
    private fun initUi() {
        initVariables()   // Conecta los elementos de la interfaz con el código
        initListeners()   // Configura el comportamiento al hacer clic en botones
        initRecyclerView() // Configura la vista de lista
    }

    /**
     * Configura el RecyclerView: establece la disposición y conecta el adaptador de tareas.
     */
    private fun initRecyclerView() {
        tasks = prefs.getTasks()  // Carga tareas guardadas de preferencias
        rvTasks.layoutManager = LinearLayoutManager(this)
        adapter = TaskAdapter(tasks) { deleteTask(it) }  // Configura el adaptador
        rvTasks.adapter = adapter
    }

    /**
     * Elimina una tarea de la lista y guarda los cambios.
     */
    private fun deleteTask(position: Int) {
        tasks.removeAt(position)  // Elimina la tarea seleccionada
        adapter.notifyDataSetChanged()  // Actualiza la vista de la lista
        prefs.saveTasks(tasks)  // Guarda la lista actualizada en preferencias
    }

    /**
     * Conecta las variables de la interfaz con los elementos en el diseño XML.
     */
    private fun initVariables() {
        etTask = findViewById(R.id.etTask)
        btnAddTask = findViewById(R.id.btnAddTask)
        rvTasks = findViewById(R.id.rvTasks)
    }

    /**
     * Configura el botón para añadir una tarea cuando se hace clic en él.
     */
    private fun initListeners() {
        btnAddTask.setOnClickListener { addTask() }
    }

    /**
     * Añade una nueva tarea a la lista, guarda el cambio y limpia el campo de entrada.
     */
    private fun addTask() {
        val newTask = etTask.text.toString()  // Obtiene el texto ingresado
        tasks.add(newTask)  // Añade la nueva tarea a la lista
        prefs.saveTasks(tasks)  // Guarda la lista en preferencias
        adapter.notifyDataSetChanged()  // Notifica cambios en la vista
        etTask.setText("")  // Limpia el campo de texto
    }
}
