package usbong.android.iligtas;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/*
 * Reference: http://stackoverflow.com/questions/3616676/how-to-draw-a-line-in-android;
 * last accessed: 20 Nov. 2013
 */
public class Scenario_9_DrawView extends View implements OnTouchListener {
    Paint paint = new Paint();

	private long mMoveDelay = 600;
	private float lastPosX;
	private float lastPosY;
	private float currPosX;
	private float currPosY;
	
	private Bitmap myBitmap;
	private Canvas myCanvas;

	private int myMaxWidth;
	private int myMaxHeight;	
	private int myWidth;
	private int myHeight;

	
	private int[][] answer;
	private int[][] correctAnswer;
	
	private boolean hasAnswered;
	private boolean isAnswerCorrect;
	
	private int numOfBoxesHorizontal=4;
	private int numOfBoxesVertical=4;
	
	private ImageView myRescueBoatImageView;
	private int myRescueBoatPosX=-50;
	private int myRescueBoatPosY=-50;

//	private RelativeLayout.LayoutParams lp;
	private FrameLayout.LayoutParams lp;

	//Reference: http://stackoverflow.com/questions/5255184/android-and-setting-width-and-height-programmatically-in-dp-units;
	//last accessed: Dec. 10, 2013; answer by Robby Pond
	final float scale = getContext().getResources().getDisplayMetrics().density;

	private int goalPosX;
	
    public Scenario_9_DrawView(Context context) {
        super(context);
		this.setOnTouchListener(this);

//		lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		lastPosX=-1;//getWidth()/2;
		lastPosY=-1;//getHeight()/2;
		currPosX=-1;
		currPosY=-1;
/*
		myRescueBoatImageView = new ImageView(context);
		myRescueBoatImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.rescueboat));
		((Activity)context).addContentView(myRescueBoatImageView, lp); 				
*/
    	myRescueBoatImageView = (ImageView) ((Activity)context).findViewById(R.id.rescueboat_imageview);				

		answer = new int[numOfBoxesHorizontal][numOfBoxesVertical];
		resetAnswers();
		
		correctAnswer = new int[numOfBoxesHorizontal][numOfBoxesVertical];
		correctAnswer[0][0] = 0;	correctAnswer[1][0] = 0;	correctAnswer[2][0] = 0;	correctAnswer[3][0] = 0;
		correctAnswer[0][1] = 0;	correctAnswer[1][1] = 0;	correctAnswer[2][1] = 0;	correctAnswer[3][1] = 0;
		correctAnswer[0][2] = 1;	correctAnswer[1][2] = 1;	correctAnswer[2][2] = 1;	correctAnswer[3][2] = 1;
		correctAnswer[0][3] = 0;	correctAnswer[1][3] = 0;	correctAnswer[2][3] = 0;	correctAnswer[3][3] = 0;
		
		hasAnswered=false;
		isAnswerCorrect=false;
		update();
    }
    
    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.RED);
    	paint.setStrokeWidth((float) 3.0);

    	myMaxWidth = canvas.getWidth();
    	myMaxHeight = canvas.getHeight();
/*    	
    	Log.d(">>>>>>canvas.getWidth()",""+canvas.getWidth());
    	Log.d(">>>>>>canvas.getHeight()",""+canvas.getHeight());
*/    	
    	myWidth = myMaxWidth/numOfBoxesHorizontal;
    	myHeight = myMaxHeight/numOfBoxesVertical;
/*    	
    	for (int i=0; i<numOfBoxesHorizontal+1; i++) { //why +1? for the right-most line
    		//draw vertical lines
        	canvas.drawLine(myWidth*i, 0, myWidth*i, myMaxHeight, paint);    		
    	}
    	
    	for (int i=0; i<numOfBoxesVertical+1; i++) { //why +1? for the bottom line
	    	//draw horizontal lines
	    	canvas.drawLine(0, myHeight*i, myMaxWidth, myHeight*i, paint);    		        	
    	}
*/    	
/*    	
    	paint.setColor(Color.WHITE);
    	myCanvas.drawLine(lastPosX, lastPosY, currPosX, currPosY, paint);
    	lastPosX = currPosX;
    	lastPosY = currPosY;
*/
		try{
			canvas.drawBitmap(myBitmap,
					new Rect(0,0, myBitmap.getWidth(),myBitmap.getHeight()),
					new Rect(0,0, myBitmap.getWidth(),myBitmap.getHeight()),
					paint);//0,0,null);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
				

		if (myRescueBoatImageView!=null) {
			if ((myRescueBoatPosX==-50) && (myRescueBoatPosY==-50)) {
				resetRescueBoatPosition();
			}
	    	LayoutParams lp = new LayoutParams(myRescueBoatImageView.getWidth(), myRescueBoatImageView.getHeight());
			lp.setMargins(myRescueBoatPosX, myRescueBoatPosY, 0, 0);
			myRescueBoatImageView.setLayoutParams(lp);
			myRescueBoatImageView.setVisibility(View.VISIBLE);
		}
		
        if (hasAnswered) {
			Log.d(">>>>>>>> is answer correct?:",""+isAnswerCorrect);
			
			if (isAnswerCorrect) {
		        this.onSizeChanged(this.getWidth(),this.getHeight(),0,0);

//				resetSettings();
/*		
				//so that the bus's position doesn't go back to the start position while the scenario is transitioning to the next scenario
		        myBusImageView.setVisibility(View.INVISIBLE);
*/				
				//do resetAnswers(), etc so that if the back key is pressed it'll return to the previous scenario
				((ScenarioActivity)this.getContext()).processNextScreen();
			}
			else {
//				resetSettings();
		    	hasAnswered=false;
				isAnswerCorrect=false;
			}
		}
    }
    
    public void resetSettings() {    	
    	resetRescueBoatPosition();	//this should come before isAnswerCorrect
		myRescueBoatImageView.setVisibility(View.VISIBLE);
    	hasAnswered=false;
		isAnswerCorrect=false;
//		resetAnswers();
		this.onSizeChanged(this.getWidth(),this.getHeight(),0,0);
//		this.invalidate();
    }
    
    public void resetRescueBoatPosition() {
		int rescueBoat_offsetY = (int) (10 * scale + 0.5f);

		myRescueBoatPosX = 0;
		myRescueBoatPosY = myHeight*2 + rescueBoat_offsetY;
		
		goalPosX = myWidth*3;
    }
    
    public void resetAnswers() {
		//set 0 as default
		for(int i=0; i<numOfBoxesHorizontal; i++) {
			for(int k=0; k<numOfBoxesVertical; k++) {
				answer[i][k]=0;
			}
		}
    }
        
	//Reference: http://stackoverflow.com/questions/2423327/android-view-ondraw-always-has-a-clean-canvas
	//Last accessed on: July 2, 2010
	public void onSizeChanged(int w, int h, int oldW, int oldH) {
	    if (myBitmap != null) {
		  myBitmap.recycle();
//		  myBitmap = null;
        }
		try{
		  myBitmap = Bitmap.createBitmap(this.getWidth(),this.getHeight(),Bitmap.Config.ARGB_4444); //ARGB_8888
		  myCanvas = new Canvas(myBitmap);

//		  clearCanvas();
//    	  Paint p = new Paint();
//    	  p.setColor(0xFF6A9D69); //blackboard green
//    	  myCanvas.drawRect(0, 0, myBitmap.getWidth(), myBitmap.getHeight(), p);

		}
		catch(Exception e) {
			e.printStackTrace();
		}	  
	}

	public void destroy() {
        if (myBitmap != null) {
            myBitmap.recycle();
        }
    }
    
	public void update() {
        long now = System.currentTimeMillis();

        //TODO change this, because "now" is always greater than mMoveDelay)
        if (now  > mMoveDelay) {
        	//do updates
//        	System.out.println("DITO");
        }
        mRedrawHandler.sleep(mMoveDelay);
	}
	
	/**
     * Create a simple handler that we can use to cause animation to happen.  We
     * set ourselves as a target and we can use the sleep()
     * function to cause an update/invalidate to occur at a later date.
     */
    private RefreshHandler mRedrawHandler = new RefreshHandler();
    private class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
        	Scenario_9_DrawView.this.update();
        	Scenario_9_DrawView.this.invalidate();
        }

        public void sleep(long delayMillis) {
                this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    };
    
	@Override
	public boolean onTouch(View arg0, MotionEvent me) {
		System.out.println("TOUCHED!");
		
		if (!hasAnswered) {
			int meAction = me.getAction();
			if (lastPosX==-1) {
			  lastPosX = me.getX();
			}
			if (lastPosY==-1) {
			  lastPosY = me.getY();
			}
			
			switch(meAction) {
				case MotionEvent.ACTION_UP:
					lastPosX = -1;
					lastPosY = -1;
					currPosX = -1;
					currPosY = -1;

					hasAnswered=true;
					this.invalidate();
					Log.d(">>>>>>>DONE","DONE");					
					return true;
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					currPosX = me.getX();
					currPosY = me.getY();

					myRescueBoatPosX = (int)currPosX-myRescueBoatImageView.getWidth()/2;
					if (myRescueBoatPosX+myRescueBoatImageView.getWidth()/2>=goalPosX) {
						hasAnswered=true;
						isAnswerCorrect=true;
					}
					this.invalidate();
					return true;
			}
		}
		return false;
	}
}