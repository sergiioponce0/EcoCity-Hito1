package view; // CORREGIDO: Ruta completa del paquete

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
// import android.widget.Toast; // Ya no necesitamos Toast

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecocity.R;

import controller.IncidenciaController;
import model.Incidencia;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAgregar;
    private IncidenciaController controller;
    private IncidenciaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Inicializamos el Controlador
        controller = new IncidenciaController(this);

        // 2. Vinculamos la vista
        recyclerView = findViewById(R.id.rvIncidencias);
        fabAgregar = findViewById(R.id.fabAgregar);

        // 3. Configuramos el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 4. Cargamos los datos
        cargarDatos();

        // 5. Acción del botón flotante (+)
        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CORREGIDO: Ahora abrimos la pantalla de formulario de verdad
                Intent intent = new Intent(MainActivity.this, FormIncidenciaActivity.class);
                startActivity(intent);
            }
        });
    }

    private void cargarDatos() {
        List<Incidencia> lista = controller.obtenerTodasLasIncidencias();
        // Asegúrate de que IncidenciaAdapter también tenga el paquete correcto en su archivo
        adapter = new IncidenciaAdapter(lista);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Al volver del formulario, recargamos la lista para ver la nueva incidencia
        cargarDatos();
    }
}