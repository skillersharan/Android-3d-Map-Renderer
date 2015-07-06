package com.gl.greyengine.greyengine;

import com.gl.greyengine.greyengine.Shapes.MapWorld;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sharan on 24-Jun-15.
 */
public class MapBuilder {

    public static MapWorld BuildMap(String MapJson) {
        MapWorld mw = new MapWorld();
        try {
            JSONObject jsonObject = new JSONObject(MapJson);
            Logger.d(String.valueOf(jsonObject.getJSONArray("features").getJSONObject(0).getJSONArray("coordinates").getJSONObject(0).getDouble("x")));
            JSONArray Polygons = jsonObject.getJSONArray("features");
            for (int i = 0; i < Polygons.length(); i++) {
                JSONArray Coordinates = Polygons.getJSONObject(i).getJSONArray("coordinates");
                float vertices[] = new float[Coordinates.length() * 2];
                for (int j = 0; j < Coordinates.length(); j++) {
                    vertices[j * 2] = (float) Coordinates.getJSONObject(j).getDouble("x");
                    vertices[j * 2 + 1] = (float) Coordinates.getJSONObject(j).getDouble("y");
                }
                mw.addPolygon(vertices);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mw;
    }

}
