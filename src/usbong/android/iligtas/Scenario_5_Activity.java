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

public class Scenario_5_Activity extends ScenarioActivity {
	private final GestureDetector gdt = new GestureDetector(new GestureListener(this));
	private Scenario_5_DrawView drawView;
	public static Scenario_5_Activity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scenario_5);	
		
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

		toNextIntent = new Intent().setClass(this, Scenario_6_Activity.class);
		toPreviousIntent = new Intent().setClass(this, Scenario_4_Activity.class);		
/*
    	myImageView = (ImageView)findViewById(R.id.scenario_imageview);
    	myImageView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdt.onTouchEvent(event);
                Log.d(">>>>>>>touched!","touched!");
                return true;
            }
        });		
*/    	
    	
    	drawView = new Scenario_5_DrawView(this);
//        drawView.setBackgroundColor(Color.WHITE);    	
    	this.addContentView(drawView, new LayoutParams( ViewGroup.LayoutParams.FILL_PARENT , ViewGroup.LayoutParams.FILL_PARENT ) );
	}
/*	
	@Override
	public void processRightToLeftSwipe() {		
		//do nothing
	}

	@Override
	public void processLeftToRightSwipe() {		
		//do nothing
	}
*/	
}
