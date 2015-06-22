package com.gl.greyengine.greyengine;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Sharan on 19-Jun-15.
 */
public class OpenGLRenderer implements GLSurfaceView.Renderer {
    MapWorld mw = new MapWorld();
    private Poly3d mCube1 = new Poly3d();
    private Poly3d mCube2 = new Poly3d();
    private Plane mPlane1 = new Plane();
    private float mRotation;

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

        mw.addPolygon(new float[]{3, 3, 4, 3, 4, 4, 3, 4});
        mw.addPolygon(new float[]{0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f});
        mw.addPolygon(new float[]{-3.0f, -3.0f, -4.0f, -3.0f, -4.0f, -4.0f, -3.0f, -4.0f});
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        gl.glTranslatef(0.0f, 0.0f, -20.0f);
        gl.glRotatef(-45, 1, 0, 0);
        gl.glRotatef(mRotation, 0.0f, 0.0f, 1.0f);

        mRotation = (mRotation + 0.55f) % 360;

        mw.render(gl);
        gl.glLoadIdentity();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 500.0f);
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
}
