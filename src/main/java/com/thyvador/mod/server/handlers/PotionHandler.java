package com.thyvador.mod.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PotionHandler implements CustomHttpHandler {

    private static final String EFFECT = "effect";
    private static final String DURATION = "duration";
    private static final String AMPLIFIER = "duration";

    private final Logger logger = LogManager.getLogger();
    private static final Map<String, Effect> EFFECTS = new HashMap<>();

    static {
        EFFECTS.put("SPEED", Effects.SPEED);
        EFFECTS.put("SLOWNESS", Effects.SLOWNESS);
        EFFECTS.put("HASTE", Effects.HASTE);
        EFFECTS.put("MINING_FATIGUE", Effects.MINING_FATIGUE);
        EFFECTS.put("STRENGTH", Effects.STRENGTH);
        EFFECTS.put("INSTANT_HEALTH", Effects.INSTANT_HEALTH);
        EFFECTS.put("INSTANT_DAMAGE", Effects.INSTANT_DAMAGE);
        EFFECTS.put("JUMP_BOOST", Effects.JUMP_BOOST);
        EFFECTS.put("NAUSEA", Effects.NAUSEA);
        EFFECTS.put("REGENERATION", Effects.REGENERATION);
        EFFECTS.put("RESISTANCE", Effects.RESISTANCE);
        EFFECTS.put("FIRE_RESISTANCE", Effects.FIRE_RESISTANCE);
        EFFECTS.put("WATER_BREATHING", Effects.WATER_BREATHING);
        EFFECTS.put("INVISIBILITY", Effects.INVISIBILITY);
        EFFECTS.put("BLINDNESS", Effects.BLINDNESS);
        EFFECTS.put("NIGHT_VISION", Effects.NIGHT_VISION);
        EFFECTS.put("HUNGER", Effects.HUNGER);
        EFFECTS.put("WEAKNESS", Effects.WEAKNESS);
        EFFECTS.put("POISON", Effects.POISON);
        EFFECTS.put("WITHER", Effects.WITHER);
        EFFECTS.put("HEALTH_BOOST", Effects.HEALTH_BOOST);
        EFFECTS.put("ABSORPTION", Effects.ABSORPTION);
        EFFECTS.put("SATURATION", Effects.SATURATION);
        EFFECTS.put("GLOWING", Effects.GLOWING);
        EFFECTS.put("LEVITATION", Effects.LEVITATION);
        EFFECTS.put("LUCK", Effects.LUCK);
        EFFECTS.put("UNLUCK", Effects.UNLUCK);
        EFFECTS.put("SLOW_FALLING", Effects.SLOW_FALLING);
        EFFECTS.put("CONDUIT_POWER", Effects.CONDUIT_POWER);
        EFFECTS.put("DOLPHINS_GRACE", Effects.DOLPHINS_GRACE);
        EFFECTS.put("BAD_OMEN", Effects.BAD_OMEN);
        EFFECTS.put("HERO_OF_THE_VILLAGE", Effects.HERO_OF_THE_VILLAGE);
    }


    @Override
    public void handlePostRequest(HttpExchange httpExchange) throws IOException {
        try {
            logger.debug("POST " + httpExchange.getRequestURI());

            String[] playerNames = Minecraft.getInstance().getIntegratedServer().getPlayerList().getOnlinePlayerNames();
            if (playerNames.length == 0) {
                handleResponse(httpExchange, 500, "player not found");
                return;
            }

            JSONObject requestBody = getRequestBody(httpExchange);

            Effect effect;
            int durationInSeconds;
            int amplifier = 0;
            String effectName = requestBody.getString(EFFECT);
            try {

                if (effectName == null) {
                    handleResponse(httpExchange, 400, "effect is missing");
                    return;
                }
                effect = EFFECTS.get(effectName);

                if (effect == null) {
                    handleResponse(httpExchange, 400, "effect " + effectName + " foes not exist");
                    return;
                }
            } catch (JSONException e) {

                logger.error("Effect is missing");
                handleResponse(httpExchange, 400, "effect is missing");
                return;
            }
            try {

                durationInSeconds = requestBody.getInt(DURATION);
            } catch (JSONException e) {

                logger.error("Duration is missing");
                handleResponse(httpExchange, 400, "duration is missing");
                return;
            }
            try {

                amplifier = requestBody.getInt(AMPLIFIER) - 1;
            } catch (JSONException ignored) {
            }
            if (amplifier < 0) {
                amplifier = 0;
            }
            String redeemedBy = requestBody.getString("redeemedBy");
            String rewardCost = requestBody.getString("rewardCost");
            String playerName = Minecraft.getInstance().player.getName().getString();
            EffectInstance effectInstance = new EffectInstance(effect, durationInSeconds * 20, amplifier);
            Arrays.stream(playerNames)
                    .filter(name -> name.equals(playerName))
                    .findAny()
                    .ifPresent(name -> applyPotionToPLayer(name, effectInstance, redeemedBy, rewardCost));


            Minecraft.getInstance().player.sendChatMessage(
                    String.format(
                            "User %s applied %s potion effect for %s channel points !",
                            redeemedBy,
                            effectName.toLowerCase().replaceAll("_", " "),
                            rewardCost));

            handleResponse(httpExchange, 200, "{\"status\": \"Potion effect successfully applied\"}");

        } catch (Exception exception) {
            logger.error(exception.getMessage());
            handleResponse(httpExchange, 500, exception.getMessage());
        }

    }

    private void applyPotionToPLayer(String name, EffectInstance effectInstance, String redeemedBy, String rewardCost) {
        ServerPlayerEntity player = Minecraft.getInstance().getIntegratedServer().getPlayerList().getPlayerByUsername(name);
        // EffectInstance expects a duration in ticks (1 tick = 1/20 second)
        player.addPotionEffect(effectInstance);
        logger.info("Effect applied to " + player.getDisplayName());

    }

    private JSONObject getRequestBody(HttpExchange httpExchange) throws IOException {
        String body = IOUtils.toString(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
        return new JSONObject(body);
    }

    @Override
    public void handleGetRequest(HttpExchange httpExchange) throws IOException {
        logger.debug("GET " + httpExchange.getRequestURI());
        JSONArray effects = new JSONArray(EFFECTS.keySet());
        handleResponse(httpExchange, 200, effects.toString());
    }
}
