import gg.essential.gradle.util.noServerRunConfigs

plugins {
    kotlin("jvm")
    id("gg.essential.multi-version")
    id("gg.essential.defaults")
}

val modGroup: String by project
val modBaseName: String by project
group = modGroup
base.archivesName.set("$modBaseName-${platform.mcVersionStr}")

loom {
    noServerRunConfigs()
    forge {
        accessTransformer(rootProject.file("src/main/resources/hypixel_autocomplete_at.cfg"))
    }
    launchConfigs {
        getByName("client") {
            arg("--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker")
        }
    }
}

val embed by configurations.creating
configurations.implementation.get().extendsFrom(embed)

dependencies {
    compileOnly("gg.essential:essential-$platform:2666")
    embed("gg.essential:loader-launchwrapper:1.1.3")
}

tasks.processResources {
    rename("(.+_at.cfg)", "META-INF/$1")
}

tasks.jar {
    from(embed.files.map { zipTree(it) })

    manifest.attributes(
        mapOf(
            "ModSide" to "CLIENT",
            "FMLAT" to "hypixel_autocomplete_at.cfg",
            "FMLCorePluginContainsFMLMod" to "Yes, yes it does",
            "TweakClass" to "gg.essential.loader.stage0.EssentialSetupTweaker",
            "TweakOrder" to "0"
        )
    )
}