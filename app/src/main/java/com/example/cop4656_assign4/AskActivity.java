package com.example.cop4656_assign4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class AskActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference mDatabase; // to read and write data to database
    Button askButton, addButton, clearButton;
    TextView questionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

        askButton = findViewById(R.id.askButton);
        addButton = findViewById(R.id.addButton);
        questionView = findViewById(R.id.questionTextView);
        clearButton = findViewById(R.id.clearButton);

        askButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String[] newQuestion = new String[1];
                DatabaseReference ref = mDatabase.getRef();
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int childrenCount = (int) dataSnapshot.getChildrenCount();
                        int i = 0;
                        if (childrenCount > 0){
                            int randomNumber = new Random().nextInt(childrenCount);
                            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                // if (snap.getRef() == null) break;
                                if (i == randomNumber) {
                                    newQuestion[0] = snap.getValue(String.class);
                                    break;
                                }
                                i++;
                            }
                            questionView.setTextColor(Color.BLACK);
                            questionView.setText(newQuestion[0]);
                        }
                        else{
                            questionView.setTextColor(Color.parseColor("#FF9999"));
                            questionView.setText("No Questions Available\nPlease Add A Question");
                        }
                       // questionView.setText(newQuestion[0]);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionView.setText("");
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start CreateActivity to add a new question
                Intent intent = new Intent(getApplicationContext(), CreateActivity.class);
                startActivity(intent);
            }
        });
    }
}