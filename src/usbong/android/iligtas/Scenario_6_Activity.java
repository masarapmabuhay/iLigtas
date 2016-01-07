package usbong.android.iligtas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class Scenario_6_Activity extends ScenarioActivity {
	private final GestureDetector gdt = new GestureDetector(new GestureListener(this));
	private Scenario_6_DrawView drawView;
	public static Scenario_6_Activity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scenario_6);	
		
		instance = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    @Override
    public void onResume() {
    	super.onResume();
		init();
/*	
    	if (hasCompletedTask) {
    	}
*/    	
    }

    @Override
	public void onBackPressed() {
    	processPreviousScreen();
    }
	
	@Override
	public void init() {		
		this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

		toNextIntent = new Intent().setClass(this, Scenario_7_Activity.class);
		toPreviousIntent = new Intent().setClass(this, Scenario_5_Activity.class);		
    	
    	drawView = new Scenario_6_DrawView(this);
//        drawView.setBackgroundColor(Color.WHITE);    	
    	this.addContentView(drawView, new LayoutParams( ViewGroup.LayoutParams.FILL_PARENT , ViewGroup.LayoutParams.FILL_PARENT ) );
	}
}
