package com.harium.storage.octree;

import com.harium.etyl.linear.Point3D;
import com.harium.propan.linear.BoundingBox3D;

import java.util.*;

public class OctreeNode<T> {

    protected BoundingBox3D box;

    protected Map<Integer, OctreeNode<T>> children;

    protected Set<T> dataSet = new HashSet<T>();

    protected List<Point3D> geometry = new ArrayList<Point3D>();

    public OctreeNode(BoundingBox3D box) {
        super();
        this.box = box;

        children = new HashMap<Integer, OctreeNode<T>>(8);
    }

    protected Map<Integer, OctreeNode<T>> getChildren() {
        return children;
    }

    public Collection<OctreeNode<T>> getChildrenNodes() {
        return children.values();
    }

    public BoundingBox3D getBox() {
        return box;
    }

}

