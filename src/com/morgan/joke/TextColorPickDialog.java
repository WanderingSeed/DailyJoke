package com.morgan.joke;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.morgan.joke.ColorPickerView.OnColorPickedListener;

public class TextColorPickDialog extends DialogPreference {

    public static final int DEFAULT_COLOR = 0Xff000000;
    private ColorPickerView mColorPicker;
    private int mColor = DEFAULT_COLOR;
    private TextView mSummaryView;

    public TextColorPickDialog(Context context, AttributeSet attrs) {
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
        mSummaryView.setTextColor(getPersistedInt(DEFAULT_COLOR));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            persistInt(mColor);
            mSummaryView.setTextColor(mColor);
        }
    }
}
