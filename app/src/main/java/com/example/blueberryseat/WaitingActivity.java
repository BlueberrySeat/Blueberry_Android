package com.example.blueberryseat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.blueberryseat.MainActivity.mDatabase;


public class WaitingActivity extends AppCompatActivity {
    Integer now;
    static Integer put;
    Integer max=MainActivity.max;
    String receivedNames;
    TextView peopleName, peopleNum;
    Boolean start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        peopleNum = findViewById(R.id.numberView);
        peopleName = findViewById(R.id.nameView);
        peopleName.setGravity(Gravity.CENTER_HORIZONTAL);
        loadNow();
        loadName();
        loadData();
    }

    private void loadName() {
        mDatabase.child("userNames").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receivedNames = snapshot.getValue(String.class);
                peopleName.setText(receivedNames + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WaitingActivity.this, "로딩실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadNow() {
        mDatabase.child("userNum").child("now").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                now = snapshot.getValue(Integer.class);
                put = max - now;
                peopleNum.setText(put + "/" + max);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WaitingActivity.this, "로딩실패", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadData() {
        mDatabase.child("start").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                start = snapshot.getValue(Boolean.class);
                if (start) {
                    Intent intent=new Intent(WaitingActivity.this,ChooseActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WaitingActivity.this, "로딩실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}