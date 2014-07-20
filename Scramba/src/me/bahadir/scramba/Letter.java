package me.bahadir.scramba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Letter {

	public static String[] letters = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	/*
	private Context context;
	private String value;
	private ImageView imgView;
	
	
	public Letter(Context context) {
		this(context, getRandomLetter());
	}
	
	public Letter(Context context, String value) {
		this.context = context;
		this.value = value;
		
		LayoutInflater inflater = LayoutInflater.from(context);
		
		imgView = (ImageView) inflater.inflate(R.layout.image_letter, (ViewGroup) context,false);
		
		lettersLayout.addView(letter);
		
	}
	*/
	public static String getRandomLetter() {
		return letters[Util.randInt(0, 25)];
	}
	
	
	
}
