package com.example.michael.discgolfapp.Interfaces;

import com.example.michael.discgolfapp.CustomViews.ObservableScrollView;

/**
 * Created by Michael on 8/11/2016.
 */
public interface IScrollViewListener {
    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
}
