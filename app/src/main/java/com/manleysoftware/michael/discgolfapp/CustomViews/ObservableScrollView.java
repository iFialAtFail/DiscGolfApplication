package com.manleysoftware.michael.discgolfapp.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.manleysoftware.michael.discgolfapp.CustomViews.Interfaces.IScrollViewListener;

/**
 * Created by Michael on 8/11/2016.
 */
public class ObservableScrollView extends ScrollView{

    private IScrollViewListener scrollViewListener;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollViewListener(IScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null){
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }
}
