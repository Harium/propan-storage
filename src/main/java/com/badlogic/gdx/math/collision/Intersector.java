package com.badlogic.gdx.math.collision;

import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;

public class Intersector {

    static Vector3 tmp1 = new Vector3();
    static Vector3 tmp2 = new Vector3();

    /**
     * Quick check whether the given {@link BoundingBox} and {@link Plane} intersect.
     *
     * @param box   The bounding box
     * @param plane The plane
     * @return Whether the bounding box and the plane intersect.
     */
    public static boolean intersectBoundsPlaneFast(BoundingBox box, Plane plane) {
        return intersectBoundsPlaneFast(box.getCenter(tmp1), box.getDimensions(tmp2).scl(0.5f), plane.normal, plane.d);
    }

    /**
     * Quick check whether the given bounding box and a plane intersect. Code adapted from Christer Ericson's Real Time
     * Collision
     *
     * @param center         The center of the bounding box
     * @param halfDimensions Half of the dimensions (width, height and depth) of the bounding box
     * @param normal         The normal of the plane
     * @param distance       The distance of the plane
     * @return Whether the bounding box and the plane intersect.
     */
    public static boolean intersectBoundsPlaneFast(Vector3 center, Vector3 halfDimensions, Vector3 normal,
        float distance) {
        // Compute the projection interval radius of b onto L(t) = b.c + t * p.n
        float radius = halfDimensions.x * Math.abs(normal.x) +
            halfDimensions.y * Math.abs(normal.y) +
            halfDimensions.z * Math.abs(normal.z);

        // Compute distance of box center from plane
        float s = normal.dot(center) - distance;

        // Intersection occurs when plane distance falls within [-r,+r] interval
        return Math.abs(s) <= radius;
    }

    /**
     * Quick check whether the given {@link BoundingBox} and {@link Triangle} intersect.
     * Code adapted from Christer Ericson's Real Time Collision
     *
     * @param box   The bounding box
     * @param triangle The triangle
     * @return Whether the bounding box and the triangle intersect.
     */
    public static boolean intersectBoundsTriangleFast (BoundingBox box, Triangle triangle) {
        return intersectBoundsTriangleFast(box.getCenter(tmp1), box.getDimensions(tmp2).scl(0.5f), triangle.a, triangle.b, triangle.c);
    }

    /**
     * Quick check whether the given bounding box and a plane intersect. Code adapted from Christer Ericson's Real Time
     * Collision
     *
     * @param center         The center of the bounding box
     * @param halfDimensions Half of the dimensions (width, height and depth) of the bounding box
     * @param a              The triangle point A
     * @param b              The triangle point B
     * @param c              The triangle point C
     * @return Whether the bounding box and the plane intersect.
     */
    public static boolean intersectBoundsTriangleFast (Vector3 center, Vector3 halfDimensions, Vector3 a, Vector3 b, Vector3 c) {
        // Translate triangle as conceptually moving AABB to origin
        Vector3 v0 = new Vector3(a).sub(center);
        Vector3 v1 = new Vector3(b).sub(center);
        Vector3 v2 = new Vector3(c).sub(center);

        // Compute edge vectors for triangle
        Vector3 f0 = new Vector3(b).sub(a);
        Vector3 f1 = new Vector3(c).sub(b);
        Vector3 f2 = new Vector3(a).sub(c);

        // region Test axes a00..a22 (category 3)

        float p0, p1, p2;
        // Test axis a00
        Vector3 a00 = new Vector3(0, -f0.z, f0.y);
        p0 = v0.dot(a00);
        p1 = v1.dot(a00);
        p2 = v2.dot(a00);

        float r = halfDimensions.y * Math.abs(f0.z) + halfDimensions.z * Math.abs(f0.y);
        if ((Math.max(-(Math.max(p0, Math.max(p1, p2))), Math.min(p0, Math.min(p1, p2)))) > r) {
            return false;
        }

        // Test axis a01
        Vector3 a01 = new Vector3(0, -f1.z, f1.y);
        p0 = v0.dot(a01);
        p1 = v1.dot(a01);
        p2 = v2.dot(a01);

        r = halfDimensions.y * Math.abs(f1.z) + halfDimensions.z * Math.abs(f1.y);
        if ((Math.max(-(Math.max(p0, Math.max(p1, p2))), Math.min(p0, Math.min(p1, p2)))) > r) {
            return false;
        }

        // Test axis a02
        Vector3 a02 = new Vector3(0, -f2.z, f2.y);
        p0 = v0.dot(a02);
        p1 = v1.dot(a02);
        p2 = v2.dot(a02);

        r = halfDimensions.y * Math.abs(f2.z) + halfDimensions.z * Math.abs(f2.y);
        if ((Math.max(-(Math.max(p0, Math.max(p1, p2))), Math.min(p0, Math.min(p1, p2)))) > r) {
            return false;
        }

        // Test axis a10
        Vector3 a10 = new Vector3(f0.z, 0, -f0.x);
        p0 = v0.dot(a10);
        p1 = v1.dot(a10);
        p2 = v2.dot(a10);

        r = halfDimensions.x * Math.abs(f0.z) + halfDimensions.z * Math.abs(f0.x);
        if ((Math.max(-(Math.max(p0, Math.max(p1, p2))), Math.min(p0, Math.min(p1, p2)))) > r) {
            return false;
        }

        // Test axis a11
        Vector3 a11 = new Vector3(f1.z, 0, -f1.x);
        p0 = v0.dot(a11);
        p1 = v1.dot(a11);
        p2 = v2.dot(a11);

        r = halfDimensions.x * Math.abs(f1.z) + halfDimensions.z * Math.abs(f1.x);
        if ((Math.max(-(Math.max(p0, Math.max(p1, p2))), Math.min(p0, Math.min(p1, p2)))) > r) {
            return false;
        }

        // Test axis a12
        Vector3 a12 = new Vector3(f2.z, 0, -f2.x);
        p0 = v0.dot(a12);
        p1 = v1.dot(a12);
        p2 = v2.dot(a12);

        r = halfDimensions.x * Math.abs(f2.z) + halfDimensions.z * Math.abs(f2.x);
        if ((Math.max(-(Math.max(p0, Math.max(p1, p2))), Math.min(p0, Math.min(p1, p2)))) > r) {
            return false;
        }

        // Test axis a20
        Vector3 a20 = new Vector3(-f0.y, f0.x, 0);
        p0 = v0.dot(a20);
        p1 = v1.dot(a20);
        p2 = v2.dot(a20);

        r = halfDimensions.x * Math.abs(f0.y) + halfDimensions.y * Math.abs(f0.x);
        if ((Math.max(-(Math.max(p0, Math.max(p1, p2))), Math.min(p0, Math.min(p1, p2)))) > r) {
            return false;
        }

        // Test axis a21
        Vector3 a21 = new Vector3(-f1.y, f1.x, 0);
        p0 = v0.dot(a21);
        p1 = v1.dot(a21);
        p2 = v2.dot(a21);

        r = halfDimensions.x * Math.abs(f1.y) + halfDimensions.y * Math.abs(f1.x);
        if ((Math.max(-(Math.max(p0, Math.max(p1, p2))), Math.min(p0, Math.min(p1, p2)))) > r) {
            return false;
        }

        // Test axis a22
        Vector3 a22 = new Vector3(-f2.y, f2.x, 0);
        p0 = v0.dot(a22);
        p1 = v1.dot(a22);
        p2 = v2.dot(a22);
        r = halfDimensions.x * Math.abs(f2.y) + halfDimensions.y * Math.abs(f2.x);
        if ((Math.max(-(Math.max(p0, Math.max(p1, p2))), Math.min(p0, Math.min(p1, p2)))) > r) {
            return false;
        }

        // Test the three axes corresponding to the face normals of AABB b (category 1)

        // Exit if...
        // ... [-extents.X, extents.X] and [min(v0.X,v1.X,v2.X), max(v0.X,v1.X,v2.X)] do not overlap
        if (Math.max(v0.x, Math.max(v1.x, v2.x)) < -halfDimensions.x || Math.min(v0.x, Math.min(v1.x, v2.x)) > halfDimensions.x) {
            return false;
        }
        // ... [-extents.Y, extents.Y] and [min(v0.Y,v1.Y,v2.Y), max(v0.Y,v1.Y,v2.Y)] do not overlap
        if (Math.max(v0.y, Math.max(v1.y, v2.y)) < -halfDimensions.y || Math.min(v0.y, Math.min(v1.y, v2.y)) > halfDimensions.y) {
            return false;
        }
        // ... [-extents.Z, extents.Z] and [min(v0.Z,v1.Z,v2.Z), max(v0.Z,v1.Z,v2.Z)] do not overlap
        if (Math.max(v0.z, Math.max(v1.z, v2.z)) < -halfDimensions.z || Math.min(v0.z, Math.min(v1.z, v2.z)) > halfDimensions.z) {
            return false;
        }

        // Test separating axis corresponding to triangle face normal (category 2)
        Vector3 normal = f0.crs(f1);
        float d = normal.dot(v0);
        return intersectBoundsPlaneFast(center, halfDimensions, normal, d);
    }
}
