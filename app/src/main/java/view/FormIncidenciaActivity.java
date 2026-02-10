package view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.ecocity.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import controller.IncidenciaController;
import model.Incidencia;

public class FormIncidenciaActivity extends AppCompatActivity {

    private EditText etTitulo, etDescripcion;
    private Spinner spinnerUrgencia;
    private Button btnGuardar;
    private IncidenciaController controller;
    private ImageView ivFoto;
    private Button btnFoto;

    // Variables para la cámara
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PERMISSION_CAMERA = 100;
    private String currentPhotoPath = null; // Aquí guardaremos la ruta de la foto

    // Variables para el mapa
    private Button btnMapa;
    private TextView tvCoordenadas;
    private double latitudSeleccionada = 0.0;
    private double longitudSeleccionada = 0.0;
    private static final int REQUEST_MAPA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_incidencia);

        // Inicializar vistas existentes
        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        spinnerUrgencia = findViewById(R.id.spinnerUrgencia);
        btnGuardar = findViewById(R.id.btnGuardar);

        // --- AQUÍ PEGAS LA PARTE DE LA CÁMARA (VISTAS) ---
        ivFoto = findViewById(R.id.ivFoto);
        btnFoto = findViewById(R.id.btnFoto);
        // --------------------------------------------------

        // Inicializar controlador
        controller = new IncidenciaController(this);

        // Configurar el Spinner (Selector de urgencia)
        String[] opcionesUrgencia = {"Baja", "Media", "Alta", "Crítica"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, opcionesUrgencia);
        spinnerUrgencia.setAdapter(adapter);

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FormIncidenciaActivity.this, "Botón pulsado", Toast.LENGTH_SHORT).show();
                verificarPermisosYAbriCamara();
            }
        });

        btnMapa = findViewById(R.id.btnMapa);
        tvCoordenadas = findViewById(R.id.tvCoordenadas);

        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormIncidenciaActivity.this, view.MapaActivity.class);
                startActivityForResult(intent, REQUEST_MAPA);
            }
        });

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

        // 1. Validaciones básicas
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

        // AÑADIMOS LA FOTO (Si existe)
        if (currentPhotoPath != null) {
            nuevaIncidencia.setFotoUri(currentPhotoPath);
        }

        // AÑADIMOS LAS COORDENADAS (Si existen)
        nuevaIncidencia.setLatitud(latitudSeleccionada);
        nuevaIncidencia.setLongitud(longitudSeleccionada);

        // 4. Guardar en BBDD
        long resultado = controller.crearIncidencia(nuevaIncidencia);

        if (resultado != -1) {
            Toast.makeText(this, "Incidencia guardada", Toast.LENGTH_SHORT).show();
            finish(); // Cierra esta pantalla y vuelve a la lista
        } else {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }

    // 1. Verificar permisos
    private void verificarPermisosYAbriCamara() {
        if (androidx.core.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            // Si no tenemos permiso, lo pedimos
            androidx.core.app.ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
        } else {
            // Si ya lo tenemos, abrimos cámara
            abrirCamara();
        }
    }

    // Gestionar la respuesta del usuario al permiso
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                abrirCamara();
            } else {
                Toast.makeText(this, "Se necesita permiso de cámara", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 2. Intent para abrir la cámara
    private void abrirCamara() {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = crearArchivoImagen();
            } catch (java.io.IOException ex) {
                Toast.makeText(this, "Error creando archivo", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                // Generar URI segura con FileProvider
                android.net.Uri photoURI = androidx.core.content.FileProvider.getUriForFile(this,
                        "com.example.ecocity.fileprovider", // OJO: Debe coincidir con tu Manifest
                        photoFile);
                takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // 3. Crear archivo temporal
    private File crearArchivoImagen() throws java.io.IOException {
        String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath(); // Guardamos la ruta para la BBDD
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Mostrar la foto en el ImageView
            android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(currentPhotoPath);
            ivFoto.setImageBitmap(bitmap);
            ivFoto.setVisibility(View.VISIBLE); // Hacemos visible la imagen
        }
        else if (requestCode == REQUEST_MAPA && resultCode == RESULT_OK) {
            latitudSeleccionada = data.getDoubleExtra("latitud", 0.0);
            longitudSeleccionada = data.getDoubleExtra("longitud", 0.0);

            tvCoordenadas.setText("Lat: " + latitudSeleccionada + "\nLon: " + longitudSeleccionada);
        }
    }
}