
package com.appmilitia.firebuds.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.ArgbEvaluator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.appmilitia.firebuds.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity  {

    private Toolbar toolbar;
    private GradientBackgroundPainter gradientBackgroundPainter;

    private CallbackManager mCallbackManager;
    private static final String TAG ="FACELOG";
    private FirebaseAuth mAuth;
    String first_name, last_name, email, id, birthday, gender;
    Double  double_latti,double_longi;
    LocationManager locationManager;
    LocationListener locationListener;
    static  final int REQUEST_LOCATION=1;


    ObjectAnimator textColorAnim;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //faceboook hash key generator code
        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        } */

        //hiding status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // setting blinking anmation to the welcome text
        TextView textviewwelcome=(TextView)findViewById(R.id.textviewwelcome);
        textColorAnim = ObjectAnimator.ofInt(textviewwelcome, "textColor", Color.BLACK, Color.TRANSPARENT);
        textColorAnim.setDuration(1000);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();

        // blinking ends here


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //bg code........

        View backgroundImage = findViewById(R.id.loginbg);

        final int[] drawables = new int[3];
        drawables[0] = R.drawable.gradient_1;
        drawables[1] = R.drawable.gradient_2;
        drawables[2] = R.drawable.gradient_3;

        gradientBackgroundPainter = new GradientBackgroundPainter(backgroundImage, drawables);
        gradientBackgroundPainter.start();


        //bg code ends here ................

        // LOCATION CODE
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        getLocation();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        // facebook login button
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG , "facebook:onSuccess:" + loginResult);

                // My code for getting fB user details 1

                String userId = loginResult.getAccessToken().getUserId();
                GraphRequest graphRequest= GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        displayUserInfo(object);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, last_name, email, id, birthday, gender");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();



                //My code for getting fb user details ends here 1

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

        // Firebase auth
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();



    }
    void getLocation() {
        try{

            final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                buildAlertMessageNoGps();
                if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

                } else {
                    Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(location !=null) {
                        double_latti=location.getLatitude();
                        double_longi=location.getLongitude();
                    }
                }
            }
            if(manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ){
                if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

                } else {
                    Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(location !=null) {
                        double_latti=location.getLatitude();
                        double_longi=location.getLongitude();
                    }
                }

            }

        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(),"Unable to fetch Your location! Please Enable The GPS",Toast.LENGTH_SHORT).show();
            double_latti=0.0;
            double_longi=0.0;
        }

    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_LOCATION:
                getLocation();
                break;
        }
    }

    //My code for getting fb user details 2

    public void displayUserInfo(JSONObject object) {

        first_name = "";
        last_name = "";
        email = "";
        id = "";
        birthday= "";
        gender= "";
        try {

            if(object.has("first_name"))
                first_name = object.getString("first_name");
            if(object.has("last_name"))
                last_name = object.getString("last_name");
            if (object.has("email"))
                email = object.getString("email");
            if (object.has("id"))
                id=object.getString("id");
            if (object.has("birthday"))
                birthday = object.getString("birthday");
            if (object.has("gender"))
                gender = object.getString("gender");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //My code for getting fb user detail ends here 2

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null ){
            updateUI();
        }

    }
    private void updateUI(){
        Intent icontabsactivity=new Intent(MainActivity.this,IconTabsActivity.class);
        ArrayList<String> myUserDataList=new ArrayList<String>();
        myUserDataList.add(first_name);
        myUserDataList.add(last_name);
        myUserDataList.add(email);
        myUserDataList.add(id);
        myUserDataList.add(birthday);
        myUserDataList.add(gender);
        icontabsactivity.putExtra("myUserDataList",myUserDataList);
        icontabsactivity.putExtra("double_latti",double_latti);
        icontabsactivity.putExtra("double_longi",double_longi);
        startActivity(icontabsactivity);
        finish();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }

                        // ...
                    }
                });
    }



    // Login bg

    /*
    public class BackgroundPainter {

        private static final int MIN = 800;
        private static final int MAX = 1500;

        private final Random random;

        public BackgroundPainter() {
            random = new Random();
        }

        public void animate(@NonNull final View target, @ColorInt final int color1,
                            @ColorInt final int color2) {

            final ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), color1, color2);

            valueAnimator.setDuration(randInt(MIN, MAX));

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override public void onAnimationUpdate(ValueAnimator animation) {
                    target.setBackgroundColor((int) animation.getAnimatedValue());
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationEnd(Animator animation) {
                    //reverse animation
                    animate(target, color2, color1);
                }
            });

            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.start();
        }

        private int randInt(int min, int max) {
            return random.nextInt((max - min) + 1) + min;
        }
    } */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gradientBackgroundPainter.stop();
    }

}



