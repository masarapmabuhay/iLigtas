package usbong.android.iligtas;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class GestureListener extends SimpleOnGestureListener {
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	private boolean hasPerformedSwipeMotion=false;
	
	ScenarioActivity myScenarioActivity;
	public GestureListener(ScenarioActivity a) {
		myScenarioActivity=a;
	}
	
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    	hasPerformedSwipeMotion=false;
    	
    	if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
        	myScenarioActivity.processRightToLeftSwipe();
        	hasPerformedSwipeMotion=true; // Right to left
        }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
           	myScenarioActivity.processLeftToRightSwipe();
           	hasPerformedSwipeMotion=true; // Left to right
        }
        if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            myScenarioActivity.processBottomToTopSwipe();
            hasPerformedSwipeMotion=true; // Bottom to top
        }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            myScenarioActivity.processTopToBottomSwipe();
            hasPerformedSwipeMotion=true; // Top to bottom
        }
        
        if (hasPerformedSwipeMotion) {
        	return true;
        }
        else {
        	return false;
        }
    }
}