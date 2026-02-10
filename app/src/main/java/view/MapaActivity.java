package view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ecocity.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        // Inicializar el fragmento del mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    /**
     * Este método se ejecuta cuando el mapa está listo para usarse.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Configuración inicial: Mover la cámara a una posición por defecto (Ej: Madrid)
        LatLng madrid = new LatLng(40.416775, -3.703790);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(madrid, 10));

        // Listener: Qué pasa cuando el usuario toca el mapa
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                // 1. Poner un marcador visual
                mMap.clear(); // Borrar marcadores anteriores
                mMap.addMarker(new MarkerOptions().position(latLng).title("Incidencia aquí"));

                // 2. Confirmar selección
                Toast.makeText(MapaActivity.this, "Ubicación seleccionada", Toast.LENGTH_SHORT).show();

                // 3. Devolver datos al formulario
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitud", latLng.latitude);
                resultIntent.putExtra("longitud", latLng.longitude);
                setResult(RESULT_OK, resultIntent);

                // 4. Cerrar el mapa y volver tras un pequeño retraso o inmediatamente
                finish();
            }
        });
    }
}