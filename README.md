🏀 Basketball Score App

Aplicación Android nativa para la gestión de marcadores de baloncesto.
Desarrollada como proyecto final del primer trimestre, implementando Data Binding, navegación entre actividades, persistencia de estado y diseño adaptativo (Responsive).



📱 Capturas de Pantalla

Pantalla Vertical
![Pantalla Vertical](assets/Vertical.png)

Pantalla Horizontal
![Pantalla Horizontal](assets/Horizontal.png)



🚀 Funcionalidades

Marcador en Tiempo Real: Gestión de puntos para equipo Local y Visitante.

Controles Completos: Botones para sumar (+1, +2, +3) y restar (-1).

Validaciones: Lógica de seguridad para evitar marcadores negativos.

Reset: Botón para reiniciar el partido a 0-0 rápidamente.

Análisis de Resultados: Pantalla final que determina automáticamente el ganador o si hubo empate, cambiando colores dinámicamente.

Rotación de Pantalla: Los datos no se pierden al girar el móvil gracias a onSaveInstanceState.

Diseño Adaptativo: Layout específico para modo horizontal (layout-land) que reorganiza las tarjetas para aprovechar el espacio.

🛠️ Aspectos Técnicos

El proyecto cumple con los requisitos técnicos avanzados solicitados:

Data Binding

Se ha eliminado completamente el uso de findViewById.

// Ejemplo de uso en MainActivity
binding.btnLocalPlus1.setOnClickListener(v -> updateLocal(1));

Intents Explícitos y Constantes

La comunicación entre MainActivity y ScoreActivity se realiza mediante Intents, utilizando una clase dedicada Constantes.java para evitar errores con las claves.

intent.putExtra(Constantes.EXTRA_LOCAL_SCORE, localScore);

Ciclo de Vida y Persistencia

Implementación de onSaveInstanceState para guardar los marcadores en un Bundle antes de que la actividad se destruya por rotación.

Interfaz de Usuario (UI)

Uso de ConstraintLayout para un diseño flexible.

CardView para agrupar la información de los equipos.

ScrollView para garantizar accesibilidad en pantallas pequeñas.

🐛 Diario de Errores y Soluciones (Learning Log)

Durante el desarrollo de esta aplicación, me enfrenté a varios desafíos que resolví de la siguiente manera:

1. Estructura de Archivos Incorrecta

Error: Creé la clase ScoreActivity dentro del archivo Marcador.java, impidiendo que Android la reconociera como pantalla independiente.
Solución: Separé las clases en archivos individuales (MainActivity.java, ScoreActivity.java) dentro del paquete correcto com.example.baloncesto.

2. Data Binding – "Cannot resolve symbol"

Error: Al acceder a binding.txtWinner, Android Studio marcaba error aunque el ID existía.
Solución: Caché corrupta del IDE → Build > Clean Project y luego Rebuild Project.

3. Error en AndroidManifest (App no iniciaba)

Error: La app se instalaba pero no abría en Android 12+.
Solución: Faltaba:

android:exported="true"


en la MainActivity del AndroidManifest.xml.

4. Desaparición de Colores del Tema

Error: Tras sobrescribir colors.xml, la app buscaba colores antiguos como color/background y color/accent.
Solución: Mapeé los nombres antiguos a los nuevos:

<color name="background">@color/bg_dark</color>

5. Problemas de Diseño (Layout)

Corte en Horizontal:
Las tarjetas se cortaban en modo landscape.
Solución: Crear un layout alternativo en layout-land reorganizando las tarjetas horizontalmente.

Espacio Superior Pegado:
La interfaz estaba demasiado pegada a la barra de estado.
Solución: Añadir:

android:paddingTop="100dp"


en el contenedor principal.
