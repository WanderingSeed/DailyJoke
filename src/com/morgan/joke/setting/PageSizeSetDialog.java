package com.morgan.joke.setting;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.morgan.joke.R;
import com.morgan.joke.data.Constant;

public class PageSizeSetDialog extends DialogPreference {

    private SeekBar mSeekBar;
    private TextView mCurrentValueView;
    private TextView mSummaryView;

    public PageSizeSetDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
        mCurrentValueView = (TextView) view.findViewById(R.id.current_page_num);
        mSeekBar.setProgress((int) (100 * ((getPersistedInt(Constant.DEFAULT_PAGE_SIZE) - Constant.MIN_PAGE_SIZE) / (double) (Constant.MAX_PAGE_SIZE - Constant.MIN_PAGE_SIZE))));
        mCurrentValueView.setText(String.valueOf(getPersistedInt(Constant.DEFAULT_PAGE_SIZE)));
        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mCurrentValueView.setText(String.valueOf(getValue(mSeekBar.getProgress())));
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
            persistInt(value);
        }
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mSummaryView = (TextView) view.findViewById(android.R.id.summary);
        mSummaryView.setVisibility(View.VISIBLE);
        mSummaryView.setText(String.valueOf(getPersistedInt(Constant.DEFAULT_PAGE_SIZE)));
    }

    private int getValue(int progress) {
        return (int) (progress / 100.0 * (Constant.MAX_PAGE_SIZE - Constant.MIN_PAGE_SIZE) + Constant.MIN_PAGE_SIZE);
    }
}
