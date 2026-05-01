package org.codeberg.chromatic.freelook.option;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.NameableEnum;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.*;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.CameraType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class FreelookConfig {
    public static final ConfigClassHandler<FreelookConfig> HANDLER = ConfigClassHandler.createBuilder(FreelookConfig.class)
            .id(ResourceLocation.fromNamespaceAndPath("freelook", "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("freelook.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    @AutoGen(category = "freelook")
    @EnumCycler
    @SerialEntry
    @CustomDescription("yacl3.config.freelook:config.perspectiveType.description")
    public PerspectiveType perspectiveType = PerspectiveType.THIRD_PERSON_BACK;

    @AutoGen(category = "freelook")
    @EnumCycler
    @SerialEntry
    @CustomDescription("yacl3.config.freelook:config.pressMode.description")
    public PressMode pressMode = PressMode.QUICK_PRESS;

    @AutoGen(category = "freelook")
    @LongSlider(min = 50, max = 1000, step = 50)
    @SerialEntry
    @CustomDescription("yacl3.config.freelook:config.holdThreshold.description")
    // TODO: Depends on pressMode == PressMode.QUICK_PRESS
    public long holdThreshold = 300;

    @AutoGen(category = "freelook")
    @TickBox
    @SerialEntry
    @CustomDescription("yacl3.config.freelook:config.addToCameraCycle.description")
    public boolean addToCameraCycle = false;

    @AutoGen(category = "freelook")
    @EnumCycler
    @SerialEntry
    @CustomDescription("yacl3.config.freelook:config.onCycleChange.description")
    public CycleChangeAction onCycleChange = CycleChangeAction.STOP_FREELOOK;

    @AutoGen(category = "freelook")
    @TickBox
    @SerialEntry
    @CustomImage(value = "textures/descriptions/smooth_camera.webp")
    @CustomDescription("yacl3.config.freelook:config.smoothCamera.description")
    public boolean smoothCamera = false;

    @AutoGen(category = "pitch")
    @MasterTickBox({"invertPitch", "lockPitch"})
    @SerialEntry
    @CustomDescription("yacl3.config.freelook:config.pitchEnabled.description")
    public boolean pitchEnabled = true;

    @AutoGen(category = "pitch")
    @TickBox
    @SerialEntry
    @CustomDescription("yacl3.config.freelook:config.invertPitch.description")
    public boolean invertPitch = false;

    @AutoGen(category = "pitch")
    @TickBox
    @SerialEntry
    @CustomImage(value = "textures/descriptions/pitch_lock.webp")
    @CustomDescription("yacl3.config.freelook:config.lockPitch.description")
    public boolean lockPitch = false;

    @AutoGen(category = "yaw")
    @MasterTickBox({"invertYaw"})
    @SerialEntry
    @CustomDescription("yacl3.config.freelook:config.yawEnabled.description")
    public boolean yawEnabled = true;

    @AutoGen(category = "yaw")
    @TickBox
    @SerialEntry
    @CustomDescription("yacl3.config.freelook:config.invertYaw.description")
    public boolean invertYaw = false;

    public enum PerspectiveType implements NameableEnum {
        FIRST_PERSON,
        THIRD_PERSON_BACK,
        THIRD_PERSON_FRONT;

        public CameraType asCameraType() {
            return switch (this) {
                case FIRST_PERSON -> CameraType.FIRST_PERSON;
                case THIRD_PERSON_BACK -> CameraType.THIRD_PERSON_BACK;
                case THIRD_PERSON_FRONT -> CameraType.THIRD_PERSON_FRONT;
            };
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable("yacl3.config.freelook:config.perspectiveType." + name().toLowerCase());
        }
    }

    public enum PressMode implements NameableEnum {
        HOLD,
        QUICK_PRESS,
        TOGGLE;

        @Override
        public Component getDisplayName() {
            return Component.translatable("yacl3.config.freelook:config.pressMode." + name().toLowerCase());
        }
    }

    public enum CycleChangeAction implements NameableEnum {
        CHANGE_AND_FREELOOK,
        STOP_FREELOOK,
        BLOCK_PERSPECTIVE_CHANGE;

        @Override
        public Component getDisplayName() {
            return Component.translatable("yacl3.config.freelook:config.onCycleChange." + name().toLowerCase());
        }
    }
}
