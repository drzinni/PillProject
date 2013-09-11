package com.example.pills.here;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.utdproject.pills.here.R;

public class Receiver extends BroadcastReceiver {
	public Receiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// This method is called when the BroadcastReceiver is receiving
		// an Intent broadcast.
		if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
		{
			Intent serviceIntent=new Intent(context,AlarmService.class);
			context.startService(serviceIntent);
		}
	}
}
