package com.example.simplecommunicationapp.widget;

import com.example.simplecommunicationapp.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
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
		leftText.setText("0");
		rightText = (TextView) findViewById(R.id.right_text);
		rightText.setText(String.valueOf(seekBar.getMax()));
		curText = (TextView) findViewById(R.id.cur_text);
		curText.setText(String.valueOf(seekBar.getProgress()));
		curText.layout(0, 0, 30, 30);

		seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

	}

	private OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			int spaceWidth = getWidth() - curText.getWidth();
			moveStep = (double) spaceWidth / seekBar.getMax();
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			int offset = (int) (progress * moveStep);
			curText.setText(String.valueOf(progress));
			curText.layout(offset, curText.getTop(), curText.getWidth() + offset, curText.getBottom());
		}
	};

}
