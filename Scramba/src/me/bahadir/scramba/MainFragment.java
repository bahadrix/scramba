package me.bahadir.scramba;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import me.bahadir.scramba.WordFactory.FactoryWord;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainFragment extends Fragment {

	public static final float LETTER_ALPHA_LOW = 0.5f;
	public static final float LETTER_ALPHA_HIGH = 1f;
	public static final long LETTER_ANIMATION_DURATION = 400;
	public static final int LETTER_FLASH_DELAY = 250;
	
	LayoutInflater inflater;
	ViewGroup container;
	RelativeLayout layoutLetters, layoutScene;
	TableLayout layoutDefinitions;
	private ImageView swap1 = null;
	private int letterWidth, letterHeight; 
	
	List<String> currentOrder = null;
	private WordFactory wordFactory;
	private FactoryWord currentWord;
	private boolean touchDisabled = false;
	List<ImageView> imgViewList = new LinkedList<>();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		letterWidth = getResources().getDimensionPixelSize(R.dimen.letter_width);
		letterHeight = getResources().getDimensionPixelSize(R.dimen.letter_height);
		
		layoutLetters = (RelativeLayout) rootView.findViewById(R.id.layoutLetters);
		layoutScene = (RelativeLayout) rootView.findViewById(R.id.layoutScene);
		layoutDefinitions = (TableLayout)rootView.findViewById(R.id.definitions_list);
		
		this.inflater = inflater;
		this.container = container;

		wordFactory = new WordFactory(this.getActivity());
		
		newGame();
		
		return rootView;
	}

	public void newGame() {
		imgViewList.clear();
		currentWord = wordFactory.getRandomWord();
		currentOrder = wordToList(currentWord.word);
		shuffleList(currentOrder);
		createLetters();
		loadMeanings();
		Log.i("baha","New game ready for word " + currentWord.word);
		
	}
	
	public void shuffleList(List<String> list) {
		List<String> original = new ArrayList<>(list);
		while(compareList(original, list)) {
			Collections.shuffle(list);
		}
	}
	
	public boolean compareList(List<String> l1, List<String> l2) {
		
		for(int i = 0; i < l1.size(); i++) {
			
			if(!l1.get(i).equals(l2.get(i)))
				return false;
		}
			
			
		return true;
	}
	
	public void loadMeanings() {
		
		layoutDefinitions.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(this.getActivity());
		
		for(String meaning : currentWord.meanings) {
			Log.i("baha",meaning);
			View defLayout = inflater.inflate(R.layout.item_definition, this.container, false);
			TextView txtDef = (TextView) defLayout.findViewById(R.id.txtDefinition);

			txtDef.setText(meaning.split(";")[0]);
			
			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)defLayout.getLayoutParams();
			params.setMargins(10, 10, 10, 10);
			
			layoutDefinitions.addView(defLayout);
			
		}
		
	}
	
	public void createLetters() {

		
		touchDisabled = false;
		String letter = "";
		layoutLetters.removeAllViews();
		try {
			for(int i = 0; i < currentOrder.size(); i++) {
				ImageView imgLetter = (ImageView) inflater.inflate(
						R.layout.image_letter,
						(ViewGroup) layoutLetters.getParent(), false);
				letter = currentOrder.get(i);
				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imgLetter
						.getLayoutParams();
				Bitmap bmp = getLetterBitmap("images/alphabet/Letter-" + letter + "-icon.png");
				imgLetter.setImageBitmap(bmp);
				imgLetter.setAlpha(LETTER_ALPHA_LOW);
				lp.setMargins(i * letterWidth, 0, 0, 0);
				
				
				imgLetter.setOnClickListener(imgClick);
				imgLetter.setTag(Integer.valueOf(i));
				layoutLetters.addView(imgLetter);
				imgViewList.add(imgLetter);
				
			//--
				
			}
		} catch (IOException e) {
			Log.e("baha", "Asset read error for " + letter);
		}

	}
	
	//Fake method
	public View.OnClickListener imgClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {

			if(touchDisabled)
				return;
			
			ImageView imgView = (ImageView) v;
			int idx = (Integer) imgView.getTag();
			
			if(swap1 == null) { //select
				swap1 = imgView;
				imgView.setAlpha(LETTER_ALPHA_HIGH);
			} else if(swap1.equals(imgView)) { //unselect
				swap1 = null;
				imgView.setAlpha(LETTER_ALPHA_LOW);
			} else { //do swap
				imgView.setAlpha(LETTER_ALPHA_HIGH);
				doSwap(swap1, imgView);
			}
		}
	};
	
	public void doSwap(final ImageView imgView1, final ImageView imgView2) {

		
		int top = 0 + letterHeight;
		int bottom = layoutScene.getHeight() - letterHeight;
		
		ImageViewWrapper img1 = new ImageViewWrapper(imgView1);
		ImageViewWrapper img2 = new ImageViewWrapper(imgView2);
		
		ObjectAnimator goTo2Horizontal = ObjectAnimator.ofInt(
				img1, "LeftMargin", 
				img1.layoutParams.leftMargin,
				img2.layoutParams.leftMargin
				);
		
		goTo2Horizontal.setInterpolator(new DecelerateInterpolator());
		
		ObjectAnimator goTo2Vertical = ObjectAnimator.ofInt(
				img2, "TopMargin", 
				img2.layoutParams.topMargin,
				top,
				img2.layoutParams.topMargin);
		
		goTo2Vertical.setInterpolator(new DecelerateInterpolator());
		
		// ------------------
		
		ObjectAnimator goTo1Horizontal = ObjectAnimator.ofInt(
				img2, "LeftMargin", 
				img2.layoutParams.leftMargin,
				img1.layoutParams.leftMargin
				);
		
		goTo2Horizontal.setInterpolator(new DecelerateInterpolator());
		
		ObjectAnimator goTo1Vertical = ObjectAnimator.ofInt(
				img1, "TopMargin", 
				img1.layoutParams.topMargin,
				bottom - img1.layoutParams.topMargin,
				img1.layoutParams.topMargin);
		
		goTo2Vertical.setInterpolator(new DecelerateInterpolator());
		
		
		AnimatorSet set = new AnimatorSet();
		
		set
			.play(goTo1Horizontal)
			//.with(goTo1Vertical)
			.with(goTo2Horizontal);
			//.with(goTo2Vertical);

		set.addListener(new Animator.AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				imgView1.setAlpha(LETTER_ALPHA_LOW);
				imgView2.setAlpha(LETTER_ALPHA_LOW);
				swap1 = null;

			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
		
		set.setDuration(LETTER_ANIMATION_DURATION);
		set.start();
		
		// Swap order
		
		int idx1 = (Integer)imgView1.getTag();
		int idx2 = (Integer)imgView2.getTag();
		imgView1.setTag(Integer.valueOf(idx2));
		imgView2.setTag(Integer.valueOf(idx1));
		
		Collections.swap(currentOrder, idx1, idx2);
		checkOrder();
		
	}

	public void checkOrder() {
		String word = "";
		for(String letter : currentOrder)
			word += letter;
		Log.i("baha",word + ":" + currentWord.word);
		if(word.equalsIgnoreCase(currentWord.word)) {
			Toast.makeText(this.getActivity(), "Congrats!", Toast.LENGTH_LONG).show();
			goNext();
		} else {
			Log.i("baha","Yanlis: " + word);
		}
			
				
	}
	
	
	public List<String> wordToList(String word) {
		List<String> lettersList = new ArrayList<>(word.length());
		
		for(char c : word.toCharArray()) {
			lettersList.add(Character.toString(c).toUpperCase(Locale.ENGLISH));
		}

		return lettersList;
	}
	
	public Bitmap getLetterBitmap(String fileName) throws IOException {

	    InputStream istr = getResources().getAssets().open(fileName);
	    Bitmap bitmap = BitmapFactory.decodeStream(istr);

	    return bitmap;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		
		layoutLetters.setMinimumHeight(layoutScene.getHeight());
		
		
		
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i("baha","optaa");
		switch (item.getItemId()) {
		case R.id.action_solve:
			solve();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void solve() {
		
		
		currentOrder = wordToList(currentWord.word);
		createLetters();
		goNext();
	}
	
	
	private void goNext() {
		touchDisabled = true;

		Handler handle = new Handler();
		handle.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				newGame();
				
			}
		}, 3000);
		
	}
	
	
	
	public void setLettersAlpha(float alpha) {
		
		for(ImageView view : imgViewList) {
			view.setAlpha(alpha);	
		}
		
	}
	
	
}
