package com.appmilitia.firebuds.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appmilitia.firebuds.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Salman on 13-01-2018.
 */

public class UsersList extends ArrayAdapter<User> {
    private Activity context;
    private List<User> userList;
    public UsersList(Activity context,List<User> userList){
        super(context, R.layout.users_list_layout,userList);
        this.context=context;
        this.userList=userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewitem=inflater.inflate(R.layout.users_list_layout,null,true);
        TextView usernamefromdb=(TextView)listViewitem.findViewById(R.id.fbuser_name);
        TextView userstatusfromdb=(TextView)listViewitem.findViewById(R.id.fbuser_txtStatusMsg);
        ImageView listview_image=(ImageView)listViewitem.findViewById(R.id.fbuser_profilePic);
        Button connectbtn=(Button)listViewitem.findViewById(R.id.connectbtn);
        User user=userList.get(position);
        usernamefromdb.setText(user.getUser_fb_first_name()+" "+user.getUser_fb_last_name());
        userstatusfromdb.setText(user.getUser_firebudsstatus());
        final String user_id=user.getUser_fb_id();
        Picasso.with(context).load( "https://graph.facebook.com/"+user_id+"/picture?width=250&height=200").into(listview_image);
        connectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="https://www.facebook.com/app_scoped_user_id/"+user_id;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                getContext().startActivity(i);
            }
        });
        return listViewitem;
    }
}
