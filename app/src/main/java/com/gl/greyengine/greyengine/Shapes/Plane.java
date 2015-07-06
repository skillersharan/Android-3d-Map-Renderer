package com.gl.greyengine.greyengine.Shapes;

import com.orhanobut.logger.Logger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Sharan on 21-Jun-15.
 */
public class Plane {
    public FloatBuffer mVertexBuffer;
    public float corners[] = {

    };
    public int numberofcorners = 0;
    public float color[] = {
            240f / 255f, 240f / 255f, 240f / 255f, 1f
    };
    public float[] colorborder = {225f / 255f, 221f / 255f, 210 / 255f, 1f};

    public Plane() {
        initBuffers();
    }

    public void setCorners(float[] corners) {
        this.corners = corners;
        numberofcorners = corners.length / 2;
        initBuffers();
    }

    public void setCorners(float[] corners, float col[]) {
        this.corners = corners;
        numberofcorners = corners.length / 2;
        this.color = col;
        initBuffers();
    }

    public void setColor(float red, float green, float blue, float alpha) {
        this.color[0] = red;
        this.color[1] = green;
        this.color[2] = blue;
        this.color[3] = alpha;
    }

    public void initBuffers() {
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(corners.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(corners);
        mVertexBuffer.position(0);
    }


    public void draw(GL10 gl) {
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, mVertexBuffer);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glColor4f(color[0], color[1], color[2], color[3]);

        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, numberofcorners);

        gl.glColor4f(colorborder[0], colorborder[1], colorborder[2], colorborder[3]);
        gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, numberofcorners);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }
}
