package com.example.himanshu.trial4;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class PostSignUp extends AppCompatActivity {

    ListView courtList;
    FirebaseDatabase firebaseCourts;
    DatabaseReference databaseReference;
    ArrayList<String> list,list1,list2;
    ArrayAdapter<String> adapter;
    Courts courts,court1;
    RadioButton bad,foot;
    public static Courts selectedCourt;
    RadioGroup ss;
    Vector<Courts> cvector1,cvector2;
    Boolean b,f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_sign_up);
        courtList = (ListView)findViewById(R.id.courtList);
        bad = (RadioButton)findViewById(R.id.bad);
        foot = (RadioButton)findViewById(R.id.foot);
        firebaseCourts = FirebaseDatabase.getInstance();
        databaseReference = firebaseCourts.getReference("BADFUT/COURTS");

        courts = new Courts();

        bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBadClicked();

            }
        });

        foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFootClicked();
            }
        });

        courtList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);


                //Intent intent = new Intent(PostSignUp.this,CourtInfo.class);
                if(b)
                {
                    selectedCourt = cvector1.get(position);
                }
                else
                {
                    selectedCourt = cvector2.get(position);
                }
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PostSignUp.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_court_info, null);
                Button check,map;
                check = (Button)mView.findViewById(R.id.check);
                map = (Button)mView.findViewById(R.id.map);
                TextView cname=(TextView)mView.findViewById(R.id.cname) ;
                cname.setText(selectedCourt.getName());
                check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PostSignUp.this,Booking.class);
                        startActivity(intent);
                    }
                });
                map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PostSignUp.this,MapsActivity.class);
                        startActivity(intent);
                    }
                });
                /*startActivity(intent);*/
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

    }

    public void onBadClicked()
    {
        b=true;
        f=false;
        list1 = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list1);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cvector1=new Vector<>();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    courts = ds.getValue(Courts.class);
                    court1 = new Courts(courts.getAddress(),courts.getLat(),courts.getLong(),courts.getName(),courts.getTimings(),courts.getType());

                    if(courts.getType().toString().compareTo("B")==0) {
                        cvector1.addElement(court1);
                        //System.out.print(courts.getAddress().toString());
                        list1.add(courts.getName().toString() + "\n" + courts.getAddress().toString());
                    }
                }
                courtList.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void onFootClicked()
    {
        f=true;
        b=false;
        list2 = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list2);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cvector2=new Vector<>();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    courts = ds.getValue(Courts.class);
                    court1 = new Courts(courts.getAddress(),courts.getLat(),courts.getLong(),courts.getName(),courts.getTimings(),courts.getType());
                    if(courts.getType().toString().compareTo("F")==0) {
                        cvector2.addElement(court1);
                        list2.add(courts.getName().toString() + "\n" + courts.getAddress().toString());
                    }
                }
                courtList.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
