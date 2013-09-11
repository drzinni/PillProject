package com.example.pills.here;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import com.utdproject.pills.here.R;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        TextView textView = (TextView)findViewById(R.id.clock);
        // textView is the TextView view that should display it
        textView.setText(currentDateTimeString);
        Intent intent=new Intent(this,AlarmService.class);
		startService(intent);//update the time the alarm will go off
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        TextView textView = (TextView)findViewById(R.id.clock);
        // textView is the TextView view that should display it
        textView.setText(currentDateTimeString);
    }
    
    // DEBUG: test for alarm
    public void testAlarm(View view) {
        Intent intent = new Intent(this, AlarmMain.class);
        intent.putExtra("testing", true);
        startActivity(intent);
    }
    
    public void settings(View view){
    	Intent intent = new Intent(this, GeneralSettings.class);
        startActivity(intent);
    }
    
    public void about(View view){
    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://utdallas.edu/~drz100020/modify.html"));
    	startActivity(browserIntent);
    }
}
