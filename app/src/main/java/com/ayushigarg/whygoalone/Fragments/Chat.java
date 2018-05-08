package com.ayushigarg.whygoalone.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ayushigarg.whygoalone.Activities.DashboardActivity;
import com.ayushigarg.whygoalone.Models.message;
import com.ayushigarg.whygoalone.R;
import com.ayushigarg.whygoalone.Utils.MessageAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Chat extends Fragment {
    EditText e1;
    Button b1;
    RecyclerView r1;
    ArrayList<message> m;
    MessageAdapter ad;
    SharedPreferences sp;
    String id1,name1;
    DatabaseReference db;
    SharedPreferences.Editor edi;
    Context ctx;
    Activity activity;

    public Chat(Context ctx, Activity activity) {
        this.ctx = ctx;
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
            ((DashboardActivity) getActivity()).setActionBarTitle("chat here");
        e1=(EditText)rootView.findViewById(R.id.edd1);
        b1=(Button)rootView.findViewById(R.id.b11);
        r1=(RecyclerView)rootView.findViewById(R.id.r11);
        db= FirebaseDatabase.getInstance().getReference().child("chat");
        sp=this.getActivity().getSharedPreferences("mydata", Context.MODE_PRIVATE);
        id1=sp.getString("userid","a");
        name1=sp.getString("username","ab");
        m=new ArrayList<>();

        //      ad=new MessageAdapter(this,m);
        r1.setLayoutManager(new LinearLayoutManager(ctx));

        showmessage();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_messgae();

            }
        });
        return rootView;
    }
    private void save_messgae()
    {
        String msgg=e1.getText().toString().trim();
        if(!TextUtils.isEmpty(msgg))
        {
            message msg=new message(id1,name1,msgg);
            db.push().setValue(msg);
            Toast.makeText(ctx, "Message added", Toast.LENGTH_SHORT).show();
            e1.setText("");

            showmessage();
        }
        else
        {
            Toast.makeText(ctx, "please eneter the messgae", Toast.LENGTH_SHORT).show();
        }
    }
    private void showmessage() {

        m.clear();
        FirebaseDatabase.getInstance().getReference().child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String name = snap.child("name").getValue(String.class);
                    String message = snap.child("messagel").getValue(String.class);
                    String id = snap.child("id").getValue(String.class);
                    message msg = new message(id, name, message);
                    m.add(msg);
                }
                MessageAdapter messageAdapter=new MessageAdapter(ctx,m);
                r1.setAdapter(messageAdapter);
                r1.scrollToPosition(m.size()-1);
//                ad.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
}

}
