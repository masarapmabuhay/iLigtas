package usbong.android.iligtas;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

public class TouchableObject {
	protected float myPosX=-50;
	protected float myPosY=-50;
	protected int myWidth;
	protected int myHeight;	
	
	protected ImageView myImageView;
	protected FrameLayout.LayoutParams lp;
	
//	protected boolean hasBeenInitiated;
	protected boolean isBeingMoved;

	protected int offsetX;
	protected int offsetY;	
	protected int offsetWidth;
	protected int offsetHeight;	

	public TouchableObject(Context c) {
		lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		myImageView = new ImageView(c);
		//do this in subclass
//		myImageView.setImageDrawable(c.getResources().getDrawable(R.drawable.bottle));
/*
		myWidth = myImageView.getWidth();
		myHeight = myImageView.getHeight();
		((Activity)c).addContentView(myImageView, lp); 
*/		
	}
	
	public void draw(Canvas c) {	
		//put these here because Android only knows the size of the imageview when it reaches draw(...)
		//Reference: http://stackoverflow.com/questions/17479391/get-imageview-width-and-height;
		//last accessed: 11 Dec. 2013; answer by Kaifei
		myWidth = myImageView.getWidth();
		myHeight = myImageView.getHeight();

    	lp.setMargins((int)myPosX, (int)myPosY, 0, 0);
        myImageView.setLayoutParams(lp);
//        myImageView.setVisibility(View.VISIBLE);
	}
	
	public void setXYPos(float x, float y) {
		myPosX = x;
		myPosY = y;
	}
/*	
	public boolean getHasBeenInitiated() {
		return hasBeenInitiated;
	}
*/	
	public boolean hasIntersectedWithPoint(float x, float y) {
/*//original
		//if x and y positions are outside the bounding box of this object
		if (((x < myPosX) || (x > myPosX + myWidth)) ||
			((y < myPosY) || (y > myPosY + myHeight))) {
			return false;
		}
		return true;
*/	
		//if x and y positions are inside the bounding box of this object
		if (((x >= myPosX-offsetX) && (x <= myPosX + myWidth+offsetWidth)) &&
			((y >= myPosY-offsetY) && (y <= myPosY + myHeight+offsetHeight))) {
			return true;
		}
		return false;		
	}

	public boolean hasIntersectedWithRectangle(float x, float y, int w, int h) {
/*
		Log.d("x and y",x+" and "+y);
		Log.d("w and h",w+" and "+h);
		Log.d("myPosX and myPosY",myPosX+" and "+myPosY);
		Log.d("myWidth and myHeight",myWidth+" and "+myHeight);		
*/
		//if the rectangle is outside the bounding box of this object
		if (((myPosX+myWidth < x) || (myPosX > x + w)) ||
			((myPosY+myHeight < y) || (myPosY > y + h))) {
				return false;
			}
		return true;
	}
	
	public boolean getIsVisible() {
		if (myImageView.getVisibility()==View.VISIBLE) {
			return true;
		}
		return false;
	}
	
	public void setVisible(int v) {
        myImageView.setVisibility(v);		
	}
	
	public int getWidth() {
		return myWidth;
	}
	
	public int getHeight() {
		return myHeight;
	}	
	
	public float getX() {
		return myPosX;
	}
	
	public float getY() {
		return myPosY;
	}
	
	public void removeImageView(ViewGroup root) {
		root.removeView(myImageView);
	}
}