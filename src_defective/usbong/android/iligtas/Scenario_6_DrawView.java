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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/*
 * Reference: http://stackoverflow.com/questions/3616676/how-to-draw-a-line-in-android;
 * last accessed: 20 Nov. 2013
 */
public class Scenario_6_DrawView extends View implements OnTouchListener {
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
	
	private int numOfBoxesHorizontal=3;
	private int numOfBoxesVertical=4;

	private ImageView myTarpImageView;
	private int myTarpPosX=-50;
	private int myTarpPosY=-50;

	private ImageView myTarpOverheadImageView;
//	private int myTarpOverheadPosX=-50;
//	private int myTarpOverheadPosY=-50;
	
	private int transitionToNextScreenCounter=0;
	private int transitionToNextScreenMaxCounter=2;
	
	private RelativeLayout.LayoutParams lp;
	
	//Reference: http://stackoverflow.com/questions/5255184/android-and-setting-width-and-height-programmatically-in-dp-units;
	//last accessed: Dec. 10, 2013; answer by Robby Pond
	final float scale = getContext().getResources().getDisplayMetrics().density;
	
    public Scenario_6_DrawView(Context context) {
        super(context);
		this.setOnTouchListener(this);

		lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		lastPosX=-1;//getWidth()/2;
		lastPosY=-1;//getHeight()/2;
		currPosX=-1;
		currPosY=-1;

		myTarpImageView = (ImageView) ((Activity)context).findViewById(R.id.tarp_imageview);				
//		myTarpImageView.setVisibility(View.INVISIBLE);

		myTarpOverheadImageView = (ImageView) ((Activity)context).findViewById(R.id.tarp_overhead_imageview);				
//		myTarpOverheadImageView.setVisibility(View.INVISIBLE);

		answer = new int[numOfBoxesHorizontal][numOfBoxesVertical];
		resetAnswers();
		
		correctAnswer = new int[numOfBoxesHorizontal][numOfBoxesVertical];
		correctAnswer[0][0] = 0;	correctAnswer[1][0] = 0;	correctAnswer[2][0] = 0;
		correctAnswer[0][1] = 0;	correctAnswer[1][1] = 1;	correctAnswer[2][1] = 0;
		correctAnswer[0][2] = 0;	correctAnswer[1][2] = 1;	correctAnswer[2][2] = 0;
		correctAnswer[0][3] = 0;	correctAnswer[1][3] = 0;	correctAnswer[2][3] = 0;
		
		hasAnswered=false;
		isAnswerCorrect=false;//true;
		update();
    }
    
    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.RED);
    	paint.setStrokeWidth((float) 3.0);
        //        canvas.drawLine(0, 0, 20, 20, paint);
//        canvas.drawLine(20, 0, 0, 20, paint);
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
    	paint.setColor(Color.WHITE);
    	myCanvas.drawLine(lastPosX, lastPosY, currPosX, currPosY, paint);
    	lastPosX = currPosX;
    	lastPosY = currPosY;

		try{
			canvas.drawBitmap(myBitmap,
					new Rect(0,0, myBitmap.getWidth(),myBitmap.getHeight()),
					new Rect(0,0, myBitmap.getWidth(),myBitmap.getHeight()),
					paint);//0,0,null);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
				

		if ((myTarpPosX==-50) && (myTarpPosY==-50)) {
			resetTarpPosition();
	        myTarpImageView.setVisibility(View.VISIBLE);
	        myTarpOverheadImageView.setVisibility(View.INVISIBLE);

	        //put these here since the X, Y positions do not change anyway
	        lp.setMargins(myTarpPosX, myTarpPosY, 0, 0);
	        myTarpImageView.setLayoutParams(lp);
/*
	        lp.setMargins(myTarpOverheadPosX, myTarpOverheadPosY, 0, 0);
	        myTarpOverheadImageView.setLayoutParams(lp);
*/	        
		}
        
        if (hasAnswered) {
			Log.d(">>>>>>>> is answer correct?:",""+isAnswerCorrect);
			
			if (isAnswerCorrect) {
//				resetSettings();
		
				//so that the bus's position doesn't go back to the start position while the scenario is transitioning to the next scenario
		        myTarpImageView.setVisibility(View.INVISIBLE);
		        myTarpOverheadImageView.setVisibility(View.VISIBLE);
		        this.onSizeChanged(this.getWidth(),this.getHeight(),0,0);
				
				//do resetAnswers(), etc so that if the back key is pressed it'll return to the previous scenario
//				((ScenarioActivity)this.getContext()).processNextScreen();
			}
			else {
				resetSettings();
			}
		}
    }
    
    public void resetSettings() {    	
    	resetTarpPosition();	//this should come before isAnswerCorrect
		hasAnswered=false;
		isAnswerCorrect=true;
		resetAnswers();
		transitionToNextScreenCounter=0;
		this.onSizeChanged(this.getWidth(),this.getHeight(),0,0);
//		this.invalidate();
    }
    
    public void resetTarpPosition() {    	
		int tarp_offsetX = (int) (33 * scale + 0.5f);

		myTarpPosX = myWidth*1+tarp_offsetX;
		myTarpPosY = myHeight*2;
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
        	if (hasAnswered && isAnswerCorrect){
        		if (transitionToNextScreenCounter<transitionToNextScreenMaxCounter) {
	        		transitionToNextScreenCounter++;
	        	}
	        	else {
	        		resetSettings();
					((ScenarioActivity)this.getContext()).processNextScreen();
	        	}        		
        	}
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
        	Scenario_6_DrawView.this.update();
        	Scenario_6_DrawView.this.invalidate();
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

					isAnswerCorrect=true;								
					//check if correct answer
					for(int i=0; i<numOfBoxesHorizontal; i++) {
						for(int k=0; k<numOfBoxesVertical; k++) {
							if ((correctAnswer[i][k]==1) && (answer[i][k]!=correctAnswer[i][k])){
								isAnswerCorrect=false;								
							}							
						}
					}				
					
					hasAnswered=true;
					this.invalidate();
					Log.d(">>>>>>>DONE","DONE");					
					return true;
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					currPosX = me.getX();
					currPosY = me.getY();
/*	
					myBusPosX = (int)currPosX-myBusImageView.getWidth()/2;
					myBusPosY = (int)currPosY-myBusImageView.getHeight()/2;
*/					
		        	for (int i=0; i<numOfBoxesHorizontal; i++) { //horizontal
		        		for (int k=0; k<numOfBoxesVertical; k++) { //vertical
		            		if (currPosX>=myWidth*i && currPosX<=myWidth*i+myWidth) {
		            			if (currPosY>=myHeight*k && currPosY<=myHeight*k+myHeight) {
		            				answer[i][k]=1;
		            			}            			
		            		}        			
	
		            		if (lastPosX>=myWidth*i && lastPosX<=myWidth*i+myWidth) {
		            			if (lastPosY>=myHeight*k && lastPosY<=myHeight*k+myHeight) {
		            				answer[i][k]=1;
		            			}            			
		            		}        			        		
		        		}        		
		        	}        						
					this.invalidate();
					return true;
			}
		}
		return false;
	}
}