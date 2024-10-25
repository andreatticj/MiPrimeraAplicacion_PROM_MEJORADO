package eu.andreatt.miprimeraaplicacion_prom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * TaskAdapter es el adaptador para mostrar las tareas en un RecyclerView.
 * @param tasks Lista de tareas que se mostrará.
 * @param onItemDone Callback para indicar cuando una tarea se marca como completada.
 */
class TaskAdapter(
    private val tasks: List<String>,
    private val onItemDone: (Int) -> Unit
) : RecyclerView.Adapter<TaskViewHolder>() {

    /**
     * Crea un nuevo ViewHolder para mostrar una tarea.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TaskViewHolder(layoutInflater.inflate(R.layout.item_task, parent, false))
    }

    /**
     * Devuelve el número total de tareas en la lista.
     */
    override fun getItemCount() = tasks.size

    /**
     * Llama al método render para actualizar el contenido de cada elemento del RecyclerView.
     */
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.render(tasks[position], onItemDone)
    }
}
