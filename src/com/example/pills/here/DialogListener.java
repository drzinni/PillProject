package com.example.pills.here;

import android.content.DialogInterface;
import com.utdproject.pills.here.R;

public interface DialogListener {
        public void onDialogPositiveClick(DialogInterface dialog,int arg);
        public void onDialogNegativeClick(DialogInterface dialog,int arg);
}
