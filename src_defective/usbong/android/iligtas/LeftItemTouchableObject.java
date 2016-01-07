package usbong.android.iligtas;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

public class LeftItemTouchableObject extends TouchableObject {
	public LeftItemTouchableObject(Context c, ImageView iv) {
		super(c);
/*
		myImageView = new ImageView(c);
		myImageView.setImageDrawable(c.getResources().getDrawable(R.drawable.bottle));
		((Activity)c).addContentView(myImageView, lp); 		
*/
		myImageView = iv;

		Log.d(">>>>>> inside LeftItemTouchableObject",""+iv);
		
		offsetX=48;
		offsetY=48;
		offsetWidth=48;
		offsetHeight=48;	
	}
}
