plugins {
    id("dev.kikugie.stonecutter")
    id("me.modmuss50.mod-publish-plugin") version "2.0.0-beta.2"
}

stonecutter active "1.21.8"

stonecutter parameters {
    swaps["mod_version"] = "\"${property("mod.version")}\";"
    swaps["minecraft"] = "\"${node.metadata.version}\";"
    constants["release"] = property("mod.id") != "template"
    dependencies["fapi"] = node.project.property("deps.fabric_api") as String

    replacements {
        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
        }

        string(current.parsed >= "26") {
            replace("net.fabricmc.fabric.api.client.keybinding", "net.fabricmc.fabric.api.client.keymapping")
            replace("KeyBindingHelper", "KeyMappingHelper")
            replace("registerKeyBinding", "registerKeyMapping")
            replace("playS2C", "clientboundPlay")
            replace("playC2S", "serverboundPlay")
            replace("displayClientMessage", "sendSystemMessage")
            replace("LevelRenderState", "GameRenderState")
        }
    }
}