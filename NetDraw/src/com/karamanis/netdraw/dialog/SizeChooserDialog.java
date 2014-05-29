package com.karamanis.netdraw.dialog;

import android.app.Dialog;
import android.content.Context;

public class SizeChooserDialog extends Dialog{
	
	    public SizeChooserDialog(Context context, int size) {
	    	super(context, size);
	    	//View view = inflater.inflate(R.layout.dialog_sizepen, container);
		
	    }

		public interface OnColorChangedListener {
	        void colorChanged(int color);
	    }

}
