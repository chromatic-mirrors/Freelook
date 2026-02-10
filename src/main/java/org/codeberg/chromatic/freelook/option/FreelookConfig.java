package org.codeberg.chromatic.freelook.option;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.gui.controllers.cycling.EnumController;
import dev.isxander.yacl3.gui.controllers.slider.LongSliderController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.CameraType;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class FreelookConfig {
    public static final ConfigClassHandler<FreelookConfig> HANDLER = ConfigClassHandler.createBuilder(FreelookConfig.class)
            .id(Identifier.fromNamespaceAndPath("freelook", "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("freelook.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry
    public PerspectiveType perspectiveType = PerspectiveType.THIRD_PERSON_BACK;

    @SerialEntry
    public PressMode pressMode = PressMode.QUICK_PRESS;

    @SerialEntry
    public long holdThreshold = 300;

    @SerialEntry
    public boolean pitchEnabled = true;

    @SerialEntry
    public boolean invertPitch = false;

    @SerialEntry
    public boolean lockPitch = false;

    @SerialEntry
    public boolean yawEnabled = true;

    @SerialEntry
    public boolean invertYaw = false;

    @SerialEntry
    public boolean addToCameraCycle = false;

    @SerialEntry
    public CycleChangeAction onCycleChange = CycleChangeAction.STOP_FREELOOK;

    @SerialEntry
    public boolean smoothCamera = false;

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(HANDLER, ((defaults, config, builder) ->
                    builder
                            .title(Component.translatable("freelook.freelook"))
                            .category(ConfigCategory.createBuilder()
                                    .name(Component.translatable("freelook.perspective"))
                                    .group(OptionGroup.createBuilder()
                                            .name(Component.translatable("freelook.perspective.activation"))
                                            .description(OptionDescription.of(Component.translatable("freelook.perspective.pitch.description")))
                                            .option(Option.<PerspectiveType>createBuilder()
                                                    .name(Component.translatable("freelook.perspective_type"))
                                                    .description(OptionDescription.of(Component.translatable("freelook.perspective_type.description")))
                                                    .binding(defaults.perspectiveType, () -> config.perspectiveType, newVal -> config.perspectiveType = newVal)
                                                    .customController(opt -> new EnumController<>(opt, PerspectiveType.class))
                                                    .build()
                                            ).option(Option.<PressMode>createBuilder()
                                                    .name(Component.translatable("freelook.press_mode"))
                                                    .description(OptionDescription.of(Component.translatable("freelook.press_mode.description")))
                                                    .binding(defaults.pressMode, () -> config.pressMode, newVal -> config.pressMode = newVal)
                                                    .customController(opt -> new EnumController<>(opt, PressMode.class))
                                                    .build()
                                            ).option(Option.<Long>createBuilder()
                                                    .name(Component.translatable("freelook.hold_threshold"))
                                                    .description(OptionDescription.of(Component.translatable("freelook.hold_threshold.description")))
                                                    .binding(defaults.holdThreshold, () -> config.holdThreshold, newVal -> config.holdThreshold = newVal)
                                                    .customController(opt -> new LongSliderController(opt, 50, 1000, 50))
                                                    .build()
                                            ).build()
                                    )
                                    .group(OptionGroup.createBuilder()
                                            .name(Component.translatable("freelook.perspective.pitch"))
                                            .description(OptionDescription.of(Component.translatable("freelook.perspective.pitch.description")))
                                            .option(Option.<Boolean>createBuilder()
                                                    .name(Component.translatable("freelook.perspective.pitch.enabled"))
                                                    .description(OptionDescription.of(Component.translatable("freelook.perspective.pitch.enabled.description")))
                                                    .binding(defaults.pitchEnabled, () -> config.pitchEnabled, newVal -> config.pitchEnabled = newVal)
                                                    .controller(TickBoxControllerBuilder::create)
                                                    .build()
                                            )
                                            .option(Option.<Boolean>createBuilder()
                                                    .name(Component.translatable("freelook.perspective.pitch.invert"))
                                                    .description(OptionDescription.of(Component.translatable("freelook.perspective.pitch.invert.description")))
                                                    .binding(defaults.invertPitch, () -> config.invertPitch, newVal -> config.invertPitch = newVal)
                                                    .controller(TickBoxControllerBuilder::create)
                                                    .build()
                                            ).option(Option.<Boolean>createBuilder()
                                                    .name(Component.translatable("freelook.perspective.pitch.lock"))
                                                    .description(OptionDescription.of(Component.translatable("freelook.perspective.pitch.lock.description")))
                                                    .binding(defaults.lockPitch, () -> config.lockPitch, newVal -> config.lockPitch = newVal)
                                                    .controller(TickBoxControllerBuilder::create)
                                                    .build()
                                            ).build()
                                    ).group(OptionGroup.createBuilder()
                                            .name(Component.translatable("freelook.perspective.yaw"))
                                            .description(OptionDescription.of(Component.translatable("freelook.perspective.yaw.description")))
                                            .option(Option.<Boolean>createBuilder()
                                                    .name(Component.translatable("freelook.perspective.yaw.enabled"))
                                                    .description(OptionDescription.of(Component.translatable("freelook.perspective.yaw.enabled.description")))
                                                    .binding(defaults.yawEnabled, () -> config.yawEnabled, newVal -> config.yawEnabled = newVal)
                                                    .controller(TickBoxControllerBuilder::create)
                                                    .build()
                                            )
                                            .option(Option.<Boolean>createBuilder()
                                                    .name(Component.translatable("freelook.perspective.yaw.invert"))
                                                    .description(OptionDescription.of(Component.translatable("freelook.perspective.yaw.invert.description")))
                                                    .binding(defaults.invertYaw, () -> config.invertYaw, newVal -> config.invertYaw = newVal)
                                                    .controller(TickBoxControllerBuilder::create)
                                                    .build()
                                            ).build()
                                    ).build()
                            ).category(ConfigCategory.createBuilder()
                                    .name(Component.translatable("freelook.misc"))
                                    .option(Option.<Boolean>createBuilder()
                                            .name(Component.translatable("freelook.perspective.add_to_camera_cycle"))
                                            .description(OptionDescription.of(Component.translatable("freelook.perspective.add_to_camera_cycle.description")))
                                            .binding(defaults.addToCameraCycle, () -> config.addToCameraCycle, newVal -> config.addToCameraCycle = newVal)
                                            .controller(TickBoxControllerBuilder::create)
                                            .build()
                                    ).option(Option.<CycleChangeAction>createBuilder()
                                            .name(Component.translatable("freelook.cycle_change_action"))
                                            .description(OptionDescription.of(Component.translatable("freelook.cycle_change_action.description")))
                                            .binding(defaults.onCycleChange, () -> config.onCycleChange, newVal -> config.onCycleChange = newVal)
                                            .customController(opt -> new EnumController<>(opt, CycleChangeAction.class))
                                            .build()
                                    ).option(Option.<Boolean>createBuilder()
                                            .name(Component.translatable("freelook.perspective.smooth_camera"))
                                            .description(OptionDescription.of(Component.translatable("freelook.perspective.smooth_camera.description")))
                                            .binding(defaults.smoothCamera, () -> config.smoothCamera, newVal -> config.smoothCamera = newVal)
                                            .controller(TickBoxControllerBuilder::create)
                                            .build()
                                    ).build()
                            )
                ))
                .generateScreen(parent);
    }

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
            return Component.translatable("freelook.perspective_type." + name().toLowerCase());
        }
    }

    public enum PressMode implements NameableEnum {
        HOLD,
        QUICK_PRESS,
        TOGGLE;

        @Override
        public Component getDisplayName() {
            return Component.translatable("freelook.press_mode." + name().toLowerCase());
        }
    }

    public enum CycleChangeAction implements NameableEnum {
        CHANGE_AND_FREELOOK,
        STOP_FREELOOK,
        BLOCK_PERSPECTIVE_CHANGE;

        @Override
        public Component getDisplayName() {
            return Component.translatable("freelook.cycle_change_action." + name().toLowerCase());
        }
    }
}
