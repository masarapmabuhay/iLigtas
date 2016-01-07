package usbong.android.iligtas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

public class Scenario_3_Activity extends ScenarioActivity {
	private final GestureDetector gdt = new GestureDetector(new GestureListener(this));
	private ImageView floodImageView;
	private TranslateAnimation animation;
	private DisplayMetrics metrics;
	
	private Button hintButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scenario_3);	
		init();
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
    	
    	if (hasCompletedTask) {
	    	floodImageView = (ImageView)findViewById(R.id.scenario_flood_imageview);
	    	LayoutParams lp = new LayoutParams(floodImageView.getWidth(), floodImageView.getHeight());
	        lp.setMargins(0, -metrics.heightPixels, 0, 0);
	        floodImageView.setLayoutParams(lp);    	        
	        hasCompletedTask=false;
    	}
    }

    @Override
	public void onBackPressed() {
    	processPreviousScreen();
    }
    
	@Override
	public void init() {		
		this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

		toNextIntent = new Intent().setClass(this, Scenario_4_Activity.class);
		toPreviousIntent = new Intent().setClass(this, Scenario_2_Activity.class);	
		
    	myImageView = (ImageView)findViewById(R.id.scenario_imageview);
    	myImageView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdt.onTouchEvent(event);
                Log.d(">>>>>>>touched!","touched!");
                return true;
            }
        });

    	if (floodImageView==null) {
    		floodImageView = (ImageView)findViewById(R.id.scenario_flood_imageview);
    	}
    	
    	metrics = this.getResources().getDisplayMetrics();    	

    	animation = new TranslateAnimation(0,0,0,metrics.heightPixels);//(0, 0, floodImageView.getTop(), metrics.heightPixels);
    	animation.setDuration(1000);
    	animation.setFillAfter(false);
    	animation.setAnimationListener(new MyAnimationListener(this));
    	
    	hintButton = (Button)findViewById(R.id.hint_button);		
    	hintButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				AlertDialog.Builder prompt = new AlertDialog.Builder(new ContextThemeWrapper(Scenario_1_Activity.this, 5));
				AlertDialog.Builder prompt = new AlertDialog.Builder(Scenario_3_Activity.this);
//				prompt.setInverseBackgroundForced(true);
				prompt.setTitle("Hint");
				prompt.setMessage(R.string.hint_scenario_3);
				prompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				prompt.show();

			}
    	});
	}
	
	//Reference: http://stackoverflow.com/questions/11144421/move-an-imageview-to-different-position-in-animated-way-in-android;
	//last accessed: 29 Nov. 2013;
	//answer by: D-32
	private class MyAnimationListener implements AnimationListener{
		private Scenario_3_Activity a;
		public MyAnimationListener(Scenario_3_Activity a) {
			this.a = a;
		}
		
	    @Override
	    public void onAnimationEnd(Animation animation) {
	    	floodImageView.clearAnimation();
	        LayoutParams lp = new LayoutParams(floodImageView.getWidth(), floodImageView.getHeight());
	        lp.setMargins(0, metrics.heightPixels, 0, 0);
	        floodImageView.setLayoutParams(lp);
	        hasCompletedTask=true;
			((ScenarioActivity)this.a).processNextScreen();
	    }

	    @Override
	    public void onAnimationRepeat(Animation animation) {
	    }

	    @Override
	    public void onAnimationStart(Animation animation) {
	    }

	}
	
	@Override
	public void processTopToBottomSwipe() {		
		Log.d(">>>>>>> in processTopToBottomSwipe()","inside!");
    	floodImageView.startAnimation(animation);    	
	}
	
	@Override
	public void processRightToLeftSwipe() {		
		//do nothing
	}

}
