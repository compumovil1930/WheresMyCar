package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ratatouille.models.Chef;

public class DescripcionChefsCercanos extends AppCompatActivity {

    Bundle bundle;
    TextView editTextChefName;
    ImageView imageViewChef;
    RatingBar ratingBarChef;

    LinearLayout linearLayoutRecetas;
    LinearLayout linearLayoutHerramientas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_chefs_cercanos);

        bundle = getIntent().getBundleExtra("bundle");
        Chef chef = (Chef) bundle.getSerializable("ChefSeleccionado");

        editTextChefName = (TextView) findViewById(R.id.textViewName);
        editTextChefName.setText(chef.getNombre());

        ratingBarChef = (RatingBar) findViewById(R.id.ratingBarChef);
        ratingBarChef.setRating((int) chef.getCalificacion());

        addRecetas(chef);
        addHerramientas(chef);
    }

    private void addRecetas(Chef chef) {

        linearLayoutRecetas = (LinearLayout) findViewById(R.id.LinearLayoutRecetas);

        for (int i = 0; i < chef.getListaRecetas().size(); i++){

            TextView textViewReceta = new TextView(getApplicationContext());

            textViewReceta.setText(chef.getListaRecetas().get(i).getNombre());
            textViewReceta.append("\n" + chef.getListaRecetas().get(i).getTiempo() + " minutos");
            textViewReceta.setPadding(15,10, 15,10);

            linearLayoutRecetas.addView(textViewReceta);
        }
    }

    private void addHerramientas(Chef chef) {

        linearLayoutRecetas = (LinearLayout) findViewById(R.id.LinearLayoutHerramientas);

        for (int i = 0; i < chef.getListaRecetas().size(); i++){

            TextView textViewHerramienta = new TextView(getApplicationContext());

            textViewHerramienta.setText(chef.getListaHerramientas().get(i).getNombre());
            textViewHerramienta.setPadding(15,10, 15,10);

            linearLayoutRecetas.addView(textViewHerramienta);
        }
    }
}
