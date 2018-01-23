package com.appmilitia.firebuds.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appmilitia.firebuds.R;
import com.appmilitia.firebuds.activity.GradientBackgroundPainter;
import com.appmilitia.firebuds.activity.IconTabsActivity;
import com.appmilitia.firebuds.activity.MainActivity;
import com.appmilitia.firebuds.activity.User;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class OneFragment extends Fragment{
    private FirebaseAuth mAuth;
    private ArrayList<String> myUserDataList;
    public String first_name, last_name, email, id,gender, birthday ;
    String firebudsstatus;
    public EditText et_status;
    public ImageButton btn_update;
    DatabaseReference databaseUser;
    ImageView fb_user_dp;
    TextView tv_name;
    TextView tv_email;
    TextView tv_id;
    TextView tv_gender;
    TextView tv_location;
    Button fblogoutbutton;
    String userStatus;
    Double double_latti,double_longi;
    TextView tv_mycurrentstatus;
    //reverse goe coding...
    Geocoder geocoder;
    List<Address> addresses;
    String userLocationAddress;
    IconTabsActivity iconTabsActivity;

    //bg changer
    private GradientBackgroundPainter gradientBackgroundPainter;


    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_one, container, false);

        //bg code........

        View backgroundImage = view.findViewById(R.id.fragment1_layout);
        final int[] drawables = new int[3];
        drawables[0] = R.drawable.gradient_1;
        drawables[1] = R.drawable.gradient_2;
        drawables[2] = R.drawable.gradient_3;
        gradientBackgroundPainter = new GradientBackgroundPainter(backgroundImage, drawables);
        gradientBackgroundPainter.start();

        //bg code ends here ................

        myUserDataList = (ArrayList<String>)getArguments().getStringArrayList("myUserDataList");
        first_name=myUserDataList.get(0);
        last_name=myUserDataList.get(1);
        email=myUserDataList.get(2);
        id=myUserDataList.get(3);
        birthday=myUserDataList.get(4);
        gender=myUserDataList.get(5);
        iconTabsActivity=(IconTabsActivity) getActivity();
        double_latti=iconTabsActivity.double_latti;
        double_longi=iconTabsActivity.double_longi;
        firebudsstatus=getSPFirebudsstatus();

        databaseUser= FirebaseDatabase.getInstance().getReference("FbUsersList");
        mAuth= FirebaseAuth.getInstance();

        fb_user_dp=(ImageView)view.findViewById(R.id.fb_user_dp);
        tv_name=(TextView)view.findViewById(R.id.tv_username);
        tv_mycurrentstatus=(TextView)view.findViewById(R.id.tv_mycurrentstatus);
        tv_email=(TextView)view.findViewById(R.id.tv_email);
        tv_id=(TextView)view.findViewById(R.id.tv_id);
        tv_gender=(TextView)view.findViewById(R.id.tv_gender);
        tv_location=(TextView)view.findViewById(R.id.tv_location);
        et_status=(EditText)view.findViewById(R.id.et_status);
        btn_update=(ImageButton) view.findViewById(R.id.statusupdatebtn);
        if(first_name!=null){
            addUserToDB();
            tv_mycurrentstatus.setText(getSPFirebudsstatus());
        }
        else{
            first_name=getSPFirst_name();
            last_name=getSPLast_name();
            email=getSPEmail();
            id=getSPId();
            birthday=getSPBirthday();
            gender=getSPGender();
            firebudsstatus=getSPFirebudsstatus();
            tv_mycurrentstatus.setText(getSPFirebudsstatus());

        }

        Picasso.with(getContext()).load( "https://graph.facebook.com/"+id+"/picture?width=350&height=250").into(fb_user_dp);
        tv_name.setText(getSPFirst_name()+" "+getSPLast_name());

        tv_email.setText(getSPEmail());
        tv_id.setText(getSPId());
        tv_gender.setText(getSPGender());
        tv_location.setText(double_latti+","+double_longi);

       // facebook login...

        fblogoutbutton=(Button)view.findViewById(R.id.fblogoutbtn);
        fblogoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser=mAuth.getCurrentUser();
                if(currentUser !=null){
                    updateUI();
                }

            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myUserDataList!=null)
                {
                    try{
                        addUserStatusToDB();
                        sharedPreuserloggedindetails(first_name,last_name,email,id,birthday,gender,firebudsstatus);
                    }
                    catch (Exception e) {
                        Toast.makeText(getContext(),"Please Check Your Internet Connection.",Toast.LENGTH_LONG).show();
                    }


                }
                else{
                    Toast.makeText(getContext(),"Please Login First.",Toast.LENGTH_LONG).show();
                }

            }
        });



        //reverse geocodeing

        geocoder=new Geocoder(getActivity(), Locale.getDefault());
        try {
            addresses=geocoder.getFromLocation(double_latti,double_longi,1);
            if(addresses.size()!=0){
                String city=addresses.get(0).getAdminArea();
                String country=addresses.get(0).getCountryName();
                userLocationAddress=city+", "+country;
                tv_location.setText(userLocationAddress);
            }
            else{
                userLocationAddress="";
                tv_location.setText(userLocationAddress);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;

    }


    // fxn of shared prefrence.......................................
    private void sharedPreuserloggedindetails(String first_name, String last_name,String email,String id,String birthday,String gender, String firebudsstatus){
        SharedPreferences mSharedPreference=getActivity().getSharedPreferences("sharedPreuserloggedindetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor=mSharedPreference.edit();
        mEditor.putString("first_name",first_name);
        mEditor.putString("last_name",last_name);
        mEditor.putString("email",email);
        mEditor.putString("id",id);
        mEditor.putString("birthday",birthday);
        mEditor.putString("gender",gender);
        mEditor.putString("firebudsstatus",firebudsstatus);
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

    public String getSPEmail() {
        SharedPreferences mSharedPreference=getActivity().getSharedPreferences("sharedPreuserloggedindetails", Context.MODE_PRIVATE);
        String SPemail=mSharedPreference.getString("email","");
        return SPemail;
    }

    public String getSPId() {
        SharedPreferences mSharedPreference=getActivity().getSharedPreferences("sharedPreuserloggedindetails", Context.MODE_PRIVATE);
        String SPid=mSharedPreference.getString("id","");
        return SPid;
    }

    public String getSPBirthday() {
        SharedPreferences mSharedPreference=getActivity().getSharedPreferences("sharedPreuserloggedindetails", Context.MODE_PRIVATE);
        String SPbirthday=mSharedPreference.getString("birthday","");
        return SPbirthday;
    }

    public String getSPGender() {
        SharedPreferences mSharedPreference=getActivity().getSharedPreferences("sharedPreuserloggedindetails", Context.MODE_PRIVATE);
        String  SPgender=mSharedPreference.getString("gender","");
        return SPgender;
    }

    public String getSPFirebudsstatus() {
        SharedPreferences mSharedPreference=getActivity().getSharedPreferences("sharedPreuserloggedindetails", Context.MODE_PRIVATE);
        String  SPfirebudsstatus=mSharedPreference.getString("firebudsstatus","Hey There, I am using FireBuds");
        return SPfirebudsstatus;
    }


    private  void addUserToDB(){
        try{
            if(birthday=="")
            {
                birthday="Not specified";
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());


            User user=new User(first_name,last_name,email,id,birthday,gender,firebudsstatus,double_latti,double_longi,timeStamp);
            databaseUser.child(id).setValue(user);
            sharedPreuserloggedindetails(first_name,last_name,email,id,birthday,gender,firebudsstatus);
           // Toast.makeText(getContext(),"user added succesfully to DB",Toast.LENGTH_LONG).show();
        }

        catch (Exception e){
           // Toast.makeText(getContext(),"Some error occured to add user",Toast.LENGTH_LONG).show();
        }

    }
    private  void addUserStatusToDB(){
        try{

            userStatus=et_status.getText().toString().trim();
            if(TextUtils.isEmpty(userStatus) ){
                Toast.makeText(getActivity(),"Please Write Something",Toast.LENGTH_SHORT).show();
            }
            else{

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

                databaseUser.child(id).child("user_firebudsstatus").setValue(userStatus);
                databaseUser.child(id).child("timestamp").setValue(timeStamp);
                Toast.makeText(getContext(),"Status Updated Successfuly",Toast.LENGTH_LONG).show();
                firebudsstatus = userStatus;
                tv_mycurrentstatus.setText(firebudsstatus);
                sharedPreuserloggedindetails(first_name,last_name,email,id,birthday,gender,firebudsstatus);
                et_status.setText("");
            }

        }

        catch (Exception e){
            Toast.makeText(getContext(),"Some exception to update status",Toast.LENGTH_LONG).show();
        }


    }

    private void updateUI(){
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        Toast.makeText(getContext(),"You're Successfully Logged Out!",Toast.LENGTH_LONG).show();
        Intent mainactivity=new Intent(getActivity(),MainActivity.class);

        //removing data of shared preference
        SharedPreferences settings = getContext().getSharedPreferences("sharedPreuserloggedindetails", Context.MODE_PRIVATE);
        settings.edit().clear().commit();

        startActivity(mainactivity);
        getActivity().finish();



    }


}

