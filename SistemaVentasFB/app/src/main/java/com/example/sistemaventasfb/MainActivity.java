package com.example.sistemaventasfb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //se genera un objeto para conectarse a la base de datos de firebase - firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String idAutomatic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Referenciar los IDs del archivo activity_main.xml
        EditText idseller = findViewById(R.id.etidseller);
        EditText fullname = findViewById(R.id.etfullname);
        EditText email= findViewById(R.id.etemail);
        EditText password = findViewById(R.id.etpassword);
        TextView totalcomision = findViewById(R.id.tvtotalcomision);
        ImageButton btnsave = findViewById(R.id.btnsave);
        ImageButton btnsearch = findViewById(R.id.btnsearch);
        ImageButton btnedit = findViewById(R.id.btnedit);
        ImageButton btndelete = findViewById(R.id.btndelete);
        ImageButton btnsales = findViewById(R.id.btnsales);
        ImageButton btnlist = findViewById(R.id.btnlist );
        //Eventos


        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Buscar por idseller y recuperar todos los datos
                db.collection("seller")
                        .whereEqualTo("idseller", idseller.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().isEmpty()) {
                                        //La instantanea tiene informacion del documento
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            idAutomatic = document.getId();
                                            //Mostrar la información en cada uno de los objetos referenciados
                                            fullname.setText(document.getString("fullname"));
                                            email.setText(document.getString("email"));
                                            totalcomision.setText(String.valueOf(document.getDouble("totalcomision")));
                                        }
                                    } else {
                                        //Si no encuentra el idseller del vendedor
                                        Toast.makeText(getApplicationContext(), "Id Vendedor NO Existe...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

                        }
                        });

                btnedit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> mseller = new HashMap<>();
                        mseller.put("idseller", idseller.getText().toString());
                        mseller.put("fullname", fullname.getText().toString());
                        mseller.put("email", email.getText().toString());
                        mseller.put("password", password.getText().toString());
                        mseller.put("totalcomision", 0);

                        db.collection("seller").document(idAutomatic)
                                .set(mseller)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Vendedor Actualizado Correctamente...", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error al guardar los datos del vendedor...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Validación de que los datos esten bien diligenciados
                        String midseller = idseller.getText().toString();
                        String mfullname = fullname.getText().toString();
                        String memail = email.getText().toString();
                        String mpassword = password.getText().toString();

                        if (!midseller.isEmpty() && !mfullname.isEmpty() && !memail.isEmpty() && !mpassword.isEmpty()) {
                            //Crear una tabla temporal con los mismos campos de la coleccion seller
                            Map<String, Object> mSeller = new HashMap<>();
                            mSeller.put("idseller", midseller);
                            mSeller.put("fullname", mfullname);
                            mSeller.put("email", memail);
                            mSeller.put("password", mpassword);
                            mSeller.put("totalcomision", 0);

                            db.collection("seller")
                                    .whereEqualTo("idseller", idseller.getText().toString())
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
                                                            .add(mSeller)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    Toast.makeText(getApplicationContext(), "Datos ingresados correctamente", Toast.LENGTH_SHORT).show();
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
                            //Agregar el documento a la coleccion seller a través de la tabla temporal seller
                            db.collection("seller")
                                    .add(mSeller)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Datos ingresados correctamente", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Error al guardar los datos del vendedor...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Por favor diligenciar todos campos...", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }
}