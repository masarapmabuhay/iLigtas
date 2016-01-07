package usbong.android.iligtas;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

public class Scenario_8_Activity extends ScenarioActivity {
	private final GestureDetector gdt = new GestureDetector(new GestureListener(this));
	private ImageView momImageView;
	private TranslateAnimation momAnimation;
	private TranslateAnimation momAnimationToBoat;

	private ImageView youImageView;
	private TranslateAnimation youAnimation;
	private TranslateAnimation youAnimationToBoat;

	private ImageView babyImageView;
	private TranslateAnimation babyAnimation;
	private TranslateAnimation babyAnimationToBoat;

	private DisplayMetrics metrics;

	//Reference: http://stackoverflow.com/questions/5255184/android-and-setting-width-and-height-programmatically-in-dp-units;
	//last accessed: Dec. 10, 2013; answer by Robby Pond
	private float scale;
	private int distanceToFlood;
	private int babyDistanceToFlood;

	private int distanceToBoat;
	private int momDistanceToBoat;

	private int youStartPosX;
	private int babyStartPosX;
	private int momStartPosX;
	
	private boolean isMomAnimationStarted;
	private boolean isYouAnimationStarted;
	private boolean isBabyAnimationStarted;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scenario_8);	
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
	    	youImageView = (ImageView)findViewById(R.id.you_imageview);
	    	LayoutParams lp = new LayoutParams(youImageView.getWidth(), youImageView.getHeight());
	        lp.setMargins(youStartPosX, distanceToFlood, 0, 0);
	    	youImageView.setLayoutParams(lp);    	        

    		babyImageView = (ImageView)findViewById(R.id.baby_imageview);
	    	lp = new LayoutParams(babyImageView.getWidth(), babyImageView.getHeight());
	        lp.setMargins(babyStartPosX, babyDistanceToFlood, 0, 0);
	    	babyImageView.setLayoutParams(lp);    	        

    		momImageView = (ImageView)findViewById(R.id.mom_imageview);
	    	lp = new LayoutParams(momImageView.getWidth(), momImageView.getHeight());
	        lp.setMargins(momStartPosX, distanceToFlood, 0, 0);
	    	momImageView.setLayoutParams(lp);    	        

	        isYouAnimationStarted=false;
	        isBabyAnimationStarted=false;
	        isMomAnimationStarted=false;
	        
	        hasCompletedTask=false;
    	}
    }

    @Override
	public void onBackPressed() {
    	processPreviousScreen();
    }
    
    @Override
    public void onWindowFocusChanged(boolean focus) {
       super.onWindowFocusChanged(focus);
       // get the imageviews width and height here
	   	youAnimation = new TranslateAnimation(0,0,0,distanceToFlood/*-momImageView.getHeight()*/);//(0, 0, floodImageView.getTop(), metrics.heightPixels);
	   	youAnimation.setDuration(400);
	   	youAnimation.setFillAfter(false);
	   	youAnimation.setAnimationListener(new MyAnimationListener(this));

	   	babyAnimation = new TranslateAnimation(0,0,0,babyDistanceToFlood/*-momImageView.getHeight()*/);//(0, 0, floodImageView.getTop(), metrics.heightPixels);
	   	babyAnimation.setDuration(400);
	   	babyAnimation.setFillAfter(false);
	   	babyAnimation.setAnimationListener(new MyAnimationListener(this));

	   	momAnimation = new TranslateAnimation(0,0,0,distanceToFlood/*-momImageView.getHeight()*/);//(0, 0, floodImageView.getTop(), metrics.heightPixels);
	   	momAnimation.setDuration(400);
	   	momAnimation.setFillAfter(false);
	   	momAnimation.setAnimationListener(new MyAnimationListener(this));
	   	
	   	youAnimationToBoat = new TranslateAnimation(0,distanceToBoat,0,0);
	   	youAnimationToBoat.setDuration(1200);
	   	youAnimationToBoat.setFillAfter(false);
	   	youAnimationToBoat.setAnimationListener(new MyAnimationListener(this));

	   	babyAnimationToBoat = new TranslateAnimation(0,distanceToBoat,0,0);
	   	babyAnimationToBoat.setDuration(1200);
	   	babyAnimationToBoat.setFillAfter(false);
	   	babyAnimationToBoat.setAnimationListener(new MyAnimationListener(this));

	   	momAnimationToBoat = new TranslateAnimation(0,momDistanceToBoat,0,0);
	   	momAnimationToBoat.setDuration(1200);
	   	momAnimationToBoat.setFillAfter(false);
	   	momAnimationToBoat.setAnimationListener(new MyAnimationListener(this));
    }
    
	@Override
	public void init() {		
		this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

		toNextIntent = new Intent().setClass(this, Scenario_9_Activity.class);
		toPreviousIntent = new Intent().setClass(this, Scenario_7_Activity.class);	
		
    	myImageView = (ImageView)findViewById(R.id.scenario_imageview);
    	myImageView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdt.onTouchEvent(event);
                Log.d(">>>>>>>touched!","touched!");
                return true;
            }
        });

    	if (momImageView==null) {
    		youImageView = (ImageView)findViewById(R.id.you_imageview);    		
    		babyImageView = (ImageView)findViewById(R.id.baby_imageview);
    		momImageView = (ImageView)findViewById(R.id.mom_imageview);
    	}
    	
    	metrics = this.getResources().getDisplayMetrics();    	
    	scale = metrics.density;	
    	distanceToFlood = (int) (300 * scale + 0.5f);
    	babyDistanceToFlood = (int) (328 * scale + 0.5f);

    	youStartPosX = (int) (248 * scale + 0.5f);    	
    	babyStartPosX = (int) (172 * scale + 0.5f);    	
    	momStartPosX = (int) (116 * scale + 0.5f);    	

    	distanceToBoat = (int) (512 * scale + 0.5f);
    	momDistanceToBoat = (int) (478 * scale + 0.5f);
	}
	
	//Reference: http://stackoverflow.com/questions/11144421/move-an-imageview-to-different-position-in-animated-way-in-android;
	//last accessed: 29 Nov. 2013;
	//answer by: D-32
	private class MyAnimationListener implements AnimationListener{
		private Scenario_8_Activity a;
		public MyAnimationListener(Scenario_8_Activity a) {
			this.a = a;
		}
		
	    @Override
	    public void onAnimationEnd(Animation a) {
	    	if (a==youAnimation) {	    		
		    	youImageView.clearAnimation();
		        LayoutParams lp = new LayoutParams(youImageView.getWidth(), youImageView.getHeight());
		        lp.setMargins(youStartPosX, distanceToFlood, 0, 0);
		        youImageView.setLayoutParams(lp);
	    		youImageView.startAnimation(youAnimationToBoat);    	
	    	}
	    	else if (a==babyAnimation) {	    		
		    	babyImageView.clearAnimation();
		        LayoutParams lp = new LayoutParams(babyImageView.getWidth(), babyImageView.getHeight());
		        lp.setMargins(babyStartPosX, babyDistanceToFlood, 0, 0);
		        babyImageView.setLayoutParams(lp);
	    		babyImageView.startAnimation(babyAnimationToBoat);    	
	    	}
	    	else if (a==momAnimation) {	    		
		    	momImageView.clearAnimation();
		        LayoutParams lp = new LayoutParams(momImageView.getWidth(), momImageView.getHeight());
		        lp.setMargins(momStartPosX, distanceToFlood, 0, 0);
		        momImageView.setLayoutParams(lp);
	//	        hasCompletedTask=true;
	    		momImageView.startAnimation(momAnimationToBoat);    	
	    	}
	    	else if (a==youAnimationToBoat){
		    	youImageView.clearAnimation();
		        LayoutParams lp = new LayoutParams(youImageView.getWidth(), youImageView.getHeight());
		        lp.setMargins(youStartPosX+distanceToBoat, distanceToFlood, 0, 0);
		        youImageView.setLayoutParams(lp);
	    	}	        
	    	else if (a==babyAnimationToBoat){
		    	babyImageView.clearAnimation();
		        LayoutParams lp = new LayoutParams(babyImageView.getWidth(), babyImageView.getHeight());
		        lp.setMargins(babyStartPosX+distanceToBoat, babyDistanceToFlood, 0, 0);
		        babyImageView.setLayoutParams(lp);
	    	}	        
	    	else if (a==momAnimationToBoat){
		    	momImageView.clearAnimation();
		        LayoutParams lp = new LayoutParams(momImageView.getWidth(), momImageView.getHeight());
		        lp.setMargins(momStartPosX+momDistanceToBoat, distanceToFlood, 0, 0);
		        momImageView.setLayoutParams(lp);

		        ((ScenarioActivity)this.a).processNextScreen();
	    	}	        
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
		if (!isYouAnimationStarted) {
    		isYouAnimationStarted=true;
			youImageView.startAnimation(youAnimation);    	
		}
		else if (!isBabyAnimationStarted) {
    		isBabyAnimationStarted=true;
			babyImageView.startAnimation(babyAnimation);    	
		}
		else if (!isMomAnimationStarted) {
    		isMomAnimationStarted=true;
			momImageView.startAnimation(momAnimation);    	
		}
	}
	
	@Override
	public void processRightToLeftSwipe() {		
		//do nothing
	}
}