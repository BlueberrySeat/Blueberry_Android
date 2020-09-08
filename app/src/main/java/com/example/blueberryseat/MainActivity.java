package com.example.blueberryseat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    static Integer max;
    int check=0;
String getCode;
FirebaseDatabase database=FirebaseDatabase.getInstance();
static DatabaseReference mDatabase;
Boolean start=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button next=findViewById(R.id.codeButton);
        final TextView code=findViewById(R.id.getInCode);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCode=code.getText().toString();
                if(code.length()>0){
                    mDatabase=database.getReference().child(getCode);
                    loadStart();
                }
                else{
                    Toast.makeText(getApplicationContext(),"코드를 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
private void loadStart(){
        mDatabase.child("start").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    start=snapshot.getValue(Boolean.class);
                    if(!start){
                        loadMax();
                        Intent intent=new Intent(MainActivity.this,InputNameActivity.class);
                        startActivity(intent);
                        check+=1;
                }else if(check==0){
                        Toast.makeText(getApplicationContext(),"이미 시작된 코드입니다.",Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),"코드를 찾을수 없습니다.",Toast.LENGTH_SHORT).show();
                    }

                }





            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"로딩실패",Toast.LENGTH_SHORT).show();
            }
        });
}
    private void loadMax() {
        mDatabase.child("userNum").child("max").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                max = snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "로딩실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}