package com.gl.greyengine.greyengine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Sharan on 21-Jun-15.
 */
public class MapView extends GLSurfaceView {
    OpenGLRenderer renderer;

    public MapView(Context context) {
        super(context);
        this.setRenderer(renderer = new OpenGLRenderer());
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setRenderer(renderer = new OpenGLRenderer());
    }

}
