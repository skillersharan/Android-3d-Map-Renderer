package com.gl.greyengine.greyengine.Shapes;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Sharan on 21-Jun-15.
 */
public class MapWorld {
    List<Poly3d> shops = new ArrayList<>();
    Plane floor = new Plane();

    public void addPolygon(float[] vertices) {
        Poly3d p = new Poly3d();
        p.addVertices(vertices);
        shops.add(p);
    }

    public void setFloor(float[] vertices) {
        floor.setCorners(vertices);
    }

    public void setFloorSquare(float x, float y) {
        float[] vertices = new float[]{0, 0, x, 0, x, y, 0, y};
        floor.setCorners(vertices);
    }

    public void setFloorSquare(float x, float y, float color[]) {
        float[] vertices = new float[]{0, 0, x, 0, x, y, 0, y};
        floor.setCorners(vertices, color);
    }

    public void setFloor(float[] vertices, float[] color) {
        floor.setCorners(vertices, color);
    }

    public void addPolygon(Poly3d p) {
        shops.add(p);
    }

    public void render(GL10 gl) {
        floor.draw(gl);
        for (int i = 0; i < shops.size(); i++) {
            shops.get(i).draw(gl);
        }
    }
}
