package com.morgan.joke.setting;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.morgan.joke.R;

/**
 * 自定义的显示关于的弹窗
 * 
 * @author Morgan.Ji
 * 
 */
public class AboutDialog extends Dialog {

    public AboutDialog(Context context) {
        super(context, R.style.Theme_dialog);
        setContentView(R.layout.dialog_about);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = display.getWidth() - 60;
        getWindow().setAttributes(params);
        setCanceledOnTouchOutside(true);
        findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
