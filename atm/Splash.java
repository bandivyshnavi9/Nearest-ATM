package com.money.atm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;


public class Splash extends Activity{
	int s=1;
	ProgressDialog pd;
	public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        pd=new ProgressDialog(Splash.this);

	}
	public boolean onTouchEvent(final MotionEvent e)
	{ 
		if	 (e.getAction() == MotionEvent.ACTION_DOWN)
		{ 
		
			pd = ProgressDialog.show(Splash.this, "", "Displaying ATMs List ,please wait.......", true,true);
			pd.show();
			startActivity(new Intent(Splash.this, ATM.class)); 
			
			handler.sendEmptyMessage(0);
		}
	return true; 
	}

@Override
public void onStart() {
	super.onStart();
}
private Handler handler = new Handler() {

    @Override
    public void handleMessage(Message msg) {
            pd.dismiss();
            
    }
};
}
