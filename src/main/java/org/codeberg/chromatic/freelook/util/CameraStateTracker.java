package org.codeberg.chromatic.freelook.util;

import net.minecraft.world.entity.Entity;
import java.util.HashMap;
import java.util.Map;

public class CameraStateTracker {
    private static final Map<Entity, Float> pitchMap = new HashMap<>();
    private static final Map<Entity, Float> yawMap = new HashMap<>();

    public static float getCameraPitch(Entity entity) {
        return pitchMap.getOrDefault(entity, 0f);
    }

    public static float getCameraYaw(Entity entity) {
        return yawMap.getOrDefault(entity, 0f);
    }

    public static void setCameraPitch(Entity entity, float value) {
        pitchMap.put(entity, value);
    }

    public static void setCameraYaw(Entity entity, float value) {
        yawMap.put(entity, value);
    }
}
