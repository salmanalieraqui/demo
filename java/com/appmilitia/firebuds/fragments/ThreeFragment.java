package com.appmilitia.firebuds.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.appmilitia.firebuds.R;
import com.appmilitia.firebuds.activity.GradientBackgroundPainter;
import com.appmilitia.firebuds.activity.Msg;
import com.appmilitia.firebuds.activity.MsgList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */

public class ThreeFragment extends Fragment{
    ListView messages_listview;
    DatabaseReference databaseUser;
    EditText new_message;
    ArrayList<Msg> msgList;
    ImageButton send_message;
    String edittextmessage;
    String first_name,last_name;
    private ArrayList<String> myUserDataList;
    //bg changer
    private GradientBackgroundPainter gradientBackgroundPainter;

    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_three, container, false);


        //bg code........

        View fragment3_layout = view.findViewById(R.id.fragment3_layout);

        final int[] drawables = new int[3];
        drawables[0] = R.drawable.gradient_1;
        drawables[1] = R.drawable.gradient_2;
        drawables[2] = R.drawable.gradient_3;

        gradientBackgroundPainter = new GradientBackgroundPainter(fragment3_layout, drawables);
        gradientBackgroundPainter.start();


        //bg code ends here ................





        myUserDataList = (ArrayList<String>)getArguments().getStringArrayList("myUserDataList");
        first_name=myUserDataList.get(0);
        last_name=myUserDataList.get(1);
        if(first_name!=null){
            sharedPreuserloggedindetails(first_name, last_name);

        }
        else {
            first_name=getSPFirst_name();
            last_name=getSPLast_name();
        }

        msgList= new ArrayList<Msg>();
        databaseUser= FirebaseDatabase.getInstance().getReference("messages");
        messages_listview=(ListView)view.findViewById(R.id.messages_listview);
        new_message=(EditText)view.findViewById(R.id.new_message);
        send_message=(ImageButton)view.findViewById(R.id.send_message);
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMsgToDB();
            }
        });


        return view;
    }

    private void addMsgToDB() {
        edittextmessage=new_message.getText().toString().trim();
        if(TextUtils.isEmpty(edittextmessage) ){
            Toast.makeText(getActivity(),"Please write something...",Toast.LENGTH_SHORT).show();
        }
        else{
            String uuid = UUID.randomUUID().toString();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());


            Msg msg=new Msg(uuid, first_name,last_name ,edittextmessage,timeStamp);
            databaseUser.child(uuid).setValue(msg);
            //Toast.makeText(getContext(),"Messasge sent ",Toast.LENGTH_LONG).show();
            new_message.setText("");
        }

    }

     @Override
    public void onStart() {
        super.onStart();
        databaseUser.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                msgList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren() ){
                    Msg msg= userSnapshot.getValue(Msg.class);

                    msgList.add(msg);
                }

                MsgList adapter=new MsgList(getActivity(),msgList);
                messages_listview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }
    private void sharedPreuserloggedindetails(String first_name, String last_name){
        SharedPreferences mSharedPreference=getActivity().getSharedPreferences("sharedPreuserloggedindetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor=mSharedPreference.edit();
        mEditor.putString("first_name",first_name);
        mEditor.putString("last_name",last_name);
        mEditor.apply();

    }
    public String getSPFirst_name() {
        SharedPreferences mSharedPreference=getActivity().getSharedPreferences("sharedPreuserloggedindetails", Context.MODE_PRIVATE);
        String  SPfirst_name=mSharedPreference.getString("first_name","");
        return SPfirst_name;
    }

    public String getSPLast_name() {
        SharedPreferences mSharedPreference=getActivity().getSharedPreferences("sharedPreuserloggedindetails", Context.MODE_PRIVATE);
        String SPlast_name=mSharedPreference.getString("last_name","");
        return SPlast_name;
    }


}
