package com.harium.propan.storage.octree;

import static org.junit.Assert.assertEquals;

import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Intersector;
import java.util.Set;
import org.junit.Test;

public class AABBPlaneTest {

    @Test
    public void testInit() {
        Plane plane = new Plane();

        BoundingBox box = new BoundingBox(new Vector3(-1,-1,-1), new Vector3(1,1,1));

        Vector3 e = new Vector3(box.max).sub(box.getCenter(new Vector3()));
        Vector3 ext = box.getDimensions(new Vector3()).scl(0.5f);

        Intersector.intersectBoundsPlaneFast(box, plane);
    }


}
