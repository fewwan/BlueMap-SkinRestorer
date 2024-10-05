package fewwan.bluemapskinrestorer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;
import de.bluecolored.bluemap.api.plugin.SkinProvider;
import net.lionarius.skinrestorer.SkinRestorer;
import net.lionarius.skinrestorer.skin.SkinStorage;
import net.lionarius.skinrestorer.skin.SkinValue;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

public class SkinRestorerSkinProvider implements SkinProvider {
    private static final Logger LOGGER = BlueMapSkinRestorer.LOGGER;
    private final SkinStorage skinStorage;

    public SkinRestorerSkinProvider() {
        this.skinStorage = SkinRestorer.getSkinStorage();
    }

    @Override
    public Optional<BufferedImage> load(UUID playerUUID) throws IOException {
        SkinValue skinValue = skinStorage.getSkin(playerUUID);

        if (skinValue != null) {
            Property propertyToUse = (skinValue.value() != null) ? skinValue.value() : skinValue.originalValue();

            if (propertyToUse != null) {
                String base64Value = propertyToUse.value();
                String decodedValue = new String(Base64.getDecoder().decode(base64Value), StandardCharsets.UTF_8);

                JsonObject jsonObject = JsonParser.parseString(decodedValue).getAsJsonObject();
                JsonObject textures = jsonObject.getAsJsonObject("textures");
                JsonObject skin = textures.getAsJsonObject("SKIN");

                if (skin != null && skin.has("url")) {
                    String skinUrl = skin.get("url").getAsString();

                    try (InputStream inputStream = new URI(skinUrl).toURL().openStream()) {
                        BufferedImage skinImage = ImageIO.read(inputStream);
                        return Optional.ofNullable(skinImage);
                    } catch (URISyntaxException e) {
                        LOGGER.warn("Invalid URI Syntax: {}", e.getMessage());
                    }
                }
            }
        }
        return Optional.empty();
    }
}
