package com.harium.propan.storage.octree;

import com.badlogic.gdx.math.Vector3;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Octree implements BSP {

    private int maxDepth = 3;

    // Upper Level
    protected static final int TopLeftFront = 0;
    protected static final int TopRightFront = 1;
    protected static final int TopRightBack = 2;
    protected static final int TopLeftBack = 3;
    // Bottom Level
    protected static final int BottomLeftFront = 4;
    protected static final int BottomRightFront = 5;
    protected static final int BottomRightBack = 6;
    protected static final int BottomLeftBack = 7;

    private OctreeNode root;

    public Octree() {
        super();
        this.root = new OctreeNode(Vector3.Zero, Vector3.Zero);
    }

    public Octree(Vector3 minimum, Vector3 maximum) {
        super();

        Vector3 realMin = new Vector3(
            Math.min(minimum.x, maximum.x),
            Math.min(minimum.y, maximum.y),
            Math.min(minimum.z, maximum.z));

        Vector3 realMax = new Vector3(
            Math.max(minimum.x, maximum.x),
            Math.max(minimum.y, maximum.y),
            Math.max(minimum.z, maximum.z));

        this.root = new OctreeNode(realMin, realMax);
    }

    public void insert(Vector3 p) {
        if (!root.insert(p, maxDepth)) {
            resize(p);
            root.insert(p, maxDepth);
        }
    }

    private OctreeNode resize(Vector3 p) {
        Set<Object> geometries = getAllGeometries();

        Vector3 min = new Vector3(
            Math.min(root.min.x, p.x),
            Math.min(root.min.y, p.y),
            Math.min(root.min.z, p.z));

        Vector3 max = new Vector3(
            Math.max(root.max.x, p.x),
            Math.max(root.max.y, p.y),
            Math.max(root.max.z, p.z));

        root.min = min;
        root.max = max;
        // Resize children
        root.initChildren();

        // Reinsert geometries
        for (Object object : geometries) {
            if (object instanceof Vector3) {
                root.insert((Vector3) object, maxDepth);
            }
        }

        return root;
    }

    private Set<Object> getAllGeometries() {
        Set<Object> geometries = new LinkedHashSet<>();
        getAllGeometries(geometries, root);

        return geometries;
    }

    private void getAllGeometries(Set<Object> geometries, OctreeNode node) {
        if (node.isLeaf()) {
            geometries.addAll(node.getGeometries());
            return;
        }
        if (node.children == null) {
            return;
        }

        for (OctreeNode child : node.children) {
            getAllGeometries(geometries, child);
        }
    }

    public List<Object> queryNear(Vector3 p) {
        OctreeNode node = root.queryNode(p);
        return node.getGeometries();
    }

    @Override
    public OctreeNode getRoot() {
        return root;
    }

    public Octree maxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
        return this;
    }

    public Set<Object> getAll() {
        return getAllGeometries();
    }
}
  
