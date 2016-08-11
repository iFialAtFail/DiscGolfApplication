package com.example.michael.discgolfapp.Interfaces;

import com.example.michael.discgolfapp.CustomViews.ObservableHorizontalScrollView;

/**
 * Created by Michael on 8/11/2016.
 */
public interface IHorizontalScrollViewListener {
    void onScrollChanged(ObservableHorizontalScrollView horizontalScrollView, int x, int y, int oldx, int oldy);
}
