package com.manleysoftware.michael.discgolfapp.ui.customViews.Interfaces;

import com.manleysoftware.michael.discgolfapp.ui.customViews.ObservableScrollView;

/**
 * Created by Michael on 8/11/2016.
 */
public interface IScrollViewListener {
    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
}
