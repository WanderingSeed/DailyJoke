package com.morgan.joke.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * 提供图片操作相关的实用方法。
 * 
 * @author Morgan.Ji
 * 
 */
public class ImageUtils {

    /**
     * 图片转圆形
     * 
     * @param bit
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap, float roundPx) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float left, top, right, bottom;
        top = 0;
        left = 0;
        bottom = height;
        right = width;
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final RectF rectF = new RectF(src);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, src, paint);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return output;
    }

    /**
     * 颜色转圆角矩形图片
     * 
     * @param bit
     * @return
     */
    public static Bitmap toRoundBitmap(int color, int roundPx) {
        int width = 400;
        int height = 400;
        float left, top, right, bottom;
        top = 0;
        left = 0;
        bottom = height;
        right = width;
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final RectF rectF = new RectF(src);
        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        return output;
    }
}
