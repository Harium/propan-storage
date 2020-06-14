package com.harium.propan.storage.octree;

import com.badlogic.gdx.math.collision.Intersector;
import com.badlogic.gdx.math.collision.Triangle;
import java.util.ArrayList;
import java.util.List;

public class OctreeTriangleHandler {

    public static boolean contains(OctreeNode node, Triangle triangle) {
        boolean contains = false;
        contains |= OctreePointHandler.contains(node, triangle.a);
        contains |= OctreePointHandler.contains(node, triangle.b);
        contains |= OctreePointHandler.contains(node, triangle.c);

        return contains;
    }

    public static boolean insert(OctreeNode node, Triangle triangle, int maxDepth) {
        // If the point is out of bounds
        if (!intersects(node, triangle)) {
            return false;
        }

        //If is not Leaf
        if (node.depth < maxDepth) {
            if (node.children == null) {
                node.initChildren();
            }
            // Binary search to insert the triangle
            for (OctreeNode child: node.children) {
                return insert(child, triangle, maxDepth);
            }
        }

        // If is leaf
        node.addGeometry(triangle);
        return true;
    }

    private static boolean intersects(OctreeNode node, Triangle triangle) {
        return Intersector.intersectBoundsTriangleFast(node.box, triangle);
    }

    public static List<OctreeNode> queryNodes(OctreeNode root, Triangle triangle) {
        List<OctreeNode> nodes = new ArrayList<>();
        // If point is out of bound
        if (!contains(root, triangle)) {
            return nodes;
        }

        for (OctreeNode child: root.children) {
            queryNodes(child, triangle, nodes);
        }

        return nodes;
    }

    private static void queryNodes(OctreeNode node, Triangle triangle, List<OctreeNode> nodes) {
        // If point is out of bound
        if (!intersects(node, triangle)) {
            return;
        }

        // If not a leaf
        if (node.children != null) {
            for (OctreeNode child: node.children) {
                queryNodes(child, triangle, nodes);
            }
        } else {
            nodes.add(node);
        }
    }

}
