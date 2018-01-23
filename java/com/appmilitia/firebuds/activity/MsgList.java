package com.appmilitia.firebuds.activity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appmilitia.firebuds.R;

import java.util.List;
/**
 * Created by Salman on 16-01-2018.
 */

public class MsgList extends ArrayAdapter<Msg>{
    private Activity context;
    private List<Msg> msgList;

    public MsgList(Activity context, List<Msg> msgList) {
        super(context,R.layout.message_list_layout,msgList);
        this.context = context;
        this.msgList = msgList;
    }
    @NonNull

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewitem=inflater.inflate(R.layout.message_list_layout,null,true);
        TextView messageusername=(TextView)listViewitem.findViewById(R.id.messageusername);
        TextView messageusermessage=(TextView)listViewitem.findViewById(R.id.messageusermessage);
        Msg msg=msgList.get(position);
        messageusername.setText(msg.getUser_fb_first_name()+" "+msg.getUser_fb_last_name());
        messageusermessage.setText(msg.getTextmessage());
        return listViewitem;
    }
}
