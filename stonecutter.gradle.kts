plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "26.3"

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
            replace("playS2C", "clientboundPlay")
            replace("playC2S", "serverboundPlay")
            replace("displayClientMessage", "sendSystemMessage")
        }
    }
}

stonecutter tasks {
    order("publishModrinth")
}