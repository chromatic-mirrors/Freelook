package org.codeberg.chromatic.freelook.integration.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import org.codeberg.chromatic.freelook.option.FreelookConfig;

public class ModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return s -> FreelookConfig.HANDLER.generateGui().generateScreen(s);
    }
}
