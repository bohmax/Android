package com.example.custom_layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class EqLayout extends ViewGroup {

    public EqLayout(Context context) {
        super(context);
    }

    public EqLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EqLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int lato = getLato();
        Log.d("Conta", left + " " + top + " " + right + " " + bottom);
        int w = (right-left)/lato;
        int h = (bottom-top)/lato;
        for (int i = 0; i < this.getChildCount(); i++) {
            View v = getChildAt(i);
            int x = i % lato, y = i / lato;
            v.layout(x * w, y * h, (x + 1) * w, (y + 1) * h);
        }
    }

    private int getLato() {
        return (int)Math.ceil(Math.sqrt(getChildCount()));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int lato = getLato();
        int w = getMeasuredWidth() / lato;
        int h = getMeasuredHeight() / lato;
        int ws = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
        int hs = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
        for (int i = 0; i < this.getChildCount(); i++) {
            View v = getChildAt(i);
            v.measure(ws, hs);
        }
    }
}
