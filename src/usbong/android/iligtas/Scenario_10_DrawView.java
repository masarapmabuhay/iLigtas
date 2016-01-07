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
import android.view.ViewGroup;
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
public class Scenario_10_DrawView extends View implements OnTouchListener {
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

	/*
	private ImageView myBottleImageView;
	private int myBottlePosX=-50;
	private int myBottlePosY=-50;
*/
	private LeftItemTouchableObject[] myLeftItem;
	private final int totalLeftItems = 6;
//	private boolean isMovingAnObject;

	private RightItemTouchableObject[] myRightItem;
	private final int totalRightItems = 3;//6;

	private boolean hasInitItems;

	private float leftPatternPosX;
	private float leftPatternPosY;
	private int leftPatternWidth;
	private int leftPatternHeight;

	private float rightPatternPosX;
	private float rightPatternPosY;
	private int rightPatternWidth;
	private int rightPatternHeight;

	
	private ImageView myScenarioImageView;
	
	//Reference: http://stackoverflow.com/questions/5255184/android-and-setting-width-and-height-programmatically-in-dp-units;
	//last accessed: Dec. 10, 2013; answer by Robby Pond
	final float scale = getContext().getResources().getDisplayMetrics().density;
	
//	private FrameLayout.LayoutParams lp;
//	private RelativeLayout.LayoutParams lp;
	
	private Button hintButton;
	private Button naviButton;

    public Scenario_10_DrawView(Context context) {
        super(context);
		this.setOnTouchListener(this);

//		myScenarioImageView = (ImageView) ((Activity)context).findViewById(R.id.scenario_imageview);				

/*		
		lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		myScenarioImageView = new ImageView(context);
		myScenarioImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.scenario_4));
		((Activity)context).addContentView(myScenarioImageView, lp); 		
//=====
		ImageView iv = (ImageView) ((Activity)context).findViewById(R.id.fire_imageview);
		Log.d(">>>>>> ImageView iv =",""+iv);
*/
		myLeftItem = new LeftItemTouchableObject[totalLeftItems];
		myLeftItem[0] = new LeftItemTouchableObject(context, (ImageView) ((Activity)context).findViewById(R.id.fire_imageview));
		myLeftItem[1] = new LeftItemTouchableObject(context, (ImageView) ((Activity)context).findViewById(R.id.shoelace_imageview));
		myLeftItem[2] = new LeftItemTouchableObject(context, (ImageView) ((Activity)context).findViewById(R.id.bowl_imageview));
		myLeftItem[3] = new LeftItemTouchableObject(context, (ImageView) ((Activity)context).findViewById(R.id.can_1_imageview));
		myLeftItem[4] = new LeftItemTouchableObject(context, (ImageView) ((Activity)context).findViewById(R.id.can_2_imageview));
		myLeftItem[5] = new LeftItemTouchableObject(context, (ImageView) ((Activity)context).findViewById(R.id.can_3_imageview));

		for(int i=0; i<totalLeftItems; i++) {
			myLeftItem[i].setVisible(View.INVISIBLE);			
		}

		myRightItem = new RightItemTouchableObject[totalRightItems];
		myRightItem[0] = new RightItemTouchableObject(context, (ImageView) ((Activity)context).findViewById(R.id.sock_imageview));
		myRightItem[1] = new RightItemTouchableObject(context, (ImageView) ((Activity)context).findViewById(R.id.dirty_water_imageview));
		myRightItem[2] = new RightItemTouchableObject(context, (ImageView) ((Activity)context).findViewById(R.id.water_bowl_imageview));

		for(int i=0; i<totalRightItems; i++) {
			myRightItem[i].setVisible(View.INVISIBLE);			
		}

		
/*		
		lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
*/
		lastPosX=-1;//getWidth()/2;
		lastPosY=-1;//getHeight()/2;
		currPosX=-1;
		currPosY=-1;
/*
		myBottleImageView = new ImageView(context);
		myBottleImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.bottle));
		
		((Activity)context).addContentView(myBottleImageView, lp); 
*/
/*		
		hasAnswered=false;
		isAnswerCorrect=false;
*/		
    	hintButton = (Button)((Activity)context).findViewById(R.id.hint_button);		
    	hintButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				AlertDialog.Builder prompt = new AlertDialog.Builder(new ContextThemeWrapper(Scenario_1_Activity.this, 5));
				AlertDialog.Builder prompt = new AlertDialog.Builder(Scenario_10_Activity.instance);
				prompt.setTitle("Hint");
				prompt.setMessage(R.string.hint_scenario_10);
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
				AlertDialog.Builder prompt = new AlertDialog.Builder(Scenario_10_Activity.instance);
				prompt.setTitle("Navigation");
				prompt.setMessage(R.string.navigation);
				prompt.setPositiveButton("Main Menu", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Scenario_10_Activity.instance.returnToMainMenu();
					}
				});
				prompt.setNegativeButton("About", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Scenario_10_Activity.instance.goToAbout();
					}
				});
				prompt.show();

			}
    	});

		update();
    }

    public void removeItems(ViewGroup root) {
		for (int i=0; i<totalLeftItems; i++) {
			if (myLeftItem[i]!=null) {
				myLeftItem[i].removeImageView(root);
			}
		}
//		rootView.removeAllViews();
    }
    
    public boolean isTaskCompleted() {
    	return isAnswerCorrect;
    }
    
    @Override
    public void onDraw(Canvas canvas) {
    	Log.d(">>>inside","onDraw");
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
    	//init left pattern
    	leftPatternPosX=myWidth*3;
    	leftPatternPosY=myHeight*2;
    	leftPatternWidth=myWidth*2;
    	leftPatternHeight=myHeight*3;

    	//init right pattern
    	rightPatternPosX=myWidth*9;
    	rightPatternPosY=myHeight*1;
    	rightPatternWidth=myWidth*1;
    	rightPatternHeight=myHeight*4;

    	
    	/*
		try{
			canvas.drawBitmap(myBitmap,
					new Rect(0,0, myBitmap.getWidth(),myBitmap.getHeight()),
					new Rect(0,0, myBitmap.getWidth(),myBitmap.getHeight()),
					paint);//0,0,null);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
*/
    	
    	for (int i=0; i<totalLeftItems; i++) {
			if (!isAnswerCorrect) {
				myLeftItem[i].draw(canvas);
			}
		}

    	for (int i=0; i<totalRightItems; i++) {
			if (!isAnswerCorrect) {
				myRightItem[i].draw(canvas);
			}
		}

    	//put this after .draw(canvas) so that ImageView.getHeight() will work.
    	if (!hasInitItems) {
			resetItemsPosition();
	    	for (int i=0; i<totalLeftItems; i++) {
				myLeftItem[i].setVisible(View.VISIBLE);
	    	}
	    	for (int i=0; i<totalRightItems; i++) {
				myRightItem[i].setVisible(View.VISIBLE);
	    	}
			hasInitItems=true;
		}

/*
    	for (int i=0; i<totalBottles; i++) {
			Log.d(">>>>>>","inside for loop");			
			
			if (!hasInitBottles) {
				Log.d(">>>>>>","!hasInitBottles");			
				resetBottlesPosition();
				myBottle[i].setVisible(View.VISIBLE);
				
				if (i==totalBottles-1) { //if this is the last bottle
					hasInitBottles=true;
				}
			}
			if (!isAnswerCorrect) {
				Log.d(">>>>>>","!isAnswerCorrect");			
				myBottle[i].draw(canvas);
			}
		}
*/    	
//        if (hasAnswered) {
			Log.d(">>>>>>>> is answer correct?:",""+isAnswerCorrect);
			
			if (isAnswerCorrect) {
//				resetSettings();
/*
				isAnswerCorrect=false;
				hasInitBottles=false;
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
    	Log.d(">>>inside","resetSettings()");

    	resetItemsPosition();	//this should come before isAnswerCorrect
		hasAnswered=false;
		isAnswerCorrect=false;
//		resetAnswers();
//		this.onSizeChanged(this.getWidth(),this.getHeight(),0,0);
//		this.invalidate();
		
		hasInitItems=false;
    }
    
    public void resetItemsPosition() {
    	int fire_offsetX = (int) (386 * scale + 0.5f);
      	int fire_offsetY = (int) (50 * scale + 0.5f);
      	int left_item_spacing_offsetY_1 = (int) (60 * scale + 0.5f);
      	int left_item_spacing_offsetY_2 = (int) (60 * scale + 0.5f);
      	int left_item_spacing_offsetY_3 = (int) (40 * scale + 0.5f);
      	int left_item_spacing_offsetY_4 = (int) (140 * scale + 0.5f);
      	int left_item_spacing_offsetY_5 = (int) (140 * scale + 0.5f);

    	int sock_offsetX = (int) (440 * scale + 0.5f); //width - offset
      	int sock_offsetY = (int) (80 * scale + 0.5f);
      	int right_item_spacing_offsetY_1 = (int) (140 * scale + 0.5f);
      	int right_item_spacing_offsetY_2 = (int) (72 * scale + 0.5f);

      	myLeftItem[0].setXYPos(fire_offsetX,fire_offsetY);
		myLeftItem[1].setXYPos(fire_offsetX, fire_offsetY/*+myLeftItem[0].getHeight()*/+left_item_spacing_offsetY_1);
		myLeftItem[2].setXYPos(fire_offsetX, fire_offsetY+left_item_spacing_offsetY_1+left_item_spacing_offsetY_2);
		myLeftItem[3].setXYPos(fire_offsetX, fire_offsetY+left_item_spacing_offsetY_1+left_item_spacing_offsetY_2*2);
		myLeftItem[4].setXYPos(fire_offsetX, fire_offsetY+left_item_spacing_offsetY_1+left_item_spacing_offsetY_2*2+left_item_spacing_offsetY_3);
		myLeftItem[5].setXYPos(fire_offsetX, fire_offsetY+left_item_spacing_offsetY_1+left_item_spacing_offsetY_2*2+left_item_spacing_offsetY_3*2);
		
		myRightItem[0].setXYPos(getWidth()-sock_offsetX,sock_offsetY);
		myRightItem[1].setXYPos(getWidth()-sock_offsetX, sock_offsetY/*+myLeftItem[0].getHeight()*/+right_item_spacing_offsetY_1);
		myRightItem[2].setXYPos(getWidth()-sock_offsetX, sock_offsetY+right_item_spacing_offsetY_1+right_item_spacing_offsetY_2);
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
		  myBitmap = Bitmap.createBitmap(this.getWidth(),this.getHeight(),Bitmap.Config.ARGB_4444);
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
        	Scenario_10_DrawView.this.update();
        	Scenario_10_DrawView.this.invalidate();
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
					
		    		for (int i=0; i<totalLeftItems; i++) {
		    			//check if the item is in the left pattern
		    			if (myLeftItem[i].hasIntersectedWithRectangle(leftPatternPosX,
		    														  leftPatternPosY,
		    														  leftPatternWidth,
		    														  leftPatternHeight)) {
//		    				myLeftItem[i].setVisible(View.INVISIBLE);
		    				myLeftItem[i].setIsMovable(false);
		    			}		    			
		    		}

		    		for (int i=0; i<totalRightItems; i++) {
		    			//check if the item is in the left pattern
		    			if (myRightItem[i].hasIntersectedWithRectangle(rightPatternPosX,
		    														  rightPatternPosY,
		    														  rightPatternWidth,
		    														  rightPatternHeight)) {
//		    				myRightItem[i].setVisible(View.INVISIBLE);
		    				myRightItem[i].setIsMovable(false);		    				
		    			}		    			
		    		}

		    		
		    		//check if all the bottles have been placed in the bag
		    		boolean hasRemainingVisibleBottle=false;
		    		for (int i=0; i<totalLeftItems; i++) {
//		    			if (myLeftItem[i].getIsVisible()) {		    			
		    			if (myLeftItem[i].getIsMovable()) {
		    				hasRemainingVisibleBottle=true;
		    			}
		    		}
		    		for (int i=0; i<totalRightItems; i++) {
//		    			if (myRightItem[i].getIsVisible()) {
		    			if (myRightItem[i].getIsMovable()) {
		    				hasRemainingVisibleBottle=true;
		    			}
		    		}

		    		if (!hasRemainingVisibleBottle) {
						isAnswerCorrect=true;										    			
		    		}

//					hasAnswered=true;
					this.invalidate();
					return true;
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					currPosX = me.getX();
					currPosY = me.getY();

		        	//check if there is an intersection between currPosX and currPosY AND left items
		    		for (int i=0; i<totalLeftItems; i++) {
		    			if (myLeftItem[i].getIsMovable()) {
//		    			if (myLeftItem[i].getIsVisible()) {
			    			if (myLeftItem[i].hasIntersectedWithPoint(currPosX, currPosY)) {
			    				//allow user to move the item
			    				myLeftItem[i].setXYPos(currPosX-myLeftItem[i].getWidth()/2,currPosY-myLeftItem[i].getHeight()/2);
								this.invalidate();
								return true;
//			    				break;		    				
				    		}
		    			}
		    		}

		        	//check if there is an intersection between currPosX and currPosY AND right items
		    		for (int i=0; i<totalRightItems; i++) {
//		    			if (myRightItem[i].getIsVisible()) {
		    			if (myRightItem[i].getIsMovable()) {
		    				if (myRightItem[i].hasIntersectedWithPoint(currPosX, currPosY)) {
			    				//allow user to move the item
			    				myRightItem[i].setXYPos(currPosX-myRightItem[i].getWidth()/2,currPosY-myRightItem[i].getHeight()/2);
								this.invalidate();
								return true;
//			    				break;		    				
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