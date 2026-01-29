package view; // Revisa tu paquete

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecocity.R;
import model.Incidencia;

import java.util.List;

public class IncidenciaAdapter extends RecyclerView.Adapter<IncidenciaAdapter.IncidenciaViewHolder> {

    private List<Incidencia> listaIncidencias;

    // Constructor: Recibimos la lista de datos
    public IncidenciaAdapter(List<Incidencia> listaIncidencias) {
        this.listaIncidencias = listaIncidencias;
    }

    // Este método crea el "molde" visual (infla el layout item_incidencia)
    @NonNull
    @Override
    public IncidenciaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_incidencia, parent, false);
        return new IncidenciaViewHolder(view);
    }

    // Este método rellena los datos en cada posición
    @Override
    public void onBindViewHolder(@NonNull IncidenciaViewHolder holder, int position) {
        Incidencia incidencia = listaIncidencias.get(position);

        holder.tvTitulo.setText(incidencia.getTitulo());
        holder.tvFecha.setText(incidencia.getFecha());
        holder.tvUrgencia.setText(incidencia.getUrgencia());
        holder.tvDescripcion.setText(incidencia.getDescripcion());
    }

    // Devuelve cuántos elementos hay en la lista
    @Override
    public int getItemCount() {
        return listaIncidencias.size();
    }

    // Clase interna para "agarrar" los elementos de la vista
    public static class IncidenciaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvFecha, tvUrgencia, tvDescripcion;

        public IncidenciaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvUrgencia = itemView.findViewById(R.id.tvUrgencia);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
        }
    }
}