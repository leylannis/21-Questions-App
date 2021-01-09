package com.example.cop4656_assign4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;

public class CreateActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference mDatabase;
    String question = "";
    String message = "";
    Button addButton, cancelButton, deleteButton;
    EditText questionText;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        addButton = findViewById(R.id.addButton);
        cancelButton = findViewById(R.id.cancelButton);
        deleteButton = findViewById(R.id.deleteButton);
        questionText = findViewById(R.id.questionEditText);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionText.setTextColor(BLACK);
                question = questionText.getText().toString().trim();
                if (question.isEmpty()) {               // empty error
                    Toast.makeText(getApplicationContext(), "Question is empty!", Toast.LENGTH_SHORT).show();
                    questionText.setTextColor(RED);
                }
                if (question.trim().length() > 120) {   // too long error
                    Toast.makeText(getApplicationContext(), "Question is too long", Toast.LENGTH_SHORT).show();
                    questionText.setTextColor(RED);
                }
                if (!question.isEmpty() && question.trim().length() < 120) {   // not empty, less than 120 chars
                    final boolean[] flag = {false};     // set to true if a match is found
                    DatabaseReference ref = mDatabase.getRef();
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {  // unique verification, ignoring case
                            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                if (snap.getValue(String.class).equalsIgnoreCase(question) && !snap.getValue(String.class).equalsIgnoreCase("")) {
                                    //Toast.makeText(getApplicationContext(), "Question already exists!", Toast.LENGTH_SHORT).show();
                                    flag[0] = true;
                                    break;
                                }
                            }
                            if (flag[0] == false) {
                                mDatabase.push().setValue(question);
                                Toast.makeText(getApplicationContext(), "Question successfully saved!", Toast.LENGTH_SHORT).show();
                            }
                            if (flag[0] == true){
                                Toast.makeText(getApplicationContext(), "Question already exists!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // TO DO: NOT YET IMPLEMENTED
                        }
                    });

                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = mDatabase.getRef();
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            if(snap.getValue(String.class) == null)
                                break;
                            snap.getRef().removeValue();
                        }
                        Toast.makeText(getApplicationContext(), "Database successfuly cleared.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // TO DO: NOT YET IMPLEMENTED
                    }
                });
            }
        });
    }
}