package com.anas.chatiApp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.core.SnapshotHolder;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {
    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mSnapshotList;
    //type used by FB to pass data to our app
    //array that will hold snapshots dynamique
    private ChildEventListener mListener= new ChildEventListener() {
        @Override
        public void onChildAdded( DataSnapshot dataSnapshot,  String s) {
            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot,  String s) {

        }

        @Override
        public void onChildRemoved( DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved( DataSnapshot dataSnapshot,  String s) {

        }

        @Override
        public void onCancelled( DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Activity activity, DatabaseReference ref, String displayName) {
        mActivity = activity;
        mDisplayName = displayName;
        mDatabaseReference = ref.child("messages");
        mDatabaseReference.addChildEventListener(mListener);

        mSnapshotList = new ArrayList<>();
    }

    static class ViewHolder{
        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams params;

    }


    @Override
    public int getCount() {

        Log.d("ChatiApp","Get Count ");
        return mSnapshotList.size();
    }

    @Override
    public InstantMessage getItem(int position) {

        Log.d("ChatiApp","Get Item");
        DataSnapshot snapshot=mSnapshotList.get(position);
        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_msg_row,parent,false);

            final ViewHolder holder = new ViewHolder();
            holder.authorName=(TextView) convertView.findViewById(R.id.author);
            holder.body = (TextView) convertView.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams) holder.authorName.getLayoutParams();
            convertView.setTag(holder);

        }
        final InstantMessage message = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        boolean isMe=message.getAuthor().equals(mDisplayName);
        setChatRowApperance(isMe,holder);

        String author=message.getAuthor();
        holder.authorName.setText(author);

        String msg = message.getMessage();
        holder.body.setText(msg);
        return convertView;
    }

    public void setChatRowApperance(boolean isItMe, ViewHolder holder) {
        if (isItMe) {
            holder.params.gravity = Gravity.END;
            holder.authorName.setTextColor(Color.RED);
            holder.body.setBackgroundResource(R.drawable.bubble2);
        } else
        {
            holder.params.gravity = Gravity.START;
            holder.authorName.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }

        holder.authorName.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);

    }
    public void cleanup(){
        mDatabaseReference.removeEventListener(mListener);
    }

}
