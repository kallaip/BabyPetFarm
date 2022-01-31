package com.dextergames.babypetfarm;

import com.dextergames.babypetfarm.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class PetActivity extends Activity {

	   /** A handle to the thread that's actually running the animation. */
    private Thread mThread;

    /** A handle to the View in which the game is running. */
    private PetView mPetView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		setContentView(R.layout.activity_pet);
		AdView adView = (AdView) findViewById(R.id.adView);
		  AdRequest adRequest = new AdRequest();
		  //adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
		  // adRequest.addTestDevice("E213A781A27DEE785F5CCD8A07BF21BF");
		 adView.loadAd(adRequest);
		
		mPetView = (PetView) findViewById(R.id.fullscreen_content);
		mThread = mPetView.getThread();


	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		
	}
	
	
	@Override
    protected void onPause() {
        super.onPause();
        mPetView.pause(); // pause game when Activity pauses
    }
	
	@Override
    protected void onResume() {
        super.onResume();
        mPetView.resume(); // pause game when Activity pauses
    }
	
	@Override
	public void onDestroy() {
	    super.onDestroy();  // Always call the superclass
	    
	    
	    // Stop method tracing that the activity started during onCreate()
	    //android.os.Debug.stopMethodTracing();
	}

	
	
}
