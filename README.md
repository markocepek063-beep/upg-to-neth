# Upg (Fabric 1.21.11)

`/upg` upgrades the diamond armor and tools you have equipped (worn armor, main hand, off hand)
to netherite. Enchantments, durability damage, trims and custom names are kept.

Requires Fabric Loader 0.18.2+, Fabric API, Java 21.

## Build
1. Install JDK 21 and Gradle 9.x (or open the folder in IntelliJ IDEA and let it import).
2. Run `gradle wrapper` once, then `./gradlew build` (Windows: `gradlew.bat build`).
3. The jar is `build/libs/upg-1.0.0.jar`. Put it in your `mods` folder next to Fabric API.
