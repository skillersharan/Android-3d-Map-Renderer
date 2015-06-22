package com.gl.greyengine.greyengine;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Sharan on 21-Jun-15.
 */
public class MapWorld {
    List<Poly3d> buildings = new ArrayList<>();
    Plane floor = new Plane();

    public void addPolygon(float[] vertices) {
        Poly3d p = new Poly3d();
        p.addVertices(vertices);
        buildings.add(p);
    }

    public void setFloor(float[] vertices) {
        floor.setCorners(vertices);
    }

    public void setFloor(float[] vertices, float[] color) {
        floor.setCorners(vertices);
        floor.setColor(color[0], color[1], color[2], color[3]);
    }

    public void addPolygon(Poly3d p) {
        buildings.add(p);
    }

    public void render(GL10 gl) {
        floor.draw(gl);
        for (int i = 0; i < buildings.size(); i++) {
            buildings.get(i).draw(gl);
        }

    }
}
