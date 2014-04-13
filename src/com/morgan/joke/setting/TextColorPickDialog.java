package com.morgan.joke.setting;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.morgan.joke.Constant;
import com.morgan.joke.R;
import com.morgan.joke.setting.ColorPickerView.OnColorPickedListener;

public class TextColorPickDialog extends DialogPreference {

    private ColorPickerView mColorPicker;
    private int mColor = Constant.DEFAULT_TEXT_COLOR;
    private TextView mSummaryView;

    public TextColorPickDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mColorPicker = (ColorPickerView) view.findViewById(R.id.colorPickerView);
        mColorPicker.setInitColor(getPersistedInt(Constant.DEFAULT_TEXT_COLOR));
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
        mSummaryView.setTextColor(getPersistedInt(Constant.DEFAULT_TEXT_COLOR));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            persistInt(mColor);
            mSummaryView.setTextColor(mColor);
        }
    }
}
