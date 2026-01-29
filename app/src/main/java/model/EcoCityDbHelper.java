package model; // Asegúrate que coincida con tu paquete

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EcoCityDbHelper extends SQLiteOpenHelper {

    // Versión de la BBDD. Si cambias la estructura de la tabla en el futuro, sube este número.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EcoCity.db";

    // Nombres de la tabla y columnas
    public static final String TABLE_INCIDENCIAS = "incidencias";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITULO = "titulo";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_URGENCIA = "urgencia"; // Bajo, Medio, Alto
    public static final String COLUMN_FECHA = "fecha";

    // Columnas para Hito 2 (Multimedia y GPS) - Las preparamos ya
    public static final String COLUMN_FOTO_URI = "foto_uri"; // Ruta del archivo
    public static final String COLUMN_AUDIO_URI = "audio_uri"; // Ruta del archivo
    public static final String COLUMN_LATITUD = "latitud";
    public static final String COLUMN_LONGITUD = "longitud";

    // Columna para Hito 3 (Sincronización)
    public static final String COLUMN_ESTADO_SYNC = "estado_sync"; // 0 = No sinc, 1 = Sincronizado

    // Sentencia SQL para crear la tabla
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_INCIDENCIAS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TITULO + " TEXT," +
                    COLUMN_DESCRIPCION + " TEXT," +
                    COLUMN_URGENCIA + " TEXT," +
                    COLUMN_FECHA + " TEXT," +
                    COLUMN_FOTO_URI + " TEXT," +
                    COLUMN_AUDIO_URI + " TEXT," +
                    COLUMN_LATITUD + " REAL," +
                    COLUMN_LONGITUD + " REAL," +
                    COLUMN_ESTADO_SYNC + " INTEGER DEFAULT 0)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_INCIDENCIAS;

    public EcoCityDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Se ejecuta la primera vez que se abre la app
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se ejecuta si cambias la versión de la DB. Aquí borramos y creamos de nuevo (simple).
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}