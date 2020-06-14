package com.harium.propan.storage.octree;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Triangle;
import java.util.List;

public interface BSP {

    void insert(Vector3 point);

    void insert(Triangle triangle);

    List<Object> queryNear(Vector3 point);

    List<Object> queryNear(Triangle triangle);

    OctreeNode getRoot();

}
