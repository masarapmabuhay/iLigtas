package usbong.android.iligtas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/*
 * Reference: http://stackoverflow.com/questions/3616676/how-to-draw-a-line-in-android;
 * last accessed: 20 Nov. 2013
 */
public class Scenario_5_DrawView extends View implements OnTouchListener {
    Paint paint = new Paint();

	private long mMoveDelay = 600;
	private float lastPosX;
	private float lastPosY;
	private float currPosX;
	private float currPosY;
/*	
	private Bitmap myBitmap;
	private Canvas myCanvas;
*/
	private int myMaxWidth;
	private int myMaxHeight;	
	private int myWidth;
	private int myHeight;

	
	private int[][] answer;
	private int[][] correctAnswer;
	
	private boolean hasAnswered;
	private boolean isAnswerCorrect;
	
	private int numOfBoxesHorizontal=13;
	private int numOfBoxesVertical=8;

	/*
	private BottleTouchableObject[] myBottle;
	private final int totalBottles = 2;//7;
	private boolean hasInitBottles;
	private boolean isMovingAnObject;
	
	private float bagPosX;
	private float bagPosY;
	private int bagWidth;
	private int bagHeight;
*/
	private TouchableObject[] myObjects;
	private final int totalObjects = 3;
	private final int PANTS=0;
	private final int SOCKS=1;
	private final int SHOES=2;
	
	
	//Reference: http://stackoverflow.com/questions/5255184/android-and-setting-width-and-height-programmatically-in-dp-units;
	//last accessed: Dec. 10, 2013; answer by Robby Pond
	final float scale = getContext().getResources().getDisplayMetrics().density;
	
	private FrameLayout.LayoutParams lp;
//	private RelativeLayout.LayoutParams lp;
	
	private boolean hasInitObjects;
	
	private Button hintButton;
	private Button naviButton;

    public Scenario_5_DrawView(Context context) {
        super(context);
		this.setOnTouchListener(this);

		myObjects = new TouchableObject[totalObjects];
		myObjects[PANTS] = new PantsTouchableObject(context);
		myObjects[SOCKS] = new SocksTouchableObject(context);
		myObjects[SHOES] = new ShoesTouchableObject(context);
		
		for (int i=0; i<totalObjects; i++) {
			myObjects[i].setVisible(View.INVISIBLE);
		}
		
		lastPosX=-1;//getWidth()/2;
		lastPosY=-1;//getHeight()/2;
		currPosX=-1;
		currPosY=-1;
		
		hasAnswered=false;
		isAnswerCorrect=false;
//		resetSettings();		
		
    	hintButton = (Button)((Activity)context).findViewById(R.id.hint_button);		
    	hintButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				AlertDialog.Builder prompt = new AlertDialog.Builder(new ContextThemeWrapper(Scenario_1_Activity.this, 5));
				AlertDialog.Builder prompt = new AlertDialog.Builder(Scenario_5_Activity.instance);
				prompt.setTitle("Hint");
				prompt.setMessage(R.string.hint_scenario_5);
				prompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				prompt.show();

			}
    	});

    	naviButton = (Button)((Activity)context).findViewById(R.id.navi_button);		
    	naviButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder prompt = new AlertDialog.Builder(Scenario_5_Activity.instance);
				prompt.setTitle("Navigation");
				prompt.setMessage(R.string.navigation);
				prompt.setPositiveButton("Main Menu", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Scenario_5_Activity.instance.returnToMainMenu();
					}
				});
				prompt.setNegativeButton("About", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Scenario_5_Activity.instance.goToAbout();
					}
				});
				prompt.show();

			}
    	});

		update();
    }
    
    public void drawBottles(Canvas c) {
    	
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
/*    	
		for (int i=0; i<totalObjects; i++) {
			if (!hasInitObjects) {
				myObjects[i].setVisible(View.VISIBLE);

				if (i==totalObjects-1) { //if this is the last bottle
					resetObjectsPosition();
					hasInitObjects=true;
				}
			}
			if (!isAnswerCorrect) {
				myObjects[i].draw(canvas);
			}
		}
*/
		if (!hasInitObjects) {
			resetObjectsPosition();
	    	for (int i=0; i<totalObjects; i++) {
				myObjects[i].setVisible(View.VISIBLE);
	    	}
			hasInitObjects=true;
		}
    	
    	for (int i=0; i<totalObjects; i++) {
			if (!isAnswerCorrect) {
				myObjects[i].draw(canvas);
			}
		}
    	
//        if (hasAnswered) {
			Log.d(">>>>>>>> is answer correct?:",""+isAnswerCorrect);
			
			if (isAnswerCorrect) {
//				resetSettings();
/*
				isAnswerCorrect=false;
				hasInitObjects=false;
*/				
				//so that the bus's position doesn't go back to the start position while the scenario is transitioning to the next scenario
//		        myBottleImageView.setVisibility(View.INVISIBLE);
				
				//do resetAnswers(), etc so that if the back key is pressed it'll return to the previous scenario
				((ScenarioActivity)this.getContext()).processNextScreen();
			}
			else {
//				resetSettings();
			}
//		}
    }
    
    public void resetSettings() {    	
    	resetObjectsPosition();	//this should come before isAnswerCorrect
		hasAnswered=false;
		isAnswerCorrect=true;
//		resetAnswers();
//		this.onSizeChanged(this.getWidth(),this.getHeight(),0,0);
//		this.invalidate();
		
/*		hasInitBottles=false;
 * 
 */
    }
    
    public void resetObjectsPosition() {
/*    	
		int pants_offsetY = (int) (6 * scale + 0.5f);
    	int socks_offsetY = (int) (12 * scale + 0.5f);
		int shoes_offsetY = (int) (14 * scale + 0.5f);

		myObjects[PANTS].setXYPos(myWidth*2, myHeight*3+pants_offsetY);
		myObjects[SOCKS].setXYPos(myWidth*3, myHeight*1+socks_offsetY);
		myObjects[SHOES].setXYPos(myWidth*6, myHeight*4+shoes_offsetY);
*/		
		int pants_offsetX = (int) (30 * scale + 0.5f);
		int pants_offsetY = (int) (7 * scale + 0.5f);
    	int socks_offsetY = (int) (19 * scale + 0.5f);
		int shoes_offsetY = (int) (10 * scale + 0.5f);

		myObjects[PANTS].setXYPos(myWidth*2-pants_offsetX, myHeight*3+pants_offsetY);
		myObjects[SOCKS].setXYPos(myWidth*3, myHeight*1+socks_offsetY);
		myObjects[SHOES].setXYPos(myWidth*6, myHeight*4+shoes_offsetY);
    }

    /*    
    public void resetAnswers() {
		//set 0 as default
		for(int i=0; i<numOfBoxesHorizontal; i++) {
			for(int k=0; k<numOfBoxesVertical; k++) {
				answer[i][k]=0;
			}
		}
    }
*/        
/*    
	//Reference: http://stackoverflow.com/questions/2423327/android-view-ondraw-always-has-a-clean-canvas
	//Last accessed on: July 2, 2010
	public void onSizeChanged(int w, int h, int oldW, int oldH) {
	    if (myBitmap != null) {
		  myBitmap.recycle();
        }
		try{
		  myBitmap = Bitmap.createBitmap(this.getWidth(),this.getHeight(),Bitmap.Config.ARGB_8888);
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
*/
    
	public void update() {
        long now = System.currentTimeMillis();

        //TODO change this, because "now" is always greater than mMoveDelay)
        if (now  > mMoveDelay) {
        	//do updates
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
        	Scenario_5_DrawView.this.update();
        	Scenario_5_DrawView.this.invalidate();
        }

        public void sleep(long delayMillis) {
                this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    };
    
	@Override
	public boolean onTouch(View arg0, MotionEvent me) {
		System.out.println("TOUCHED!");
		if ((me.getX() >= getWidth() - hintButton.getWidth()) &&
				(me.getY() <= 0 + hintButton.getHeight())){
				return false;
		}
		
		if ((me.getX() >= getWidth() - naviButton.getWidth()) &&
				(me.getY() >= getHeight() - naviButton.getHeight())){
				return false;
		}

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
					
		    		//check if all the bottles have been placed in the bag
		    		boolean hasRemainingVisibleObjects=false;
		    		for (int i=0; i<totalObjects; i++) {
		    			if (myObjects[i].getIsVisible()) {
		    				hasRemainingVisibleObjects=true;
		    			}
		    		}
		    		if (!hasRemainingVisibleObjects) {
						isAnswerCorrect=true;										    			
		    		}
	/*
					for(int i=0; i<numOfBoxesHorizontal; i++) {
						for(int k=0; k<numOfBoxesVertical; k++) {
							Log.d(">>>>answer["+i+"]["+k+"]=",""+answer[i][k]);
						}
					}

					for(int i=0; i<numOfBoxesHorizontal; i++) {
						for(int k=0; k<numOfBoxesVertical; k++) {
							Log.d(">>>>correctAnswer["+i+"]["+k+"]=",""+correctAnswer[i][k]);
						}
					}
					
					//check if correct answer
					for(int i=0; i<numOfBoxesHorizontal; i++) {
						for(int k=0; k<numOfBoxesVertical; k++) {
							if ((correctAnswer[i][k]==1) && (answer[i][k]!=correctAnswer[i][k])){
								isAnswerCorrect=false;								
							}
						}
					}				
					
					if ((answer[2][1]==1) ||
						(answer[3][1]==1) ||
						(answer[5][1]==1) ||
						(answer[6][1]==1) ||
						(answer[5][3]==1) ||
						(answer[6][3]==1)) {
						isAnswerCorrect=false;						
					}
*/					

//					hasAnswered=true;
					this.invalidate();
					Log.d(">>>>>>>DONE","DONE");					
					return true;
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					currPosX = me.getX();
					currPosY = me.getY();

		        	//check if there is an intersection between currPosX and currPosY AND the bottles
		    		for (int i=0; i<totalObjects; i++) {
		    			if (myObjects[i].getIsVisible()) {
			    			if (myObjects[i].hasIntersectedWithPoint(currPosX, currPosY)) {
			    				myObjects[i].setVisible(View.INVISIBLE);
//				    			Log.d(">>>> currPos intersected!","Hooray!");
			    				//allow user to move the bottle object
//			    				myBottle[i].setXYPos(currPosX-myBottle[i].getWidth()/2,currPosY-myBottle[i].getHeight()/2);
			    				break;		    				
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