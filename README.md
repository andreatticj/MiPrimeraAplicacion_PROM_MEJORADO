# MiPrimeraAplicacion_PROM

MiPrimeraAplicacion_PROM es una aplicación Android sencilla que permite gestionar una lista de tareas. Los usuarios pueden añadir nuevas tareas, marcar tareas como completadas, editarlas y almacenarlas de forma persistente utilizando `SharedPreferences`.

## Características

- **Agregar tareas**: Los usuarios pueden introducir una nueva tarea mediante un campo de entrada.
- **Eliminar tareas**: Las tareas completadas se pueden marcar como hechas, lo cual las elimina de la lista.
- **Persistencia de datos**: Las tareas se almacenan en `SharedPreferences`, permitiendo que se mantengan tras cerrar la aplicación.
- **Interfaz sencilla**: Un `RecyclerView` muestra la lista de tareas en formato de lista.

## Estructura del Proyecto

El proyecto está dividido en las siguientes clases principales:

- **MainActivity**: Controla la interfaz principal, maneja la lista de tareas y la interacción del usuario.
- **Preferences**: Encapsula el uso de `SharedPreferences` para guardar y recuperar la lista de tareas.
- **TaskAdapter**: Adaptador para el `RecyclerView` que muestra las tareas.
- **TaskApplication**: Clase de aplicación que inicializa las preferencias compartidas.
- **TaskViewHolder**: `ViewHolder` que gestiona los elementos de cada tarea en la lista.

## Instalación

1. Clona el repositorio:

   git clone https://github.com/andreatticj/MiPrimeraAplicacion_PROM.git

2. Abre el proyecto en Android Studio.
3. Ejecuta la aplicación en un dispositivo o emulador Android.

##Uso

    Abre la aplicación en tu dispositivo Android.
    Escribe una tarea en el campo de entrada y presiona "Añadir".
    Marca una tarea como completada tocando el icono correspondiente para eliminarla de la lista.
