package xyz.brassgoggledcoders.skyships.content;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import xyz.brassgoggledcoders.skyships.SkyShips;

public class SkyShipsText {
    public static Component KEY_CATEGORY = SkyShips.getRegistrate()
            .addRawLang("key.skyships.category", "SkyShips");

    public static Component DESCEND_KEY = SkyShips.getRegistrate()
            .addRawLang("key.skyships.descend", "Descend");

    public static String getKey(Component component) {
        if (component.getContents() instanceof TranslatableContents translatableContents) {
            return translatableContents.getKey();
        } else {
            throw new IllegalArgumentException("Component wasn't Translatable");
        }
    }


    public static void setup() {

    }
}
