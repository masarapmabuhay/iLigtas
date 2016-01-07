package usbong.android.iligtas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class Scenario_10_Activity extends ScenarioActivity {
	private final GestureDetector gdt = new GestureDetector(new GestureListener(this));
	private Scenario_10_DrawView drawView;
	public static Scenario_10_Activity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.scenario_10);	
/*		
		ImageView iv = (ImageView) findViewById(R.id.fire_imageview);
		Log.d(">>>>>> fire_imageview =",""+iv);
*/
//		instance = this;
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
		setContentView(R.layout.scenario_10);	
		instance = this;

		this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

		toNextIntent = new Intent().setClass(this, Scenario_11_Activity.class);
		toPreviousIntent = new Intent().setClass(this, Scenario_9_Activity.class);		
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
/*
    	if (drawView==null) {
            Log.d(">>>>>>>drawView==","null!");
    		drawView = new Scenario_4_DrawView(this);
            drawView.resetSettings();
          	this.addContentView(drawView, new LayoutParams( ViewGroup.LayoutParams.FILL_PARENT , ViewGroup.LayoutParams.FILL_PARENT ) );
    	}
    	else{
//    		ViewGroup parent = (ViewGroup) drawView.getParent();
//            parent.removeView(drawView);
            
//    		drawView = new Scenario_4_DrawView(this);
    		Log.d(">>>>>>>else isTaskCompleted()...","");
    		if (drawView.isTaskCompleted()) {
//                Log.d(">>>>>>>drawView.resetSettings","");
    			drawView.resetSettings();
    		}    		
    	}
  */
	
		if (drawView!=null) {
    		ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
/*    		
    		for (int i = 0; i < rootView.getChildCount(); i++) {
    		    if(rootView.getChildAt(i) == drawView) {
    		        // do anything here
    		    	rootView.removeViewAt(i);
    		    }
    		}
*/
//    		rootView.removeAllViews();
    		drawView.removeItems(rootView);
		}
		drawView = new Scenario_10_DrawView(this);
        drawView.resetSettings();		
    	this.addContentView(drawView, new LayoutParams( ViewGroup.LayoutParams.FILL_PARENT , ViewGroup.LayoutParams.FILL_PARENT ) );    	
	}
}
