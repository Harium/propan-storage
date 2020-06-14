package com.harium.propan.storage.octree;

import com.badlogic.gdx.math.Vector3;
import java.util.List;

public interface BSP {

    void insert(Vector3 point);

    List<Object> queryNear(Vector3 point);

    OctreeNode getRoot();

}
