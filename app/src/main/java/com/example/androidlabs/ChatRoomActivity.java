package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Arrays;

public class ChatRoomActivity extends AppCompatActivity {

    private ArrayList<Message> list = new ArrayList<Message>();
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ListView chat = findViewById(R.id.listView);
        Button send = findViewById(R.id.sendButton);
        Button receive = findViewById(R.id.receiveButton);
        EditText messageText = findViewById(R.id.sendMessage);

        loadDataFromDataBase();
        MyListAdapter adapter = new MyListAdapter();
        chat.setAdapter(adapter);



        send.setOnClickListener(v -> {
            long id = insertIntoDataBase(1);
            Message message = new Message(messageText.getText().toString(), true, id);
            list.add(message);
            messageText.setText("");
            adapter.notifyDataSetChanged();


        });

        receive.setOnClickListener(v -> {
            long id = insertIntoDataBase(0);
            Message message = new Message(messageText.getText().toString(), false, id);
            list.add(message);
            messageText.setText("");
            adapter.notifyDataSetChanged();
        });

        chat.setOnItemLongClickListener((parent, view, position, id) -> {
            Message selectedMessage = list.get(position);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Do you want to delete this?");
            alertDialog.setMessage("The selected row is: " + position + "\nThe database id: " + id);
            alertDialog.setPositiveButton("Yes", (click, arg) -> {deleteMessage(selectedMessage); list.remove(position); adapter.notifyDataSetChanged();});
            alertDialog.setNegativeButton("No", (click, arg) -> {});
            alertDialog.create().show();
            return true;
        });
    }

    private void loadDataFromDataBase(){
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();

        String[] columns = {MyOpener.COL_ID, MyOpener.COL_MESSAGE, MyOpener.COL_SENT};
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        //printCursor(results, 1);

        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);
        int messageColIndex = results.getColumnIndex(MyOpener.COL_MESSAGE);
        int sentColIndex = results.getColumnIndex(MyOpener.COL_SENT);

        while(results.moveToNext()){
            long id = results.getLong(idColIndex);
            String message = results.getString(messageColIndex);
            int sent = results.getInt(sentColIndex);

            if(sent == 1){
                list.add(new Message(message, true, id));
            }

            else{
                list.add(new Message(message, false, id));
            }

        }
        results.moveToFirst();
        printCursor(results, 1);
    }

    private long insertIntoDataBase(int isSent){
        EditText messageText = findViewById(R.id.sendMessage);
        String messageToAdd = messageText.getText().toString();

        ContentValues newRowValues = new ContentValues();

        newRowValues.put(MyOpener.COL_MESSAGE, messageToAdd);
        newRowValues.put(MyOpener.COL_SENT, isSent);
        return db.insert(MyOpener.TABLE_NAME, null, newRowValues);

    }

    private void deleteMessage(Message message){
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + " = ?", new String[]{Long.toString(message.getId())});
    }

    private void printCursor(Cursor c, int version){
        int idIndex = c.getColumnIndex(MyOpener.COL_ID);
        int messageIndex = c.getColumnIndex(MyOpener.COL_MESSAGE);
        int sentIndex = c.getColumnIndex(MyOpener.COL_SENT);

        Log.e("Version: ", db.getVersion() + "");
        Log.e("Columns: ", c.getColumnCount() + "");

            Log.e("Column Name: ", Arrays.toString(c.getColumnNames()));

        Log.e("Number of results: ", c.getCount() + "");
        while(c.moveToNext()) {
            long id = c.getLong(idIndex);
            String message = c.getString(messageIndex);
            int sent = c.getInt(sentIndex);
            Log.e("_id", id + "");
            Log.e("Message", message);
            Log.e("IsSent", sent + "");
            //Log.e(String.format("_id: %s%s Message: %s%s IsSent: %s"), id + " " + message + " " + sent);
        }
    }


    class MyListAdapter extends BaseAdapter{
        @Override
        public int getCount() {

            return list.size();
        }

        @Override
        public Message getItem(int position) {

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

            //return position;
            return getItem(position).getId();
        }
    }




}
