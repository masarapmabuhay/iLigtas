package usbong.android.iligtas;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class Scenario_2_Activity extends ScenarioActivity {
	private final GestureDetector gdt = new GestureDetector(new GestureListener(this));
	private Scenario_2_DrawView drawView;
	
	public static Scenario_2_Activity instance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scenario_2);	
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

		toNextIntent = new Intent().setClass(this, Scenario_3_Activity.class);
		toPreviousIntent = new Intent().setClass(this, Scenario_1_Activity.class);		
    	
    	drawView = new Scenario_2_DrawView(this);
//        drawView.setBackgroundColor(Color.WHITE);    	    	    	
    	this.addContentView(drawView, new LayoutParams( ViewGroup.LayoutParams.FILL_PARENT , ViewGroup.LayoutParams.FILL_PARENT ) );
	}
}
