EcoCity – Hito 1: Experiencia de Usuario y Persistencia Local

Proyecto Integrador 2º Trimestre
Módulos: Programación Multimedia (PMDM) | Servicios y Procesos (PSP) | Desarrollo de Interfaces (DI)

1. Descripción del Proyecto

EcoCity es una aplicación móvil Android diseñada para que los ciudadanos puedan reportar incidencias urbanas (baches, farolas rotas, basura acumulada) en tiempo real. El proyecto combina persistencia local y sincronización en la nube, ofreciendo robustez y funcionalidad offline.

El Hito 1 se centra en la experiencia de usuario y la persistencia local, asegurando que la app sea visualmente intuitiva y que los datos puedan almacenarse y gestionarse en el dispositivo sin necesidad de conexión a internet.

2. Objetivos del Hito 1

Interfaz de Usuario (DI):

Pantalla de Login con diseño visual y validación de campos.

Listado de incidencias usando RecyclerView con tarjetas personalizadas para cada incidencia.

Formulario de alta de incidencias con validaciones de entrada (título, descripción, urgencia).

Datos Locales (PMDM):

Implementación de base de datos local SQLite.

Operaciones CRUD completas (Crear, Leer, Editar, Borrar) para incidencias.

Soporte de la aplicación offline, con sincronización futura al detectar conexión.

3. Funcionalidades Implementadas

Login local básico: Validación de email y contraseña en el dispositivo.

Gestión de incidencias:

Crear nueva incidencia.

Visualizar listado completo de incidencias en pantalla principal.

Editar o eliminar incidencias existentes.

Persistencia local: Todos los datos se almacenan en SQLite, permitiendo el uso de la app sin conexión a internet.

Interfaz amigable: Uso de RecyclerView y tarjetas personalizadas para mejorar la experiencia del usuario
