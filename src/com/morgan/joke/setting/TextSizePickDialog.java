package com.morgan.joke.setting;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.morgan.joke.Constant;
import com.morgan.joke.R;

public class TextSizePickDialog extends DialogPreference {

    private SeekBar mSeekBar;
    private TextView mCurrentValueView;
    private TextView mSummaryView ;

    public TextSizePickDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
        mCurrentValueView = (TextView) view.findViewById(R.id.current_text_size);
        mSeekBar.setProgress((int) (100 * ((getPersistedInt(Constant.DEFAULT_TEXT_SIZE) - Constant.MIN_TEXT_SIZE) / (double) (Constant.MAX_TEXT_SIZE - Constant.MIN_TEXT_SIZE))));
        mCurrentValueView.setText(String.valueOf(getPersistedInt(Constant.DEFAULT_TEXT_SIZE)));
        mCurrentValueView.setTextSize(getValue(getPersistedInt(Constant.DEFAULT_TEXT_SIZE)));
        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mCurrentValueView.setText(String.valueOf(getValue(mSeekBar.getProgress())));
                mCurrentValueView.setTextSize(getValue(mSeekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCurrentValueView.setText(String.valueOf(getValue(progress)));
            }
        });
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int value = getValue(mSeekBar.getProgress());
            mSummaryView.setText(String.valueOf(value));
            mSummaryView.setTextSize(value);
            persistInt(value);
        }
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mSummaryView = (TextView) view.findViewById(android.R.id.summary);
        mSummaryView.setVisibility(View.VISIBLE);
        mSummaryView.setTextSize(getPersistedInt(Constant.DEFAULT_TEXT_SIZE));
        mSummaryView.setText(String.valueOf(getPersistedInt(Constant.DEFAULT_TEXT_SIZE)));
        mSummaryView.setMinHeight(65);
        mSummaryView.setGravity(Gravity.TOP);
    }

    private int getValue(int progress) {
        return (int) (progress / 100.0 * (Constant.MAX_TEXT_SIZE - Constant.MIN_TEXT_SIZE) + 12);
    }
}
