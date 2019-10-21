package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String working = s.toString();
                boolean isValid = true;
                mmaa.setError(null);
                if (working.length() == 2 && before == 0) {
                    if (Integer.parseInt(working) < 1 || Integer.parseInt(working) > 12) {
                        isValid = false;
                    } else {
                        working += "/";
                        mmaa.setText(working);
                        mmaa.setSelection(working.length());
                    }
                } else if (working.length() == 5 && before == 0) {
                    String enteredYear = working.substring(3);
                    DateFormat df = new SimpleDateFormat("yy");
                    String formattedDate = df.format(Calendar.getInstance().getTime());
                    int currentYear = Integer.parseInt(formattedDate);
                    if (Integer.parseInt(enteredYear) < currentYear) {
                        isValid = false;
                    }
                } else if (working.length() != 5) {
                    isValid = false;
                } else
                    isValid = true;

                if (!isValid) {
                    mmaa.setError("Enter a valid date: MM/YY");
                } else {
                    mmaa.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
