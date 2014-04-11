package com.morgan.joke;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.morgan.joke.ColorPickerView.OnColorPickedListener;

public class BgColorPickDialog extends DialogPreference {

    public static final int DEFAULT_COLOR = 0Xff000000;
    private ColorPickerView mColorPicker;
    private int mColor = DEFAULT_COLOR;
    private TextView mSummaryView;

    public BgColorPickDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mColorPicker = (ColorPickerView) view.findViewById(R.id.colorPickerView);
        mColorPicker.setInitColor(getPersistedInt(DEFAULT_COLOR));
        mColorPicker.setListener(new OnColorPickedListener() {

            @Override
            public void onColorChange(int color) {
                mColor = color;
            }
        });
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mSummaryView = (TextView) view.findViewById(android.R.id.summary);
        mSummaryView.setVisibility(View.VISIBLE);
        mSummaryView.setWidth(40);
        mSummaryView.setHeight(40);
        mSummaryView.setBackgroundColor(getPersistedInt(DEFAULT_COLOR));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            persistInt(mColor);
            mSummaryView.setBackgroundColor(mColor);
        }
    }
}
