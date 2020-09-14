package com.example.blueberryseat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.blueberryseat.InputNameActivity.getUserName;
import static com.example.blueberryseat.MainActivity.mDatabase;


public class ChooseActivity extends AppCompatActivity {
Integer maxCho=MainActivity.max;
String setName=getUserName;
String[] peoples=new String[50];
int selected=-1;
int checkedSelected=-2;
    int same=0;
    Button checkButton;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        loadSeat();
        LinearLayout View=findViewById(R.id.main);
        LinearLayout Line = findViewById(R.id.line);
        Line.setGravity(Gravity.CENTER);
        checkButton=findViewById(R.id.check);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                    if(selected>-1){
                            checkedSelected=selected;
                            mDatabase.child("seat/" + checkedSelected).setValue(setName);
                    }
                    else{
                        Toast.makeText(ChooseActivity.this,"자리를 선택해주세요.",Toast.LENGTH_SHORT).show();
                    }
                }


        });
        Display display=getWindowManager().getDefaultDisplay();
        Point size=new Point();
        display.getSize(size);
        int width=size.x/4-27;
        int a;
        for(a=0;a<maxCho;a++){
            final Button button=new Button(this);
            button.setId(a);
            ViewGroup.LayoutParams ip=new ViewGroup.LayoutParams(width,width);
            button.setLayoutParams(ip);
            button.setBackgroundResource(R.drawable.basepng);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(android.view.View view) {
                    if(selected!=checkedSelected){
                            button.setBackgroundResource(R.drawable.selectseat);
                            if(selected>-1){
                                Button other=findViewById(selected);
                                other.setBackgroundResource(R.drawable.basepng);
                            }
                            selected=button.getId();
                        }
                }
            });
            Line.addView(button);
            if((a+1)%4==0){
                Line=new LinearLayout(this);
                Line.setOrientation(LinearLayout.HORIZONTAL);
                Line.setGravity(Gravity.LEFT);
                int pad=(size.x-(width*4))/2;
                Line.setPadding(pad,0,0,0);
                View.addView(Line);
            }

        }

    }
    private void loadSeat(){
        try{
            mDatabase.child("seat").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    same=0;
                    for(int a=0;a<maxCho;a++){
                        loadChild(a);
                    }
                    if(same==0){
                        checkedSelected=-2;
                        selected=-1;
                        checkButton.setEnabled(true);
                        checkButton.setBackgroundResource(R.drawable.selected);
                    }
                    else if(same==1){
                        checkedSelected=selected;
                        checkButton.setBackgroundResource(R.drawable.wasselected);
                        checkButton.setEnabled(false);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            finish();
        }

    }
    private void loadChild(final int child){
        mDatabase.child("seat/"+child).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                peoples[child]=snapshot.getValue(String.class);
                Button button=findViewById(child);
                Typeface face=getResources().getFont(R.font.jalnan);
                button.setTypeface(face);
                if(peoples[child].length()>0){
                    button.setText(peoples[child]+"");
                    button.setTextColor(Color.WHITE);
                    button.setGravity(Gravity.CENTER);
                    button.setPadding(0,0,0,0);
                    button.setBackgroundResource(R.drawable.otherselect);
                    if(peoples[child].equals(setName)){
                        checkButton.setBackgroundResource(R.drawable.wasselected);
                        checkButton.setEnabled(false);
                        same=1;
                    }
                    button.setEnabled(false);
                }
                else{
                    button.setEnabled(true);
                    button.setBackgroundResource(R.drawable.basepng);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
      }
