package com.example.pills.here;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import com.utdproject.pills.here.R;

public class GeneralSettings extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_settings);
    }
    public void addPill(View view){
    	Intent intent = new Intent((Context)this, AddPill.class);
        startActivity(intent);
    }
    
    public void lookup(View view){
    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.drugs.com/imprints.php"));
    	startActivity(browserIntent);
    }
    
    public void modifySchedule(View view){
    	Intent intent = new Intent(this, ListPills.class);
        startActivity(intent);
    }
    
    public void goHome(View view){
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    
    
}
