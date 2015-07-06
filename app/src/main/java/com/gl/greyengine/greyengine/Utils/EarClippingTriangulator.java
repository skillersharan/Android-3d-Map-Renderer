package com.gl.greyengine.greyengine.Utils;

/**
 * Created by Sharan on 06-Jul-15.
 */

public class EarClippingTriangulator {
    static private final int CONCAVE = -1;
    static private final int TANGENTIAL = 0;
    static private final int CONVEX = 1;

    private final ShortArray indicesArray = new ShortArray();
    private final IntArray vertexTypes = new IntArray();
    private final ShortArray triangles = new ShortArray();
    private short[] indices;
    private float[] vertices;
    private int vertexCount;

    static private boolean areVerticesClockwise(float[] vertices, int offset, int count) {
        if (count <= 2) return false;
        float area = 0, p1x, p1y, p2x, p2y;
        for (int i = offset, n = offset + count - 3; i < n; i += 2) {
            p1x = vertices[i];
            p1y = vertices[i + 1];
            p2x = vertices[i + 2];
            p2y = vertices[i + 3];
            area += p1x * p2y - p2x * p1y;
        }
        p1x = vertices[offset + count - 2];
        p1y = vertices[offset + count - 1];
        p2x = vertices[offset];
        p2y = vertices[offset + 1];
        return area + p1x * p2y - p2x * p1y < 0;
    }

    static private int computeSpannedAreaSign(float p1x, float p1y, float p2x, float p2y, float p3x, float p3y) {
        float area = p1x * (p3y - p2y);
        area += p2x * (p1y - p3y);
        area += p3x * (p2y - p1y);
        return (int) Math.signum(area);
    }

    public ShortArray computeTriangles(FloatArray vertices) {
        return computeTriangles(vertices.items, 0, vertices.size);
    }

    public ShortArray computeTriangles(float[] vertices) {
        return computeTriangles(vertices, 0, vertices.length);
    }

    public ShortArray computeTriangles(float[] vertices, int offset, int count) {
        this.vertices = vertices;
        int vertexCount = this.vertexCount = count / 2;
        int vertexOffset = offset / 2;

        ShortArray indicesArray = this.indicesArray;
        indicesArray.clear();
        indicesArray.ensureCapacity(vertexCount);
        indicesArray.size = vertexCount;
        short[] indices = this.indices = indicesArray.items;
        if (areVerticesClockwise(vertices, offset, count)) {
            for (short i = 0; i < vertexCount; i++)
                indices[i] = (short) (vertexOffset + i);
        } else {
            for (int i = 0, n = vertexCount - 1; i < vertexCount; i++)
                indices[i] = (short) (vertexOffset + n - i); // Reversed.
        }

        IntArray vertexTypes = this.vertexTypes;
        vertexTypes.clear();
        vertexTypes.ensureCapacity(vertexCount);
        for (int i = 0, n = vertexCount; i < n; ++i)
            vertexTypes.add(classifyVertex(i));

        // A polygon with n vertices has a triangulation of n-2 triangles.
        ShortArray triangles = this.triangles;
        triangles.clear();
        triangles.ensureCapacity(Math.max(0, vertexCount - 2) * 3);
        triangulate();
        return triangles;
    }

    private void triangulate() {
        int[] vertexTypes = this.vertexTypes.items;

        while (vertexCount > 3) {
            int earTipIndex = findEarTip();
            cutEarTip(earTipIndex);

            // The type of the two vertices adjacent to the clipped vertex may have changed.
            int previousIndex = previousIndex(earTipIndex);
            int nextIndex = earTipIndex == vertexCount ? 0 : earTipIndex;
            vertexTypes[previousIndex] = classifyVertex(previousIndex);
            vertexTypes[nextIndex] = classifyVertex(nextIndex);
        }

        if (vertexCount == 3) {
            ShortArray triangles = this.triangles;
            short[] indices = this.indices;
            triangles.add(indices[0]);
            triangles.add(indices[1]);
            triangles.add(indices[2]);
        }
    }

    private int classifyVertex(int index) {
        short[] indices = this.indices;
        int previous = indices[previousIndex(index)] * 2;
        int current = indices[index] * 2;
        int next = indices[nextIndex(index)] * 2;
        float[] vertices = this.vertices;
        return computeSpannedAreaSign(vertices[previous], vertices[previous + 1], vertices[current], vertices[current + 1],
                vertices[next], vertices[next + 1]);
    }

    private int findEarTip() {
        int vertexCount = this.vertexCount;
        for (int i = 0; i < vertexCount; i++)
            if (isEarTip(i)) return i;

        int[] vertexTypes = this.vertexTypes.items;
        for (int i = 0; i < vertexCount; i++)
            if (vertexTypes[i] != CONCAVE) return i;
        return 0; // If all vertices are concave, just return the first one.
    }

    private boolean isEarTip(int earTipIndex) {
        int[] vertexTypes = this.vertexTypes.items;
        if (vertexTypes[earTipIndex] == CONCAVE) return false;

        int previousIndex = previousIndex(earTipIndex);
        int nextIndex = nextIndex(earTipIndex);
        short[] indices = this.indices;
        int p1 = indices[previousIndex] * 2;
        int p2 = indices[earTipIndex] * 2;
        int p3 = indices[nextIndex] * 2;
        float[] vertices = this.vertices;
        float p1x = vertices[p1], p1y = vertices[p1 + 1];
        float p2x = vertices[p2], p2y = vertices[p2 + 1];
        float p3x = vertices[p3], p3y = vertices[p3 + 1];

        // Check if any point is inside the triangle formed by previous, current and next vertices.
        // Only consider vertices that are not part of this triangle, or else we'll always find one inside.
        for (int i = nextIndex(nextIndex); i != previousIndex; i = nextIndex(i)) {
            // Concave vertices can obviously be inside the candidate ear, but so can tangential vertices
            // if they coincide with one of the triangle's vertices.
            if (vertexTypes[i] != CONVEX) {
                int v = indices[i] * 2;
                float vx = vertices[v];
                float vy = vertices[v + 1];
                // Because the polygon has clockwise winding order, the area sign will be positive if the point is strictly inside.
                // It will be 0 on the edge, which we want to include as well.
                // note: check the edge defined by p1->p3 first since this fails _far_ more then the other 2 checks.
                if (computeSpannedAreaSign(p3x, p3y, p1x, p1y, vx, vy) >= 0) {
                    if (computeSpannedAreaSign(p1x, p1y, p2x, p2y, vx, vy) >= 0) {
                        if (computeSpannedAreaSign(p2x, p2y, p3x, p3y, vx, vy) >= 0) return false;
                    }
                }
            }
        }
        return true;
    }

    private void cutEarTip(int earTipIndex) {
        short[] indices = this.indices;
        ShortArray triangles = this.triangles;

        triangles.add(indices[previousIndex(earTipIndex)]);
        triangles.add(indices[earTipIndex]);
        triangles.add(indices[nextIndex(earTipIndex)]);

        indicesArray.removeIndex(earTipIndex);
        vertexTypes.removeIndex(earTipIndex);
        vertexCount--;
    }

    private int previousIndex(int index) {
        return (index == 0 ? vertexCount : index) - 1;
    }

    private int nextIndex(int index) {
        return (index + 1) % vertexCount;
    }
}