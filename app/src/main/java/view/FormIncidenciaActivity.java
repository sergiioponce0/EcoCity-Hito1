package view;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecocity.R;
import controller.IncidenciaController;
import model.Incidencia;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormIncidenciaActivity extends AppCompatActivity {

    private EditText etTitulo, etDescripcion;
    private Spinner spinnerUrgencia;
    private Button btnGuardar;
    private IncidenciaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_incidencia);

        // Inicializar vistas
        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        spinnerUrgencia = findViewById(R.id.spinnerUrgencia);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Inicializar controlador
        controller = new IncidenciaController(this);

        // Configurar el Spinner (Selector de urgencia)
        String[] opcionesUrgencia = {"Baja", "Media", "Alta", "Crítica"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, opcionesUrgencia);
        spinnerUrgencia.setAdapter(adapter);

        // Acción del botón guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarIncidencia();
            }
        });
    }

    private void guardarIncidencia() {
        String titulo = etTitulo.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String urgencia = spinnerUrgencia.getSelectedItem().toString();

        // 1. Validaciones básicas (Hito 1: Formulario con validaciones)
        if (titulo.isEmpty()) {
            etTitulo.setError("El título es obligatorio");
            return;
        }
        if (descripcion.isEmpty()) {
            etDescripcion.setError("Describe el problema");
            return;
        }

        // 2. Obtener fecha actual
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        // 3. Crear objeto Incidencia
        Incidencia nuevaIncidencia = new Incidencia(titulo, descripcion, urgencia, fecha);

        // 4. Guardar en BBDD
        long resultado = controller.crearIncidencia(nuevaIncidencia);

        if (resultado != -1) {
            Toast.makeText(this, "Incidencia guardada", Toast.LENGTH_SHORT).show();
            finish(); // Cierra esta pantalla y vuelve a la lista
        } else {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }
}