package com.example.blueberryseat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.blueberryseat.MainActivity.mDatabase;

public class InputNameActivity extends AppCompatActivity {

    String setUserName;
    String receivedNames;
    static String getUserName;
    Integer now;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_name);
        Button getButton=findViewById(R.id.input);
        final EditText getName=findViewById(R.id.name);
        loadName();
        loadNumber();
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getName.getText().toString().length()>0){
                    if (now > 0) {
                        getUserName=getName.getText().toString();
                        setUserName=receivedNames+getUserName+" ";
                        mDatabase.child("userNames").setValue(setUserName);
                        mDatabase.child("userNum/now").setValue(--now);
                        Intent intent=new Intent(InputNameActivity.this,WaitingActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(InputNameActivity.this,"가득찼습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(InputNameActivity.this,"이름을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void loadName(){
        try{
            mDatabase.child("userNames").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    receivedNames=snapshot.getValue(String.class);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {Toast.makeText(InputNameActivity.this,"로딩실패",Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            finish();
        }

    }
    private void loadNumber(){
        try{
            mDatabase.child("userNum").child("now").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    now=snapshot.getValue(Integer.class);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {Toast.makeText(InputNameActivity.this,"로딩실패",Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            finish();
        }

    }

}