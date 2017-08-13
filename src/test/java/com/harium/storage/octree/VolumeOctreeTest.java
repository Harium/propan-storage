package com.harium.storage.octree;


import com.harium.etyl.linear.Point3D;
import com.harium.propan.core.model.Face;
import com.harium.propan.linear.BoundingBox3D;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VolumeOctreeTest {

    private VolumeOctree<Face> tree;

    @Before
    public void setUp() {

        Point3D minPoint = new Point3D(0, 0, 0);
        Point3D maxPoint = new Point3D(10, 10, 10);

        BoundingBox3D box = new BoundingBox3D(minPoint, maxPoint);

        tree = new VolumeOctree<Face>(box);
    }

    @Test
    public void testInit() {
        OctreeNode<Face> root = tree.getRoot();

        Assert.assertNotNull(root);
        Assert.assertEquals(0, root.children.size());
    }

    @Test
    public void testAddPoint() {
        tree.add(new Point3D(1, 1, 0), null);

        OctreeNode<Face> root = tree.getRoot();

        OctreeNode<Face> leftLower = root.children.get(Octree.BELOW_LEFT_LOWER);
        Assert.assertNotNull(leftLower);

        BoundingBox3D innerBox = leftLower.box;
        Assert.assertEquals(15.625, innerBox.getVolume(), 0.1);

        Assert.assertNotNull(leftLower.children.get(Octree.BELOW_LEFT_LOWER));
    }

}
