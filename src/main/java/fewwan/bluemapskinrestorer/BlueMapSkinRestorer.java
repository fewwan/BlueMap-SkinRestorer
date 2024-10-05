package fewwan.bluemapskinrestorer;

import de.bluecolored.bluemap.api.BlueMapAPI;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlueMapSkinRestorer implements ModInitializer {
    public static final String MOD_ID = "bluemapskinrestorer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        BlueMapAPI.onEnable(api -> api.getPlugin().setSkinProvider(new SkinRestorerSkinProvider()));
    }
}
