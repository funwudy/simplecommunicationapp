package com.example.simplecommunicationapp.widget;

import com.example.simplecommunicationapp.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MySeekBar extends LinearLayout {

	private SeekBar seekBar;
	private TextView leftText;
	private TextView rightText;
	private TextView curText;
	
	private TextMoveLayout mTextMoveLayout;

	double moveStep;

	public MySeekBar(Context context) {
		this(context, null);
	}

	public MySeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.my_seek_bar, this);
		seekBar = (SeekBar) findViewById(R.id.seek_bar);
		leftText = (TextView) findViewById(R.id.left_text);
		rightText = (TextView) findViewById(R.id.right_text);
		curText = new TextView(context);
		mTextMoveLayout = (TextMoveLayout) findViewById(R.id.text_move_layout);
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(200, 50);
		mTextMoveLayout.addView(curText, layoutParams);
		curText.setText("hehe");
		curText.layout(0, 20, 200, 80);

		seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
	}

	private OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			// TODO Auto-generated method stub
			int layoutWidth = getWidth();
			moveStep = (double) layoutWidth / seekBar.getMax();
			rightText.setText(String.valueOf(layoutWidth));

			int offset = (int) (progress * moveStep);
			leftText.setText(String.valueOf(offset));
			curText.setText(String.valueOf(progress));
			curText.layout(offset, curText.getTop(), curText.getWidth() + offset, curText.getBottom());
			//curText.layout(getLeft() + offset, getTop(), getRight() + offset, getBottom());
		}
	};

}
