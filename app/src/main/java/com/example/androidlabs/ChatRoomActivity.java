package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private ArrayList<Message> list = new ArrayList<Message>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ListView chat = findViewById(R.id.listView);
        Button send = findViewById(R.id.sendButton);
        Button receive = findViewById(R.id.receiveButton);
        EditText messageText = findViewById(R.id.sendMessage);


        MyListAdapter adapter = new MyListAdapter();
        chat.setAdapter(adapter);

        send.setOnClickListener(v -> {
            Message message = new Message(messageText.getText().toString(), true);
            list.add(message);
            messageText.setText("");
            adapter.notifyDataSetChanged();


        });

        receive.setOnClickListener(v -> {
            Message message = new Message(messageText.getText().toString(), false);
            list.add(message);
            messageText.setText("");
            adapter.notifyDataSetChanged();
        });

        chat.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Do you want to delete this?");
            alertDialog.setMessage("The selected row is: " + position + "\nThe database id: " + id);
            alertDialog.setPositiveButton("Yes", (click, arg) -> {list.remove(position); adapter.notifyDataSetChanged();});
            alertDialog.setNegativeButton("No", (click, arg) -> {});
            alertDialog.create().show();
            return true;
        });
    }



    class MyListAdapter extends BaseAdapter{
        @Override
        public int getCount() {

            return list.size();
        }

        @Override
        public Object getItem(int position) {

            return list.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return null;
            Message message = (Message) getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            if (message.getSend() == true) {
                View newView = inflater.inflate(R.layout.activity_send, parent, false);
                TextView textView = newView.findViewById(R.id.sendText);
                textView.setText(message.getMessage());
                return newView;
            }

            else {
                View newView = inflater.inflate(R.layout.activity_receive, parent, false);
                TextView textView = newView.findViewById(R.id.receiveText);
                textView.setText(message.getMessage());
                return newView;
            }



        }

        @Override
        public long getItemId(int position) {

            return position;
        }
    }




}
