package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class AddPayMethod extends AppCompatActivity {

    EditText mmaa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_metodo_de_pago);
        mmaa = findViewById(R.id.MMAA);
        mmaa.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                boolean first = true;
                if (editable.length() == 2 && first == true) {
                    first = false;
                    editable.append('/');
                }


            }
        });
    }
}
