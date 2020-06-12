package com.harium.propan.storage.octree;

import static com.harium.propan.storage.octree.Octree.BottomLeftBack;
import static com.harium.propan.storage.octree.Octree.BottomLeftFront;
import static com.harium.propan.storage.octree.Octree.BottomRightBack;
import static com.harium.propan.storage.octree.Octree.BottomRightFront;
import static com.harium.propan.storage.octree.Octree.TopLeftBack;
import static com.harium.propan.storage.octree.Octree.TopLeftFront;
import static com.harium.propan.storage.octree.Octree.TopRightBack;
import static com.harium.propan.storage.octree.Octree.TopRightFront;

import com.badlogic.gdx.math.Vector3;
import java.util.ArrayList;
import java.util.List;

public class OctreeNode {

    private int depth = 0;

    // Represent the boundary of the BoundingBox
    protected Vector3 max, min;

    protected OctreeNode[] children;
    private List<Object> geometries = null;// TODO CHANGE TO CONSTANT

    public OctreeNode(Vector3 min, Vector3 max) {
        this(min.x, min.y, min.z, max.x, max.y, max.z);
    }

    public OctreeNode(float x1, float y1, float z1, float x2, float y2, float z2) {
        min = new Vector3(x1, y1, z1);
        max = new Vector3(x2, y2, z2);
    }

    protected void initChildren() {
        children = new OctreeNode[8];

        float midx = (max.x + min.x) / 2;
        float midy = (max.y + min.y) / 2;
        float midz = (max.z + min.z) / 2;

        children[TopLeftFront] = new OctreeNode(
            min.x, midy, midz,
            midx, max.y, max.z)
            .depth(depth + 1);

        children[TopRightFront] = new OctreeNode(
            midx, midy, midz,
            max.x, max.y, max.z)
            .depth(depth + 1);

        children[TopRightBack] = new OctreeNode(
            midx, midy, min.z,
            max.x, max.y, midz)
            .depth(depth + 1);

        children[TopLeftBack] = new OctreeNode(
            min.x, midy, min.z,
            midx, max.y, midz)
            .depth(depth + 1);

        children[BottomLeftFront] = new OctreeNode(
            min.x, min.y, midz,
            midx, midy, max.z)
            .depth(depth + 1);

        children[BottomRightFront] = new OctreeNode(
            midx, min.y, midz,
            max.x, midy, max.z)
            .depth(depth + 1);

        children[BottomRightBack] = new OctreeNode(
            midx, min.y, min.z,
            max.x, midy, midz)
            .depth(depth + 1);

        children[BottomLeftBack] = new OctreeNode(
            min.x, min.y, min.z,
            midx, midy, midz)
            .depth(depth + 1);
    }

    private OctreeNode depth(int depth) {
        this.depth = depth;
        return this;
    }

    // Function to insert a point in the octree
    public boolean insert(Vector3 p, int maxDepth) {
        // If the point is out of bounds
        if (!contains(p)) {
            return false;
        }

        //If is not Leaf
        if (depth < maxDepth) {
            if (children == null) {
                initChildren();
            }
            // Binary search to insert the point
            int pos = findPosition(p);
            return children[pos].insert(p, maxDepth);
        } else {
            // is leaf
            if (geometries == null) {
                geometries = new ArrayList<>();
            }
            geometries.add(p);
            return true;
        }
    }

    public boolean contains(Vector3 p) {
        // If the point is out of bounds
        if (p.x > max.x
            || p.x < min.x
            || p.y > max.y
            || p.y < min.y
            || p.z > max.z
            || p.z < min.z) {

            return false;
        }
        return true;
    }

    public OctreeNode queryNode(Vector3 p) {
        // If point is out of bound
        if (!contains(p)) {
            return null;
        }

        int pos = findPosition(p);

        // If not a leaf
        if (children != null) {
            return children[pos].queryNode(p);
        }

        return this;
    }

    private int findPosition(Vector3 p) {
        // Perform binary search
        // for each ordinate
        float midx = (max.x + min.x) / 2;
        float midy = (max.y + min.y) / 2;
        float midz = (max.z + min.z) / 2;

        int pos;

        // Deciding the position
        // where to move
        if (p.x <= midx) {
            if (p.y <= midy) {
                if (p.z <= midz) {
                    pos = BottomLeftBack;
                } else {
                    pos = BottomLeftFront;
                }
            } else {
                if (p.z <= midz) {
                    pos = TopLeftBack;
                } else {
                    pos = TopLeftFront;
                }
            }
        } else {
            if (p.y <= midy) {
                if (p.z <= midz) {
                    pos = BottomRightBack;
                } else {
                    pos = BottomRightFront;
                }
            } else {
                if (p.z <= midz) {
                    pos = TopRightBack;
                } else {
                    pos = TopRightFront;
                }
            }
        }
        return pos;
    }

    public List<Object> getGeometries() {
        return geometries;
    }

    public boolean isLeaf() {
        return geometries != null;
    }

}
