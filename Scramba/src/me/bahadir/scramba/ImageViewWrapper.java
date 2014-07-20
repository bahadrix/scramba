package me.bahadir.scramba;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ImageViewWrapper {

	private ImageView imgView;
	public final RelativeLayout.LayoutParams layoutParams;
	
	
	public ImageViewWrapper(ImageView imgView) {
		this.imgView = imgView;
		this.layoutParams = (LayoutParams) imgView.getLayoutParams();
	}



	public void setLeftMargin(int margin) {
		layoutParams.setMargins(margin, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
		imgView.setLayoutParams(layoutParams);
	}
	
	public void setTopMargin(int margin) {
		layoutParams.setMargins(layoutParams.leftMargin, margin, layoutParams.rightMargin, layoutParams.bottomMargin);
		imgView.setLayoutParams(layoutParams);
	}
}
