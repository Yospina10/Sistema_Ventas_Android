package com.example.sistemaventasfb;

import static java.lang.Double.parseDouble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class sales extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String idAutomatic;
    double mTotcomision = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        EditText idsellers = findViewById(R.id.etidsellersale);
        EditText idsale = findViewById(R.id.etidsale);
        EditText datesale = findViewById(R.id.etdatesale);
        EditText salevalue = findViewById(R.id.etsalevalue);
        ImageButton btnsavesale = findViewById(R.id.btnsavesale);
        ImageButton btnback = findViewById(R.id.btnback);
        //Recibir la identificacion enviada desde la actividad MainActivity
        idsellers.setText(getIntent().getStringExtra("eidseller"));
        idAutomatic = getIntent().getStringExtra("eidautomatic");
        mTotcomision = parseDouble(getIntent().getStringExtra("totalcomision"));
        //Eventos
        btnsavesale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verificar qye tidis kis datis estén diligenciados
                String xidsale = idsale.getText().toString();
                String xdatesale = datesale.getText().toString();
                String xsalevalue = salevalue.getText().toString();
                if(!xidsale.isEmpty() && !xdatesale.isEmpty() && !xsalevalue.isEmpty()) {
                    //Guardar la venta
                    Map<String, Object> cSales = new HashMap<>();
                    cSales.put("idsale", idsale.getText().toString());
                    cSales.put("idseller", idsellers.getText().toString());
                    cSales.put("datesale", datesale.getText().toString());
                    cSales.put("salevalue", parseDouble(salevalue.getText().toString()));

                    db.collection("sale")
                            .whereEqualTo("idsale", idsale.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (!task.getResult().isEmpty()) {
                                            Toast.makeText(getApplicationContext(),"Usuario Existente", Toast.LENGTH_SHORT).show();

                                        } else {
                                            //Agregar el documento a la coleccion seller a través de la tabla temporal seller
                                            db.collection("seller")
                                                    .add(cSales)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Toast.makeText(getApplicationContext(), "Datos ingresados correctamente", Toast.LENGTH_SHORT).show();
                                                            idsale.setText("");
                                                            idsellers.setText("");
                                                            datesale.setText("");

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getApplicationContext(), "Error al guardar los datos del vendedor...", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                }
                            });
                    db.collection("sales")
                            .add(cSales)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    //Buscar el idseller para que retorne el total de la comisión actual
                                    //para acumular el total de la comisión con base en la comisión de la venta
                                    Intent iMain = new Intent(getApplicationContext(),MainActivity.class);
                                    db.collection("seller").document(idAutomatic)
                                                    .update("totalcomision", mTotcomision + parseDouble(salevalue.getText().toString())*0.02)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Toast.makeText(getApplicationContext(), "Venta guardada correctamente...", Toast.LENGTH_SHORT).show();
                                                                    startActivity(iMain);
                                                                }
                                                            });

                                    idsale.setText("");
                                    idsellers.setText("");
                                    datesale.setText("");
                                    salevalue.setText("");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "No se guardó la venta...", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });
    }
}

