package usbong.android.iligtas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/*
 * Reference: http://stackoverflow.com/questions/3616676/how-to-draw-a-line-in-android;
 * last accessed: 20 Nov. 2013
 */
public class Scenario_2_DrawView extends View implements OnTouchListener {
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
	
	private int numOfBoxesHorizontal=13;
	private int numOfBoxesVertical=8;
	
	private ImageView myBusImageView;
	private int myBusPosX=-50;
	private int myBusPosY=-50;

	private RelativeLayout.LayoutParams lp;
	
	//Reference: http://stackoverflow.com/questions/5255184/android-and-setting-width-and-height-programmatically-in-dp-units;
	//last accessed: Dec. 10, 2013; answer by Robby Pond
	final float scale = getContext().getResources().getDisplayMetrics().density;
	
	private Button hintButton;
	private Button naviButton;

    public Scenario_2_DrawView(Context context) {
        super(context);
		this.setOnTouchListener(this);

		lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		lastPosX=-1;//getWidth()/2;
		lastPosY=-1;//getHeight()/2;
		currPosX=-1;
		currPosY=-1;

		myBusImageView = (ImageView) ((Activity)context).findViewById(R.id.bus_imageview);				

		answer = new int[numOfBoxesHorizontal][numOfBoxesVertical];
		resetAnswers();
		
		correctAnswer = new int[numOfBoxesHorizontal][numOfBoxesVertical];
		correctAnswer[0][0] = 0;	correctAnswer[1][0] = 0;	correctAnswer[2][0] = 0;	correctAnswer[3][0] = 0;	correctAnswer[4][0] = 0;	correctAnswer[5][0] = 0;	correctAnswer[6][0] = 0;	correctAnswer[7][0] = 0;	correctAnswer[8][0] = 0;	correctAnswer[9][0] = 0;	correctAnswer[10][0] = 0;
		correctAnswer[0][1] = 0;	correctAnswer[1][1] = 0;	correctAnswer[2][1] = 0;	correctAnswer[3][1] = 0;	correctAnswer[4][1] = 0;	correctAnswer[5][1] = 0;	correctAnswer[6][1] = 0;	correctAnswer[7][1] = 1;	correctAnswer[8][1] = 1;	correctAnswer[9][1] = 0;	correctAnswer[10][1] = 0;
		correctAnswer[0][2] = 0;	correctAnswer[1][2] = 0;	correctAnswer[2][2] = 0;	correctAnswer[3][2] = 0;	correctAnswer[4][2] = 0;	correctAnswer[5][2] = 0;	correctAnswer[6][2] = 0;	correctAnswer[7][2] = 1;	correctAnswer[8][2] = 0;	correctAnswer[9][2] = 0;	correctAnswer[10][2] = 0;
		correctAnswer[0][3] = 0;	correctAnswer[1][3] = 1;	correctAnswer[2][3] = 1;	correctAnswer[3][3] = 1;	correctAnswer[4][3] = 1;	correctAnswer[5][3] = 0;	correctAnswer[6][3] = 0;	correctAnswer[7][3] = 1;	correctAnswer[8][3] = 0;	correctAnswer[9][3] = 0;	correctAnswer[10][3] = 0;
		correctAnswer[0][4] = 0;	correctAnswer[1][4] = 1;	correctAnswer[2][4] = 0;	correctAnswer[3][4] = 0;	correctAnswer[4][4] = 1;	correctAnswer[5][4] = 0;	correctAnswer[6][4] = 0;	correctAnswer[7][4] = 1;	correctAnswer[8][4] = 0;	correctAnswer[9][4] = 0;	correctAnswer[10][4] = 0;
		correctAnswer[0][5] = 0;	correctAnswer[1][5] = 0;	correctAnswer[2][5] = 0;	correctAnswer[3][5] = 0;	correctAnswer[4][5] = 1;	correctAnswer[5][5] = 1;	correctAnswer[6][5] = 1;	correctAnswer[7][5] = 1;	correctAnswer[8][5] = 0;	correctAnswer[9][5] = 0;	correctAnswer[10][5] = 0;	
		correctAnswer[0][6] = 0;	correctAnswer[1][6] = 0;	correctAnswer[2][6] = 0;	correctAnswer[3][6] = 0;	correctAnswer[4][6] = 0;	correctAnswer[5][6] = 0;	correctAnswer[6][6] = 0;	correctAnswer[7][6] = 0;	correctAnswer[8][6] = 0;	correctAnswer[9][6] = 0;	correctAnswer[10][6] = 0;
		correctAnswer[0][7] = 0;	correctAnswer[1][7] = 0;	correctAnswer[2][7] = 0;	correctAnswer[3][7] = 0;	correctAnswer[4][7] = 0;	correctAnswer[5][7] = 0;	correctAnswer[6][7] = 0;	correctAnswer[7][7] = 0;	correctAnswer[8][7] = 0;	correctAnswer[9][7] = 0;	correctAnswer[10][7] = 0;	
		//correctAnswer[7][4] is special; refer to checking the answer <-- not anymore

    	hintButton = (Button)((Activity)context).findViewById(R.id.hint_button);		
    	hintButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				AlertDialog.Builder prompt = new AlertDialog.Builder(new ContextThemeWrapper(Scenario_1_Activity.this, 5));
				AlertDialog.Builder prompt = new AlertDialog.Builder(Scenario_2_Activity.instance);
				prompt.setTitle("Hint");
				prompt.setMessage(R.string.hint_scenario_2);
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
				AlertDialog.Builder prompt = new AlertDialog.Builder(Scenario_2_Activity.instance);
				prompt.setTitle("Navigation");
				prompt.setMessage(R.string.navigation);
				prompt.setPositiveButton("Main Menu", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Scenario_2_Activity.instance.returnToMainMenu();
					}
				});
				prompt.setNegativeButton("About", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Scenario_2_Activity.instance.goToAbout();
					}
				});
				prompt.show();

			}
    	});
		
		hasAnswered=false;
		isAnswerCorrect=true;
//		resetSettings();		
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
				
		if ((myBusPosX==-50) && (myBusPosY==-50)) {
			resetBusPosition();
		}
		lp.setMargins(myBusPosX, myBusPosY, 0, 0);
        myBusImageView.setLayoutParams(lp);
        myBusImageView.setVisibility(View.VISIBLE);
        
        if (hasAnswered) {
			Log.d(">>>>>>>> is answer correct?:",""+isAnswerCorrect);
			
			if (isAnswerCorrect) {
//				resetSettings();
		
				//so that the bus's position doesn't go back to the start position while the scenario is transitioning to the next scenario
		        myBusImageView.setVisibility(View.INVISIBLE);
				
				//do resetAnswers(), etc so that if the back key is pressed it'll return to the previous scenario
				((ScenarioActivity)this.getContext()).processNextScreen();
			}
			else {
				resetSettings();
			}
		}
    }
    
    public void resetSettings() {    	
    	resetBusPosition();	//this should come before isAnswerCorrect
		hasAnswered=false;
		isAnswerCorrect=true;
		resetAnswers();
		this.onSizeChanged(this.getWidth(),this.getHeight(),0,0);
//		this.invalidate();
    }
    
    public void resetBusPosition() {
		int bus_offsetX = (int) (7 * scale + 0.5f);

    	//set bus position to [1][5]
		myBusPosX = myWidth*1+bus_offsetX;
		myBusPosY = myHeight*5;
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
        }
		try{
		  myBitmap = Bitmap.createBitmap(this.getWidth(),this.getHeight(),Bitmap.Config.ARGB_4444);//ARGB_8888
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
        	Scenario_2_DrawView.this.update();
        	Scenario_2_DrawView.this.invalidate();
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
/*							
							if (answer[i][k]!=correctAnswer[i][k]) {
								isAnswerCorrect=false;
							}
*/							
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
/*					
					//this is to allow multiple solutions
					if ((answer[7][4]!=1)){ //&& (answer[11][4]!=1)) {
						isAnswerCorrect=false;												
					}
*/
					hasAnswered=true;
					this.invalidate();
					Log.d(">>>>>>>DONE","DONE");					
					return true;
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					currPosX = me.getX();
					currPosY = me.getY();
						
					myBusPosX = (int)currPosX-myBusImageView.getWidth()/2;
					myBusPosY = (int)currPosY-myBusImageView.getHeight()/2;
					
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