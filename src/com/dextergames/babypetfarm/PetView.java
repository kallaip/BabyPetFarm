package com.dextergames.babypetfarm;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dextergames.babypetfarm.PetView.MainThread;
import com.dextergames.babypetfarm.R;

public class PetView extends SurfaceView implements SurfaceHolder.Callback {

	
	public static final int STATE_COMPUTE = 0;
	public static final int STATE_WAIT = 1;
	public static final int STATE_DRAW = 2;
	
	private MainThread thread;
	private boolean mRun = false;
	private SurfaceHolder mSurfaceHolder;
	private final Paint mPaint = new Paint();
	private int maxX;
	private int maxY;
	private long mLastTime;
	
	private Bitmap backg;
	private Bitmap scaledbackg;
	private Bitmap unicorn;
	private Bitmap lamby;
	private Bitmap cow;
	private Bitmap cat;
	private Bitmap dog;
	
	private long fpsTimer;
	private long rndTime;
	private long rndTimer;
	private int fpsCounter;
	private int lastFPS;
	private int drawState;
	private boolean firstRun;
	private int xstart;
	private int periodX;
	private int amplX;
	private int periodY;
	private int amplY;
	private double phaseX;
	private double phaseY;
	private double trackAngle;
	private double trackX;
	private double trackY;
	
	private int windowX;
	private int windowY;
	private int offsetX;
	private int offsetY;	
	private int rndPet;
	public boolean mTouched;
	public boolean mReleased;
	
	private MediaPlayer uniPlayer;
	private MediaPlayer lambyPlayer;
	private MediaPlayer catPlayer;
	private MediaPlayer cowPlayer;
	private MediaPlayer dogPlayer;
	private int screenX;
	private int screenY;
	private Context mContext;
	
	public boolean dogWasPlayed;
	public boolean lambyWasPlayed;
	public boolean cowWasPlayed;
	public boolean catWasPlayed;
	public boolean uniWasPlayed;
	
	public PetView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// register our interest in hearing about changes to our surface
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		mContext=context;
		backg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		unicorn = BitmapFactory.decodeResource(getResources(), R.drawable.unikornis);
		lamby = BitmapFactory.decodeResource(getResources(), R.drawable.lamby);
		cow = BitmapFactory.decodeResource(getResources(), R.drawable.cow);
		cat = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
		dog = BitmapFactory.decodeResource(getResources(), R.drawable.dog);
		uniPlayer = MediaPlayer.create(context, R.raw.horse);
		lambyPlayer  = MediaPlayer.create(context, R.raw.lamb_voice);
		cowPlayer  = MediaPlayer.create(context, R.raw.cow);
		catPlayer  = MediaPlayer.create(context, R.raw.meow);
		dogPlayer  = MediaPlayer.create(context, R.raw.dog);
		setFocusable(true); // make sure we get key events
		drawState=STATE_COMPUTE;
		firstRun=true;
		
		// create thread only; it's started in surfaceCreated()
		thread = new MainThread(holder, context);
		thread.setRunning(true);
		
		dogWasPlayed=false;
		uniWasPlayed=false;
		lambyWasPlayed=false;
		catWasPlayed=false;
		cowWasPlayed=false;
		
	}
	
	
	
	

	class MainThread extends Thread {

		public MainThread(SurfaceHolder surfaceHolder, Context context) {
			mSurfaceHolder = surfaceHolder;
			mPaint.setARGB(255, 0, 0, 255);

		}

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%% THREAD %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5

		public void run() {

			while (mRun) {
				Canvas canvas = null;
				canvas = mSurfaceHolder.lockCanvas(null);
				// TODO: IDOZITEST RENDBE TENNI!!!!!!
				synchronized (mSurfaceHolder) {
					try {

						// canvas.drawColor(Color.BLACK);
						long now = System.currentTimeMillis();
						// if (mLastTime > now)
						// return;

						double elapsed = (now - mLastTime);
						fpsTimer += elapsed;
						rndTimer+=elapsed;
						
						// -------------------------------------

						canvas.drawBitmap(scaledbackg, 0, 0, null);
						fpsCounter++;
						
						if (drawState==STATE_COMPUTE){
							rndTime = (long) Math.ceil(Math.random()*2500+500);
							
							try {
								Thread.sleep(150);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							
							periodX = (int) Math.ceil(Math.random()*0.1+0.01);
							amplX = (int) Math.ceil(Math.random()*0.5+0.2);
							phaseX = Math.ceil(Math.random()* Math.PI);
							rndPet=(int) Math.round(Math.random()*4);
							
							rndTimer = 0;
							drawState=STATE_WAIT;
							
						} else if (drawState==STATE_WAIT){
							
							if (rndTimer>=rndTime){
								drawState=STATE_DRAW;
								trackAngle=0;
								dogWasPlayed=false;
							}
							
							//canvas.drawText(Integer.toString((int) rndTimer), 100, 50, mPaint);
							//canvas.drawText("/ "+Integer.toString((int) rndTime), 120, 50, mPaint);
							
						} else if (drawState==STATE_DRAW){
							
							if (dogWasPlayed){
								if(!dogPlayer.isPlaying()){
									if (dogPlayer != null) {
										dogPlayer.release();
									}
									dogPlayer = MediaPlayer.create(mContext, R.raw.dog);
									dogWasPlayed=false;
								}
							}
							if (cowWasPlayed){
								if(!cowPlayer.isPlaying()){
									if (cowPlayer != null) {
										cowPlayer.release();
									}
									cowPlayer = MediaPlayer.create(mContext, R.raw.cow);
									cowWasPlayed=false;
								}
							}
							if (catWasPlayed){
								if(!catPlayer.isPlaying()){
									if (catPlayer != null) {
										catPlayer.release();
									}
									catPlayer = MediaPlayer.create(mContext, R.raw.meow);
									catWasPlayed=false;
								}
							}
							if (lambyWasPlayed){
								if(!lambyPlayer.isPlaying()){
									if (lambyPlayer != null) {
										lambyPlayer.release();
									}
									lambyPlayer = MediaPlayer.create(mContext, R.raw.lamb_voice);
									lambyWasPlayed=false;
								}
							}
							if (uniWasPlayed){
								if(!uniPlayer.isPlaying()){
									if (uniPlayer != null) {
										uniPlayer.release();
									}
									uniPlayer = MediaPlayer.create(mContext, R.raw.horse);
									uniWasPlayed=false;
								}
							}
							
							if (mTouched) {
								
								if (rndPet==0){
									if(!uniPlayer.isPlaying() && !uniWasPlayed){
										uniPlayer.start();
										uniWasPlayed=true;
									} 	

								} else if(rndPet==1){
									if(!lambyPlayer.isPlaying() && !lambyWasPlayed){
										lambyPlayer.start();
										lambyWasPlayed=true;
									} 	

								} else if(rndPet==2){
									if(!cowPlayer.isPlaying() && !cowWasPlayed){
										cowPlayer.start();
										cowWasPlayed=true;
									} 	

								} else if(rndPet==3){
									if(!catPlayer.isPlaying() && !catWasPlayed){
										catPlayer.start();
										catWasPlayed=true;
									} 	

								} else if(rndPet==4){
									if(!dogPlayer.isPlaying() && !dogWasPlayed){
										dogPlayer.start();
										dogWasPlayed=true;
									} 
								}
							}
							
							trackX = (Math.PI/180)*trackAngle;
							trackY = Math.abs(Math.sin(trackX*periodX+phaseX)*amplX);
							screenX = (int) Math.floor(windowX*(trackX/(Math.PI)))-offsetX;
							screenY = (int) Math.floor(windowY*trackY)-offsetY;
							
					
							if (rndPet==0){
								canvas.drawBitmap(unicorn, screenX ,screenY, null);
							} else if (rndPet==1){
								canvas.drawBitmap(lamby, screenX, screenY, null);
							} else if (rndPet==2){
								canvas.drawBitmap(cow, screenX, screenY, null);
							} else if (rndPet==3){
								canvas.drawBitmap(cat, screenX, screenY, null);
							} else if (rndPet==4){
								canvas.drawBitmap(dog, screenX, screenY, null);
							}
							
							
							
							trackAngle+=0.3;
							if (trackAngle>=180){
								drawState=STATE_COMPUTE;
							}
						}

						if (fpsTimer >= 500) {
							lastFPS = fpsCounter;
							fpsCounter = 0;
							fpsTimer = 0;
						}

						
						//canvas.drawText(Integer.toString(lastFPS * 2), 50, 50, mPaint);
						//canvas.drawText(Integer.toString(drawState), 70, 50, mPaint);
						//canvas.drawText(Integer.toString((int) trackAngle), 90, 50, mPaint);
						//canvas.drawText("tX:" + Double.toString(trackX), 120, 50, mPaint);
						//canvas.drawText("tY:" + Double.toString(trackY), 320, 50, mPaint);
						
						//canvas.drawText("dX:" + Integer.toString((int) Math.floor(windowX*(trackX/(Math.PI)))-offsetX), 520, 50, mPaint);
						//canvas.drawText("dY:" + Integer.toString((int) Math.floor(windowY*trackY)-offsetY), 720, 50, mPaint);
						
						if (mTouched) {
						//	canvas.drawText("T", 100, 10, mPaint);
						}

						if (mReleased) {
						//	canvas.drawText("R", 110, 10, mPaint);
							mTouched = false;
							mReleased = false;
							
						}
						
						// -------------------------------------
						firstRun=false;

					} finally {
						if (canvas != null) {
							mSurfaceHolder.unlockCanvasAndPost(canvas);
						}

					}
					try {
						Thread.sleep(3);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

		public void setRunning(boolean b) {
			mRun = b;
		}
		
        public MainThread getThread() {
            return thread;
        }
        

        public void setTouched(){
 	
        	mTouched=true;
        	
        }
        
        public void setReleased(){
         	
        	mReleased=true;
        	
        } 

		public void setSurfaceSize(int width, int height) {
			// synchronized to make sure these all change atomically
			synchronized (mSurfaceHolder) {
				maxX = width;
				maxY = height;
				
				windowX=(int) Math.floor(maxX*1.2);
				windowY=(int) Math.floor(maxY*1.2);
				
				offsetX= (int) Math.floor((windowX-maxX)/2);
				offsetY= (int) Math.floor((windowX-maxX)/2);
				
				//double scaleRatio = maxX / backg.getWidth();
				//int origWidth = backg.getWidth();
				scaledbackg = Bitmap.createScaledBitmap(backg, maxX, maxY, true);
			}
		}

	}

	/* Callback invoked when the surface dimensions change. */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		thread.setSurfaceSize(width, height);
	}

	/*
	 * Callback invoked when the Surface has been created and is ready to be
	 * used.
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		if (thread.getState()==Thread.State.TERMINATED) { 
            thread = new MainThread(holder, mContext);
       }
       thread.setRunning(true);
       thread.start();
	}

	/*
	 * Callback invoked when the Surface has been destroyed and must no longer
	 * be touched. WARNING: after this method returns, the Surface/Canvas must
	 * never be touched again!
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		// we have to tell thread to shut down & wait for it to finish, or else
		// it might touch the Surface after we return and explode
		boolean retry = true;
		thread.setRunning(false);
	//	thread.stop();
		
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event){ 
	        
	    int action = MotionEventCompat.getActionMasked(event);
	        
	    switch(action) {
	        case (MotionEvent.ACTION_DOWN) :
	           // Log.d(DEBUG_TAG,"Action was DOWN");
	        	thread.setTouched();
	            return true;
	        case (MotionEvent.ACTION_MOVE) :
	            //Log.d(DEBUG_TAG,"Action was MOVE");
	            return true;
	        case (MotionEvent.ACTION_UP) :
	            //Log.d(DEBUG_TAG,"Action was UP");
	        	thread.setReleased();
	            return true;
	        case (MotionEvent.ACTION_CANCEL) :
	            //Log.d(DEBUG_TAG,"Action was CANCEL");
	            return true;
	        case (MotionEvent.ACTION_OUTSIDE) :
	            //Log.d(DEBUG_TAG,"Movement occurred outside bounds of current screen element");
	            return true;      
	        default : 
	            return super.onTouchEvent(event);
	    }      
	}
	
	public void pause() {
		thread.setRunning(false);
		
	}
	
	public void resume() {
		thread.setRunning(true);
		
	}
	
	
	public Thread getThread() {
		return thread;
	}

}
