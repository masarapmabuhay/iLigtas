package usbong.android.iligtas;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button myReadButton;
	private Intent readIntent;

	private Button myAboutButton;
	private Intent aboutIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    /*
     * Initialize this activity
     */
    public void init()
    {
    	myReadButton = (Button)findViewById(R.id.read_button);
		readIntent = new Intent().setClass(this, Scenario_1_Activity.class);
		
    	myReadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				finish();
				startActivity(readIntent);
			}
    	});
    	
    	myAboutButton = (Button)findViewById(R.id.about_button);
		aboutIntent = new Intent().setClass(this, AboutActivity.class);
		
    	myAboutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				finish();
				startActivity(aboutIntent);
			}
    	});

    }
}
