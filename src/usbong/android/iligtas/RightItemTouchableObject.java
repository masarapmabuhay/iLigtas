package usbong.android.iligtas;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

public class RightItemTouchableObject extends TouchableObject {
	private boolean isMovable;

	public RightItemTouchableObject(Context c, ImageView iv) {
		super(c);
/*
		myImageView = new ImageView(c);
		myImageView.setImageDrawable(c.getResources().getDrawable(R.drawable.bottle));
		((Activity)c).addContentView(myImageView, lp); 		
*/
		myImageView = iv;

		offsetX=48;
		offsetY=48;
		offsetWidth=48;
		offsetHeight=48;	

		isMovable=true;
	}
	
	public boolean getIsMovable() {
		return isMovable;
	}
	
	public void setIsMovable(boolean b) {
		isMovable=b;
	}
}
