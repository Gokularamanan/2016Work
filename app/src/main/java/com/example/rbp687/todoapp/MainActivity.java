package com.example.rbp687.todoapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get ListView object from xml
        final ListView listView = (ListView) findViewById(R.id.listView);

        // Create a new Adapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // Use Firebase to populate the list.
        Firebase.setAndroidContext(this);


        new Firebase("https://my-first-app-14449.firebaseio.com/todoItems/users")
                .addChildEventListener(new ChildEventListener() {
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("GokD", "onChildAdded");
                        Log.d("GokF", dataSnapshot.toString());
                        String key = dataSnapshot.getKey().toString();
                        Log.d("GokK", key);
                        String value= dataSnapshot.getValue().toString();
                        Log.d("GokV", value);
                        Bitmap hit = getEventValue(value);
                        showNotification(hit);
                    }
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        adapter.remove((String)dataSnapshot.child("text").getValue());
                    }
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.d("GokD", "onChildChanged");
                        Log.d("GokF", dataSnapshot.toString());
                        String key = dataSnapshot.getKey().toString();
                        Log.d("GokK", key);
                        String value= dataSnapshot.getValue().toString();
                        Log.d("GokV", value);
                        Bitmap hit = getEventValue(value);
                        showNotification(hit);

            }

                    private Bitmap getEventValue(String value) {
                        if (value.contains("event=FOUR")) {
                            Log.d("Gokul", "FOUR");
                            return BitmapFactory.decodeResource(getResources(), R.drawable.mcdonalds_four);
                        }
                        if (value.contains("event=SIX")) {
                            Log.d("Gokul", "SIX");
                            return BitmapFactory.decodeResource(getResources(), R.drawable.shell_logo_six);
                        }
                        if (value.contains("event=no run")) {
                            Log.d("Gokul", "no run");
                            return BitmapFactory.decodeResource(getResources(), R.drawable.pepsiwicket);
                        }
                        if (value.contains("event=1 run")) {
                            Log.d("Gokul", "1 run");
                            return BitmapFactory.decodeResource(getResources(), R.drawable.mcdonalds_four);
                        }
                        if (value.contains("event=2 run")) {
                            Log.d("Gokul", "2 run");
                            return BitmapFactory.decodeResource(getResources(), R.drawable.shell_logo_six);
                        }
                        if (value.contains("event=OUT") || value.contains("event=WICKET")) {
                            Log.d("Gokul", "OUT");
                            return BitmapFactory.decodeResource(getResources(), R.drawable.pepsiwicket);
                        }
                        return null;
                    }

                    public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
                    public void onCancelled(FirebaseError firebaseError) { }


                    NotificationManager mNotificationManager1 =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    int notifyID = 1;
                    Notification.Builder notif = new Notification.Builder(getApplicationContext());

                    private void showNotification(Bitmap bitmap7) {
                        if (bitmap7 == null) {
                            Log.d("Gok", "Other events");
                            return;
                        }
                        Calendar c = Calendar.getInstance();
                        int hour = c.get(Calendar.HOUR);
                        int seconds = c.get(Calendar.SECOND);
                        int minutes = c.get(Calendar.MINUTE);
                        String time = hour + ":" + minutes + "-" + seconds;
                        Log.d("Gok", time);
                        notif//.setContentTitle("contenttitle  " + item)
                                //.setContentText(Integer.toString(item))
                                .setSmallIcon(android.R.drawable.arrow_down_float)
                                .setLargeIcon(bitmap7) // probably not needed
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setPriority(Notification.PRIORITY_MAX)
                                .setStyle(new Notification.InboxStyle()
                                        .addLine(time));
                                        //.setBigContentTitle(Integer.toString(item))
                                        //.setSummaryText("+3 more"));
                                //.build();
                        mNotificationManager1.notify(notifyID, notif.build());
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
