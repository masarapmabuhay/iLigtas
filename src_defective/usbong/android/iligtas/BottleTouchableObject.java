package usbong.android.iligtas;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.ImageView;

public class BottleTouchableObject extends TouchableObject {
	public BottleTouchableObject(Context c) {
		super(c);

		myImageView = new ImageView(c);
		myImageView.setImageDrawable(c.getResources().getDrawable(R.drawable.bottle));
		((Activity)c).addContentView(myImageView, lp); 		
		
		offsetX=48;
		offsetY=48;
		offsetWidth=48;
		offsetHeight=48;	
	}
}
