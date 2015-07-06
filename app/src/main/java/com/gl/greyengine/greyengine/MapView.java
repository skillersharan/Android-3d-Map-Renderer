package com.gl.greyengine.greyengine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.gl.greyengine.greyengine.Shapes.MapWorld;
import com.gl.greyengine.greyengine.Utils.EarClippingTriangulator;
import com.gl.greyengine.greyengine.Utils.ShortArray;
import com.orhanobut.logger.Logger;

/**
 * Created by Sharan on 21-Jun-15.
 */
public class MapView extends GLSurfaceView {
    private final float threshold = 1;
    public String mapJson;
    OpenGLRenderer renderer;
    private float mPreviousX;
    private float mPreviousY;

    public MapView(Context context) {
        super(context);
        mapJson = context.getString(R.string.mapjson);
        mapJson = context.getString(R.string.mapjson);
        MapWorld mw = MapBuilder.BuildMap(mapJson);
        this.setRenderer(renderer = new OpenGLRenderer(mw));
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mapJson = context.getString(R.string.mapjson);
        MapWorld mw = MapBuilder.BuildMap(mapJson);
        mw.setFloorSquare(1615, 772);
        this.setRenderer(renderer = new OpenGLRenderer(mw));
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                renderer.eye.m_x = renderer.eye.m_x + -dx * threshold;
                renderer.center.m_x = renderer.center.m_x + -dx * threshold;

                renderer.eye.m_y = renderer.eye.m_y + dy * threshold;
                renderer.center.m_y = renderer.center.m_y + dy * threshold;
                this.requestRender();
                break;
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

}
