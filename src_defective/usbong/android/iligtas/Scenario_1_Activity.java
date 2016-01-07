package usbong.android.iligtas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

public class Scenario_1_Activity extends ScenarioActivity {	
	private final GestureDetector gdt = new GestureDetector(new GestureListener(this));
	private Button hintButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scenario_1);			
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

		toNextIntent = new Intent().setClass(this, Scenario_2_Activity.class);
		toPreviousIntent = new Intent().setClass(this, MainActivity.class);		

    	myImageView = (ImageView)findViewById(R.id.scenario_imageview);
    	myImageView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdt.onTouchEvent(event);
                Log.d(">>>>>>>touched!","touched!");
                return true;
            }
        });

    	hintButton = (Button)findViewById(R.id.hint_button);		
    	hintButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				AlertDialog.Builder prompt = new AlertDialog.Builder(new ContextThemeWrapper(Scenario_1_Activity.this, 5));
				AlertDialog.Builder prompt = new AlertDialog.Builder(Scenario_1_Activity.this);
//				prompt.setInverseBackgroundForced(true);
				prompt.setTitle("Hint");
				prompt.setMessage(R.string.hint_scenario_1);
				prompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				prompt.show();

			}
    	});
    	
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 500 milliseconds
		v.vibrate(500);
	}	
}
