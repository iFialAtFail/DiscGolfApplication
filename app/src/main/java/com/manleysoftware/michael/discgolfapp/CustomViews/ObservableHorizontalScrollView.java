package com.manleysoftware.michael.discgolfapp.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import com.manleysoftware.michael.discgolfapp.Interfaces.IHorizontalScrollViewListener;

/**
 * Created by Michael on 8/11/2016.
 */
public class ObservableHorizontalScrollView extends HorizontalScrollView {

    private IHorizontalScrollViewListener hScrollView;

    public ObservableHorizontalScrollView(Context context) {
        super(context);
    }

    public ObservableHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHorizontalScrollViewListener(IHorizontalScrollViewListener hScrollViewListener){
        this.hScrollView = hScrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (hScrollView != null){
            hScrollView.onScrollChanged(this, l,t, oldl,oldt);
        }
    }
}
