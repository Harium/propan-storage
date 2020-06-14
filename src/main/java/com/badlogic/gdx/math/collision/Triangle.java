package com.badlogic.gdx.math.collision;

import com.badlogic.gdx.math.Vector3;
import java.io.Serializable;

public class Triangle implements Serializable {

    public final Vector3 a = new Vector3();
    public final Vector3 b = new Vector3();
    public final Vector3 c = new Vector3();

    public final Vector3 normal = new Vector3();

    /**
     * Constructs a new triangle given three points. The normal is calculated via a cross product between
     * (point1-point2)x(point2-point3)
     *
     * @param a The first point
     * @param b The second point
     * @param c The third point
     */
    public Triangle (Vector3 a, Vector3 b, Vector3 c) {
        this.set(a, b, c);
    }

    public Triangle set (Vector3 a, Vector3 b, Vector3 c) {
        this.a.set(a);
        this.b.set(b);
        this.c.set(c);

        this.normal.set(a).sub(b).crs(b.x - c.x, b.y - c.y, b.z - c.z).nor();
        return this;
    }

}
