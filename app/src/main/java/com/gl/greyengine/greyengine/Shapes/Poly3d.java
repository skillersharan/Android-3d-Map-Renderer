package com.gl.greyengine.greyengine.Shapes;

import com.gl.greyengine.greyengine.Utils.EarClippingTriangulator;
import com.gl.greyengine.greyengine.Utils.ShortArray;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Sharan on 19-Jun-15.
 */
public class Poly3d {

    public float height = 1.0f;

    //vertices for the polygon in 2d
    public float polyvertices[] = {
            0, 0,
            1, 0,
            0, 1
    };
    public int numofpolyvertices = 3;

    public float borderwidth = 1f;

    public float colorfaces[] = {
            233f / 255f, 230f / 255f, 226f / 255f, 0.7f
    };
    public float colortop[] = {
            250f / 255f, 246f / 255f, 239f / 255f, 0.7f
    };
    public float colorline[] = {
            225f / 255f, 221f / 255f, 210 / 255f, 1f
    };

    //Internal rendering variables
    private FloatBuffer mVertexBuffer;
    private ShortBuffer mBorderIndexBuffer;
    private ShortBuffer mIndexBuffer;
    private ShortBuffer mTopIndexBuffer;

    private short sideindices[];
    private short borderIndices[];
    private ShortArray topindices;

    private float vertices[];

    public Poly3d() {
        calctriangles();
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void initBuffers() {
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(sideindices.length * 4).order(ByteOrder.nativeOrder()).asShortBuffer();
        mIndexBuffer.put(sideindices);
        mIndexBuffer.position(0);

        mBorderIndexBuffer = ByteBuffer.allocateDirect(borderIndices.length * 4).order(ByteOrder.nativeOrder()).asShortBuffer();
        mBorderIndexBuffer.put(borderIndices);
        mBorderIndexBuffer.position(0);

        EarClippingTriangulator earClippingTriangulator = new EarClippingTriangulator();
        topindices = earClippingTriangulator.computeTriangles(polyvertices);
        mTopIndexBuffer = ByteBuffer.allocateDirect(topindices.size * 4).order(ByteOrder.nativeOrder()).asShortBuffer();
        mTopIndexBuffer.put(topindices.toArray());
        mTopIndexBuffer.position(0);
    }

    public void calctriangles() {
        vertices = new float[numofpolyvertices * 3 * 2];
        borderIndices = new short[numofpolyvertices * 2];
        sideindices = new short[numofpolyvertices * 3 * 2];
        for (int i = 0; i < numofpolyvertices; i++) {
            vertices[i * 3] = polyvertices[i * 2];
            vertices[i * 3 + 1] = polyvertices[i * 2 + 1];
            vertices[i * 3 + 2] = 0;
            vertices[i * 3 + (numofpolyvertices * 3)] = polyvertices[i * 2];
            vertices[i * 3 + 1 + (numofpolyvertices * 3)] = polyvertices[i * 2 + 1];
            vertices[i * 3 + 2 + (numofpolyvertices * 3)] = height;

            sideindices[i * 6] = (short) i;
            sideindices[i * 6 + 1] = (short) ((i + 1) % numofpolyvertices);
            sideindices[i * 6 + 2] = (short) (numofpolyvertices + i);
            sideindices[i * 6 + 3] = (short) (numofpolyvertices + i);
            sideindices[i * 6 + 4] = (short) (numofpolyvertices + (i + 1) % numofpolyvertices);
            sideindices[i * 6 + 5] = (short) ((i + 1) % numofpolyvertices);

            borderIndices[i * 2] = (short) i;
            borderIndices[i * 2 + 1] = (short) (numofpolyvertices + i);
        }

        initBuffers();
    }

    public void addVertices(float[] ver) {
        polyvertices = ver;
        numofpolyvertices = polyvertices.length / 2;
        calctriangles();
    }

    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CW);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glColor4f(colorline[0], colorline[1], colorline[2], 1.0f);
        gl.glLineWidth(borderwidth);

        gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, numofpolyvertices);
        gl.glDrawArrays(GL10.GL_LINE_LOOP, numofpolyvertices, numofpolyvertices);

        gl.glDrawElements(GL10.GL_LINES, borderIndices.length, GL10.GL_UNSIGNED_SHORT, mBorderIndexBuffer);

        gl.glColor4f(colorfaces[0], colorfaces[1], colorfaces[2], colorfaces[3]);
        gl.glDrawElements(GL10.GL_TRIANGLES, sideindices.length, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);

        gl.glColor4f(colortop[0], colortop[1], colortop[2], colortop[3]);
        gl.glDrawElements(GL10.GL_TRIANGLES, topindices.size, GL10.GL_UNSIGNED_SHORT, mTopIndexBuffer);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

    public void setPolyvertices(float[] polyvertices) {
        this.polyvertices = polyvertices;
    }

    public void setColorfaces(float[] colorfaces) {
        this.colorfaces = colorfaces;
    }

    public void setColortop(float[] colortop) {
        this.colortop = colortop;
    }

    public void setColorline(float[] colorline) {
        this.colorline = colorline;
    }

    public void setBorderwidth(float borderwidth) {
        this.borderwidth = borderwidth;
    }
}
