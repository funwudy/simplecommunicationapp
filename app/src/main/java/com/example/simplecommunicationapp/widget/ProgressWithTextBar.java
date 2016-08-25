package com.example.simplecommunicationapp.widget;

import java.text.DecimalFormat;

import com.example.simplecommunicationapp.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

//import com.arcsoft.configuration.NotifyCallBack;
//import com.arcsoft.selfiecorrection.R;

public class ProgressWithTextBar extends RelativeLayout {

	private int  mMaxValue = 100;
	private int  mMinValue = 0;
	private int  mValidValueSection;
	private int  mCurrentValue;
	private int  mThisViewID;
		
	//private NotifyCallBack mNotifyCallback;
	
	private SeekBar  mSeekBar;
	private TextView mMinTextView=null;
	private TextView mMaxTextView=null;
	private TextView mCurTextView=null;
	private Context  mContext;
	
	private static final int PROGRESS_BAR_ID_BASE = 0X200;
	
	private int mDivideValue =1;
	private String mSuffixStr=null;
	private float mTextSize = -1;
	private DecimalFormat mDecimalFormat=null;
	private boolean mIsZoombar= false;
	private boolean mIsTouching = false;
	
	
	public ProgressWithTextBar(Context context) {
		this(context, null);
	}

	
	public ProgressWithTextBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		
		init();
	}
		
	
	private void init(){
		removeAllViews();
		LayoutParams lp = null;
//		mMinTextView = new TextView(mContext);
//		mMinTextView.setId(PROGRESS_BAR_ID_BASE + 1);
//		mMinTextView.setText(String.valueOf(mMinValue));
//		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		lp.addRule(ALIGN_PARENT_LEFT);
//		lp.addRule(CENTER_VERTICAL);
//		addView(mMinTextView, lp);
//		
//		mMaxTextView = new TextView(mContext);
//		mMaxTextView.setId(PROGRESS_BAR_ID_BASE + 2);
//		mMaxTextView.setText(String.valueOf(mMaxValue));
		mCurTextView = new TextView(mContext);
		mCurTextView.setId(PROGRESS_BAR_ID_BASE + 3);
		mCurTextView.setText(String.valueOf(mCurrentValue));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(ALIGN_PARENT_RIGHT);
		lp.addRule(CENTER_VERTICAL);
		lp.topMargin = 20;
		lp.bottomMargin = 20;
		addView(mCurTextView, lp);

		mSeekBar = new SeekBar(mContext);
		
		mSeekBar.setMinimumHeight(4);
		Drawable progressDrable = getResources().getDrawable(R.drawable.seekbar_progress);
		progressDrable.setBounds(mSeekBar.getProgressDrawable().getBounds());
        mSeekBar.setProgressDrawable(progressDrable);
        
        Drawable thumbDrable =getResources().getDrawable(R.drawable.seekbar_thumb);  
        mSeekBar.setThumb(thumbDrable);
        mSeekBar.setThumbOffset(20);
        
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		lp.addRule(RIGHT_OF, PROGRESS_BAR_ID_BASE + 1);
		lp.addRule(LEFT_OF, PROGRESS_BAR_ID_BASE + 3);
		lp.addRule(CENTER_VERTICAL);
		lp.leftMargin = 20;
		lp.rightMargin = 20;
		addView(mSeekBar, lp);

		validValueSection();
		mSeekBar.setMax(mValidValueSection);
		mSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
		
		mThisViewID = getId();
	}
	
	private void validValueSection(){
		mValidValueSection = mMaxValue - mMinValue;
		mSeekBar.setMax(mValidValueSection);
	}
	
	private void reckonCurrentValue(int progress){
		mCurrentValue = progress + mMinValue;
		
		if(mDivideValue != 1){
			double value = (double) (mCurrentValue/(mDivideValue*1.0));
			if(mIsZoombar &&mCurrentValue <=10)
				value +=1.0;
			
			String str=null;
			if(mDecimalFormat!= null)
				str =mDecimalFormat.format(value);
			else
				str = String.valueOf(value);
			
			if(mSuffixStr!=null)
				str+=mSuffixStr;
			
			mCurTextView.setText(str);
		}else
			mCurTextView.setText(String.valueOf(mCurrentValue));
		
		if(mTextSize > 0)
			mCurTextView.setTextSize(mTextSize);
	}
	
	public void setRange(int min,int max){
		mMaxValue = max;
		mMinValue = min;
		
		if(mMinTextView != null)
			mMinTextView.setText(String.valueOf(mMinValue));
		
		if(mMaxTextView!= null)
			mMaxTextView.setText(String.valueOf(mMaxValue));
		
		validValueSection();
		
	}
	
	public synchronized int getCurrentValue(){
		return mCurrentValue;
	}
	
	//nValue is realvalue*mDivideValue
	public synchronized void setProgress(int nValue) {
		// TODO Auto-generated method stub	
		int progress = nValue-mMinValue;
		mSeekBar.setProgress(progress);
		reckonCurrentValue(progress);
	}
	
	public synchronized void setProgress(int nValue,boolean needProcess) {
		// TODO Auto-generated method stub	
		int progress = nValue-mMinValue;
		mSeekBar.setProgress(progress);
		reckonCurrentValue(progress);
	}
	
	private OnSeekBarChangeListener mOnSeekBarChangeListener = new OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			mIsTouching = false;
			//do process when stop touching
//			if(mNotifyCallback != null){
//				if(mIsZoombar)
//					mNotifyCallback.onNotify(mThisViewID, mCurrentValue,true);
//				else{
//					double value = (double) (mCurrentValue/(mDivideValue*1.0));
//					mNotifyCallback.onNotify(mThisViewID, value,true);
//				}					
//			}
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			mIsTouching = true;
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			
			reckonCurrentValue(progress);
			//do not process during touching
			if(!mIsTouching){
				
//				if(mNotifyCallback != null){
//					if(mIsZoombar)
//						mNotifyCallback.onNotify(mThisViewID, mCurrentValue,fromUser);
//					else{
//						double value = (double) (mCurrentValue/(mDivideValue*1.0));
//						mNotifyCallback.onNotify(mThisViewID, value,fromUser);
//					}					
//				}
			}
		}
	};
	
	
//	public void setNotifyCallBack(NotifyCallBack callBack){
//		mNotifyCallback = callBack;
//	}
	
	@Override
	public void setId(int id) {
		// TODO Auto-generated method stub
		super.setId(id);
		mThisViewID = id;
	}

	private boolean mEnableClick;
//	@Override
//	public void enableClick(boolean en) {
//		// TODO Auto-generated method stub
//		if (mSeekBar != null) {
//			mSeekBar.setEnabled(en);
//		}
//		mEnableClick = en;
//	}
	
	public void setTxtRotation(float nDegree){
		if(mCurTextView!= null)
			mCurTextView.setRotation(nDegree);		
	}
	

	public void setDisplayDivideValue(int nValue,int nDecimalCount){
		mDivideValue = nValue;
		if(nDecimalCount>0){
			String str="##0.";
			
			for(int i=0; i<nDecimalCount;i++){
				str+="0";
			}
			
			mDecimalFormat = null;
			mDecimalFormat = new DecimalFormat(str);
		}
		else
			mDecimalFormat = null;
	}
	
	public void setDisplaySuffixString(String suffix){
		mSuffixStr = suffix;
	}
	public void setZoomBarFlag(){
		mIsZoombar = true;
	}
	
	public void setDisplayTextSize(float size){
		mTextSize = size;
	}
	
}
