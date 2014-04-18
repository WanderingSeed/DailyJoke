package com.morgan.joke.setting;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.morgan.joke.R;
import com.morgan.joke.data.Constant;
import com.morgan.joke.setting.ColorPickerView.OnColorPickedListener;

public class BgColorPickDialog extends DialogPreference {

    private ColorPickerView mColorPicker;
    private int mColor = Constant.DEFAULT_BACKGROUND_COLOR;
    private TextView mSummaryView;

    public BgColorPickDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mColorPicker = (ColorPickerView) view.findViewById(R.id.colorPickerView);
        mColorPicker.setInitColor(getPersistedInt(Constant.DEFAULT_BACKGROUND_COLOR));
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
        mSummaryView.setBackgroundColor(getPersistedInt(Constant.DEFAULT_BACKGROUND_COLOR));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            persistInt(mColor);
            mSummaryView.setBackgroundColor(mColor);
        }
    }
}
