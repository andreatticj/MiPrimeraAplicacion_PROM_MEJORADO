package eu.andreatt.miprimeraaplicacion_prom

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * TaskViewHolder representa y gestiona la vista de una tarea en el RecyclerView.
 * @param view La vista de cada tarea.
 */
class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    // TextView para mostrar el nombre de la tarea
    private val tvTask: TextView = view.findViewById(R.id.tvTask)

    // ImageView para indicar que la tarea está completa
    private val ivTaskDone: ImageView = view.findViewById(R.id.ivTaskDone)

    /**
     * Configura la vista de la tarea y añade un listener para marcarla como completada.
     * @param task La tarea a mostrar en esta vista.
     * @param onItemDone Callback cuando la tarea se marca como completada.
     */
    fun render(task: String, onItemDone: (Int) -> Unit) {
        tvTask.text = task
        ivTaskDone.setOnClickListener { onItemDone(adapterPosition) } // Marca la tarea como hecha
    }
}
