package com.example.varun.app4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;

public class AddEditActivity extends AppCompatActivity {

    private EditText title;
    private DatePicker date;
    private RatingBar rating;
    private CheckBox box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        title = (EditText) findViewById(R.id.name);
        date = (DatePicker) findViewById(R.id.date);
        rating = (RatingBar) findViewById(R.id.ratingBar2);
        box = (CheckBox) findViewById(R.id.askMeLater);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.get("Action").equals("Edit")) {
                title.setText(extras.get("Title").toString());
                int array[] = setDate(extras.get("Date").toString());
                date.updateDate(array[2], array[0]-1, array[1]);
                rating.setRating(Float.valueOf(extras.get("Rating").toString()));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        menu.findItem(R.id.addMovie).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveMovie:
                // check the ask me later button first, create a notification
                if (box.isChecked()) {
                    // Alternative solution: Use URI to construct query with content provider instead of putExtra, later
                    Intent notificationIntent = new Intent(this, AddEditActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    notificationIntent.putExtra("Action", "Edit");
                    notificationIntent.putExtra("Title", title.getText());
                    notificationIntent.putExtra("Date",getDate(date));
                    notificationIntent.putExtra("Rating", rating.getRating());

                    int flag = PendingIntent.FLAG_UPDATE_CURRENT;
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, flag);
                    int icon = R.mipmap.ic_launcher;
                    CharSequence tickerText = "Edit Movie";
                    CharSequence contentTitle = getText(R.string.app_name);
                    CharSequence contentText = "Select to Edit";

                    Notification notification = new NotificationCompat.Builder(this)
                            .setSmallIcon(icon)
                            .setTicker(tickerText)
                            .setContentTitle(contentTitle)
                            .setContentText(contentText)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .build();

                    final int NOTIFICATION_ID = 1;
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(NOTIFICATION_ID, notification);

                }
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Title", title.getText().toString());
                returnIntent.putExtra("Date", getDate(date).toString());
                returnIntent.putExtra("Rating", rating.getRating());
                setResult(RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.cancel:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public int[] setDate(String date) {
        int [] returnDate = new int[3];
        String temp[] = date.split("/");
        returnDate[0] = Integer.valueOf(temp[0]);
        returnDate[1] = Integer.valueOf(temp[1]);
        returnDate[2] = Integer.valueOf(temp[2]);
        return returnDate;
    }

    // returns the date as a string in the format: mm/dd/yyyy
    public String getDate(DatePicker date) {
        String returnDate = "";
        returnDate += String.valueOf(date.getMonth() + 1);
        returnDate += "/";
        returnDate += String.valueOf(date.getDayOfMonth());
        returnDate += "/";
        returnDate += String.valueOf(date.getYear());
        return returnDate;

    }
}
