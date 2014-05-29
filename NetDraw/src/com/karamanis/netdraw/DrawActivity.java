package com.karamanis.netdraw;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.karamanis.netdraw.dialog.ColorPickerDialog;

public class DrawActivity extends Activity implements ColorPickerDialog.OnColorChangedListener { 

	//DrawablePanel paintPanel;
	private DrawingView dw;
	private Paint mPaint;
	private ImageButton ibt_Color;
	private ImageButton ibt_Send;

	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);	
		mPaint = new Paint(); 
		/*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,			
					 					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		*/
		/* Enlever le titre des fenetres */
		requestWindowFeature(Window.FEATURE_NO_TITLE);					 

		setContentView(R.layout.activity_draw);	
		ibt_Color = (ImageButton) findViewById(R.id.ibt_Color);
		ibt_Color.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ColorPickerDialog c = new ColorPickerDialog(DrawActivity.this, DrawActivity.this,mPaint.getColor());
            	c.show();				
			}
		});
		
		ibt_Send = (ImageButton) findViewById(R.id.ibt_Send);
		ibt_Send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ColorPickerDialog c = new ColorPickerDialog(DrawActivity.this, DrawActivity.this,mPaint.getColor());
            	c.show();				
			}
		});
		
		dw = new DrawingView(this);
		LinearLayout drawLayout = (LinearLayout) findViewById(R.id.layoutDrawPanel);
		Log.d("MSG",drawLayout.getWidth()+"   "+drawLayout.getHeight());
		drawLayout.addView(dw,LayoutParams.FILL_PARENT);
		Log.d("MSG",drawLayout.getWidth()+"   "+drawLayout.getHeight());
		//paintPanel.requestFocus();	
		
		 overridePendingTransition(R.anim.slide_in, R.anim.slide_out); 
		   
	}
	private void sendImage(){
		
	}
	
	@Override
	public void onBackPressed() {
		 
		super.onBackPressed();
		overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out); 
	}
		
	public void OnResume()
	{
		super.onResume();
        setContentView(R.layout.activity_draw);		       
 
	}

	@Override
	public void colorChanged(int color) {
		dw.setPaintColor(color);
	}
}
