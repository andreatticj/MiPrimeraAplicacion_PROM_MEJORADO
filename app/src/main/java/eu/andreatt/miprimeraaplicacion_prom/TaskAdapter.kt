package eu.andreatt.miprimeraaplicacion_prom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.andreatt.miprimeraaplicacion_prom.TaskApplication.Companion.prefs

/**
 * TaskAdapter es el adaptador para mostrar las tareas en un RecyclerView.
 *
 * @param tasks Lista de tareas que se mostrará.
 * @param onTaskClick Callback que se invoca cuando se hace clic en una tarea.
 */
class TaskAdapter(
    private val tasks: MutableList<String>,
    private val onTaskClick: (String) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    /**
     * Infla la vista para cada elemento de la lista de tareas.
     *
     * @param parent El ViewGroup padre al que se añadirá la nueva vista.
     * @param viewType Tipo de vista de la nueva vista (no usado en este adaptador).
     * @return Un nuevo ViewHolder con la vista de tarea inflada.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    /**
     * Vincula los datos de una tarea a la vista correspondiente en el ViewHolder.
     *
     * @param holder ViewHolder que contendrá la tarea.
     * @param position Posición de la tarea en la lista.
     */
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
        holder.itemView.setOnClickListener {
            onTaskClick(task) // Llama a la función de clic pasando la tarea como argumento
        }
    }

    /**
     * Devuelve el número de tareas en la lista.
     *
     * @return Tamaño de la lista de tareas.
     */
    override fun getItemCount(): Int = tasks.size

    /**
     * Elimina una tarea de la lista y de la base de datos.
     *
     * @param position La posición de la tarea a eliminar.
     */
    fun removeTask(position: Int) {
        val taskToDelete = tasks[position]
        tasks.removeAt(position) // Elimina de la lista
        prefs.deleteTask(taskToDelete) // Elimina de la base de datos
        notifyItemRemoved(position) // Actualiza el RecyclerView
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskTextView: TextView = itemView.findViewById(R.id.tvTask)
        private val taskDoneIcon: ImageView = itemView.findViewById(R.id.ivTaskDone)

        fun bind(task: String) {
            taskTextView.text = task

            // Elimina la tarea al hacer clic en el icono de verificación
            taskDoneIcon.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    removeTask(position) // Llama a removeTask para eliminar de la lista y la base de datos
                }
            }
        }
    }
}