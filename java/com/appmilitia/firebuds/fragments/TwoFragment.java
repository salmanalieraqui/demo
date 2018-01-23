package com.appmilitia.firebuds.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.appmilitia.firebuds.R;
import com.appmilitia.firebuds.activity.GradientBackgroundPainter;
import com.appmilitia.firebuds.activity.User;
import com.appmilitia.firebuds.activity.UsersList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragment extends Fragment{

    ListView listViewUsers;
    DatabaseReference databaseUser;
    ArrayList<User> userList;
    //bg changer
    private GradientBackgroundPainter gradientBackgroundPainter;

    public TwoFragment() {
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
        View view= inflater.inflate(R.layout.fragment_two, container, false);

        //bg code........

        View backgroundImage = view.findViewById(R.id.listViewUsers);

        final int[] drawables = new int[3];
        drawables[0] = R.drawable.gradient_1;
        drawables[1] = R.drawable.gradient_2;
        drawables[2] = R.drawable.gradient_3;

        gradientBackgroundPainter = new GradientBackgroundPainter(backgroundImage, drawables);
        gradientBackgroundPainter.start();


        //bg code ends here ................



        userList= new ArrayList<User>();
        databaseUser= FirebaseDatabase.getInstance().getReference("FbUsersList");
        listViewUsers=(ListView)view.findViewById(R.id.listViewUsers);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        databaseUser.orderByChild("timestamp").limitToLast(1000).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren() ){
                    User user= userSnapshot.getValue(User.class);

                    userList.add(0,user);
                }

                UsersList adapter=new UsersList(getActivity(),userList);
                listViewUsers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}

