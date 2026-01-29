package controller; // Revisa tu paquete

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import model.EcoCityDbHelper;
import model.Incidencia;

import java.util.ArrayList;
import java.util.List;

public class IncidenciaController {

    private EcoCityDbHelper dbHelper;
    private Context context;

    public IncidenciaController(Context context) {
        this.context = context;
        this.dbHelper = new EcoCityDbHelper(context);
    }

    // --- CREATE: Guardar una nueva incidencia ---
    public long crearIncidencia(Incidencia incidencia) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EcoCityDbHelper.COLUMN_TITULO, incidencia.getTitulo());
        values.put(EcoCityDbHelper.COLUMN_DESCRIPCION, incidencia.getDescripcion());
        values.put(EcoCityDbHelper.COLUMN_URGENCIA, incidencia.getUrgencia());
        values.put(EcoCityDbHelper.COLUMN_FECHA, incidencia.getFecha());
        // El estado de sync por defecto es 0 en la BBDD, no hace falta ponerlo

        // Insertar fila
        long newRowId = db.insert(EcoCityDbHelper.TABLE_INCIDENCIAS, null, values);
        db.close();
        return newRowId; // Devuelve el ID de la fila creada o -1 si hubo error
    }

    // --- READ: Obtener todas las incidencias (Para el RecyclerView) ---
    public List<Incidencia> obtenerTodasLasIncidencias() {
        List<Incidencia> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Seleccionamos todas las columnas
        Cursor cursor = db.query(
                EcoCityDbHelper.TABLE_INCIDENCIAS,
                null, null, null, null, null,
                EcoCityDbHelper.COLUMN_ID + " DESC" // Ordenar por ID descendente (las nuevas primero)
        );

        if (cursor.moveToFirst()) {
            do {
                // Extraer datos del cursor
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(EcoCityDbHelper.COLUMN_ID));
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow(EcoCityDbHelper.COLUMN_TITULO));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(EcoCityDbHelper.COLUMN_DESCRIPCION));
                String urgencia = cursor.getString(cursor.getColumnIndexOrThrow(EcoCityDbHelper.COLUMN_URGENCIA));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow(EcoCityDbHelper.COLUMN_FECHA));

                // Estos pueden ser null ahora mismo, manejamos la excepción o recuperamos strings
                String foto = cursor.getString(cursor.getColumnIndexOrThrow(EcoCityDbHelper.COLUMN_FOTO_URI));
                String audio = cursor.getString(cursor.getColumnIndexOrThrow(EcoCityDbHelper.COLUMN_AUDIO_URI));
                double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(EcoCityDbHelper.COLUMN_LATITUD));
                double lon = cursor.getDouble(cursor.getColumnIndexOrThrow(EcoCityDbHelper.COLUMN_LONGITUD));
                int sync = cursor.getInt(cursor.getColumnIndexOrThrow(EcoCityDbHelper.COLUMN_ESTADO_SYNC));

                Incidencia inc = new Incidencia(id, titulo, desc, urgencia, fecha, foto, audio, lat, lon, sync);
                lista.add(inc);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    // Más adelante añadiremos aquí el método "borrarIncidencia" y "actualizarIncidencia"
}