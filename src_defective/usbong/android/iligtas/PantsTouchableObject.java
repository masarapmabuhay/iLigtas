package usbong.android.iligtas;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.ImageView;

public class PantsTouchableObject extends TouchableObject {
	public PantsTouchableObject(Context c) {
		super(c);

		myImageView = new ImageView(c);
		myImageView.setImageDrawable(c.getResources().getDrawable(R.drawable.pants));
		((Activity)c).addContentView(myImageView, lp); 		
/*		
		offsetWidth=16;
		offsetHeight=16;	
*/		
	}
}
