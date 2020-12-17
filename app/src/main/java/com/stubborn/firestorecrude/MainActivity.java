package com.stubborn.firestorecrude;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText name, description;
    Button save, showaall;
    FirebaseFirestore fb;
    private String uId, uiName, uiDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        save = findViewById(R.id.save_btn);
        showaall = findViewById(R.id.show_btn);

        fb = FirebaseFirestore.getInstance();

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            save.setText("update");
            uId = bundle.getString("uid");
            uiName = bundle.getString("uname");
            uiDesc = bundle.getString("udesc");
            description.setText(uiDesc);
            name.setText(uiName);
        } else {
            save.setText("save");
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String NAME = name.getText().toString();
                String DESCRIPTION = description.getText().toString();

                Bundle bundle1 = getIntent().getExtras();
                if (bundle1 != null) {
                    String id = uId;
                    updateToFirestore(id, NAME, DESCRIPTION);
                } else {
                    String ID = UUID.randomUUID().toString();
                    saveTofirestrore(ID, NAME, DESCRIPTION);
                }
            }
        });

        showaall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ShowActivity.class));
            }
        });

    }

    private void updateToFirestore(String id, String name, String description) {
        fb.collection("Documents").document(id).update("name",name,"description",description).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "somethijg went wrong"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveTofirestrore(String id, String name, String description) {

        if (!name.isEmpty() && !description.isEmpty()) {

            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            map.put("description", description);

            fb.collection("Documents").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Data Saved Succesfully", Toast.LENGTH_SHORT).show();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Empty fields not allowed", Toast.LENGTH_SHORT).show();
        }
    }
}
