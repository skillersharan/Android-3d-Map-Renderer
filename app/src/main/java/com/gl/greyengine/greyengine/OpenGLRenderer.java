package com.gl.greyengine.greyengine;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import com.gl.greyengine.greyengine.Math.Vector3f;
import com.gl.greyengine.greyengine.Shapes.MapWorld;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Sharan on 19-Jun-15.
 */
public class OpenGLRenderer implements GLSurfaceView.Renderer {
    MapWorld mw = new MapWorld();

    Vector3f eye = new Vector3f(0, 0, 1000);
    Vector3f up = new Vector3f(0, 1, 0);
    Vector3f center = new Vector3f(0, 0, 0);

    private float nearclipping = 0.1f;
    private float farclipping = 5000f;

    public OpenGLRenderer(MapWorld mw) {
        this.mw = mw;
    }

    public void setMapWorld(MapWorld map) {
        mw = map;
    }



    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glDisable(GL10.GL_DITHER);

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        GLU.gluLookAt(gl, eye.m_x, eye.m_y, eye.m_z, center.m_x, center.m_y, center.m_z, up.m_x, up.m_y, up.m_z);

        mw.render(gl);
        gl.glLoadIdentity();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, nearclipping, farclipping);
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
}
