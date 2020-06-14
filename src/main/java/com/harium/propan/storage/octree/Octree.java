package com.harium.propan.storage.octree;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Triangle;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Octree implements BSP {

    private int maxDepth = 5;

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
        if (!root.contains(p)) {
            resize(p);
            root.insert(p, maxDepth);
        }
    }

    @Override
    public void insert(Triangle triangle) {
        if (!root.contains(triangle)) {
            resize(triangle);
            root.insert(triangle, maxDepth);
        }

        // TODO Maybe resize
        // Find what nodes intersects with the triangle
        // For each node, find leafs that intersects
        // Add the triangle to geometries (more than one node would have the triangle)
    }

    private OctreeNode resize(Vector3 p) {
        Set<Object> geometries = getAllGeometries();

        Vector3 min = new Vector3(
            Math.min(root.getMin().x, p.x),
            Math.min(root.getMin().y, p.y),
            Math.min(root.getMin().z, p.z));

        Vector3 max = new Vector3(
            Math.max(root.getMax().x, p.x),
            Math.max(root.getMax().y, p.y),
            Math.max(root.getMax().z, p.z));

        resize(geometries, min, max);
        return root;
    }

    private OctreeNode resize(Triangle triangle) {
        Set<Object> geometries = getAllGeometries();

        Vector3 min = new Vector3(
            Math.min(root.getMin().x, Math.min(Math.min(triangle.a.x, triangle.b.x), triangle.c.x)),
            Math.min(root.getMin().y, Math.min(Math.min(triangle.a.y, triangle.b.y), triangle.c.y)),
            Math.min(root.getMin().z, Math.min(Math.min(triangle.a.z, triangle.b.z), triangle.c.z)));

        Vector3 max = new Vector3(
            Math.max(root.getMax().x, Math.max(Math.max(triangle.a.x, triangle.b.x), triangle.c.x)),
            Math.max(root.getMax().y, Math.max(Math.max(triangle.a.y, triangle.b.y), triangle.c.y)),
            Math.max(root.getMax().z, Math.max(Math.max(triangle.a.z, triangle.b.z), triangle.c.z)));

        resize(geometries, min, max);
        return root;
    }

    private void resize(Set<Object> geometries, Vector3 min, Vector3 max) {
        root.getMin().set(min);
        root.getMax().set(max);
        // Resize children
        root.initChildren();

        // Reinsert geometries
        for (Object object : geometries) {
            if (object instanceof Vector3) {
                root.insert((Vector3) object, maxDepth);
            }
        }
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

    public List<Object> queryNear(Triangle triangle) {
        List<OctreeNode> nodes = root.queryNodes(triangle);
        List<Object> geometries = new ArrayList<>();
        for (OctreeNode node : nodes) {
            geometries.addAll(node.getGeometries());
        }
        return geometries;
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
  
