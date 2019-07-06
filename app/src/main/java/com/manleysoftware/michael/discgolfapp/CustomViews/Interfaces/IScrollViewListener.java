package com.manleysoftware.michael.discgolfapp.CustomViews.Interfaces;

import com.manleysoftware.michael.discgolfapp.CustomViews.ObservableScrollView;

/**
 * Created by Michael on 8/11/2016.
 */
public interface IScrollViewListener {
    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
}
