package com.thyvador.mod.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.world.ClientWorld.ClientWorldInfo;
import net.minecraft.command.CommandSource;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WeatherHandler implements CustomHttpHandler{

    private final Logger logger = LogManager.getLogger();
    private static final Set<String> WEATHERS = new HashSet<>();

    static {
        WEATHERS.add("clear");
        WEATHERS.add("rain");
        WEATHERS.add("thunder");
    }


    @Override
    public void handlePostRequest(HttpExchange httpExchange) throws IOException {
        logger.debug("POST " + httpExchange.getRequestURI());

        ClientWorld world = Minecraft.getInstance().world;

        if (world == null) {
            handleResponse(httpExchange, 500, "world not found");
            return;
        }

        JSONObject requestBody = getRequestBody(httpExchange);
        String requestedWeather = requestBody.getString("weather");

        switch (requestedWeather) {
            case "clear":
                setClearWeather(world.getWorldInfo());
                break;
            case "rain":
                setRainWeather(world.getWorldInfo());
                break;
            case "thunder":
                setThunderWeather(world.getWorldInfo());
                break;
            default:
                handleResponse(httpExchange, 400, "Unknown weather " + requestedWeather);
        }

        String redeemedBy = requestBody.getString("redeemedBy");
        String rewardCost = requestBody.getString("rewardCost");

        Minecraft.getInstance().player.sendChatMessage("User " + redeemedBy + " applied " + requestedWeather + " weather for " + rewardCost + " channel points !");

        handleResponse(httpExchange, 200, "{\"status\": \"Weather effect successfully applied\"}");
    }

    private void setClearWeather(ClientWorldInfo clientWorldInfo) {
        Minecraft.getInstance().getIntegratedServer().getWorlds().forEach(serverWorld -> {
                    // I have no idea what this does
                    serverWorld.func_241113_a_(0, 6000, false, false);
                }
        );
    }

    private void setRainWeather(ClientWorldInfo clientWorldInfo) {
        Minecraft.getInstance().getIntegratedServer().getWorlds().forEach(serverWorld -> {
                    // I have no idea what this does, but it starts raining
                    serverWorld.func_241113_a_(0, 6000, true, false);
                }
        );
    }

    private void setThunderWeather(ClientWorldInfo clientWorldInfo) {
        Minecraft.getInstance().getIntegratedServer().getWorlds().forEach(serverWorld -> {
                // I have no idea what this does, but it starts raining
                serverWorld.func_241113_a_(0, 6000, true, true);
            }
        );
    }

    private JSONObject getRequestBody(HttpExchange httpExchange) throws IOException {
        String body = IOUtils.toString(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
        return new JSONObject(body);
    }

    @Override
    public void handleGetRequest(HttpExchange httpExchange) throws IOException {
        logger.debug("GET " + httpExchange.getRequestURI());
        JSONArray effects = new JSONArray(WEATHERS);
        handleResponse(httpExchange, 200, effects.toString());
    }
}
