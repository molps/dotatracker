package com.example.nikola.dotatracker.utils;


import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.nikola.dotatracker.HeroStats;
import com.example.nikola.dotatracker.Player;
import com.example.nikola.dotatracker.RecentMatches;
import com.example.nikola.dotatracker.interfaces.MyListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String OPENDOTA_BASE_URL = "https://api.opendota.com/api";

    private static final String PATH_PLAYERS = "players";
    private static final String PATH_SEARCH = "search";
    private static final String PATH_HEROES = "heroes";
    private static final String PATH_RECENT_MATCHES = "recentMatches";

    private static final String PARAM_QUERY = "q";

    private static final String PARAM_SIMILARITY = "similarity";

    private static final String similarity = "1";

    public static List<MyListItem> fetchRecentMatchesData(String query) {
        if (TextUtils.isEmpty(query)) {
            Log.v(LOG_TAG, "RECENT: vratio je null");
            return null;
        }
        URL url = buildUrlForRecentMatches(query);
        try {
            return filterJsonResponseForRecentMathces(getJsonResponseFromHttp(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v(LOG_TAG, "RECENT: vratio je null");
        return null;
    }


    public static List<MyListItem> fetchSearchData(String query) {
        Log.v(LOG_TAG, "TIMECHECKING: fetchSearchData STARTED ");
        if (TextUtils.isEmpty(query)) {
            Log.v(LOG_TAG, "TIMECHECKING: fetchSearchData RETURNED = null");
            return null;
        }
        Log.v(LOG_TAG, "TIMECHECKING: fetchSearchData query = " + query);
        URL url = buildUrlForSearch(query);
        try {
            Log.v(LOG_TAG, "TIMECHECKING: fetchSearchData RETURNED = query je ispravan, uskoro stize odgovor");
            return filterJsonSearchResponse(getJsonResponseFromHttp(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v(LOG_TAG, "TIMECHECKING: fetchSearchData RETURNED = null (ovo ne bi smeo da vidis");
        return null;

    }

    public static List<MyListItem> fetchHeroStatsData(String playerId) {
        Log.v(LOG_TAG, "JSON RESPONSE JE: " + playerId);
        if (TextUtils.isEmpty(playerId)) {
            return new ArrayList<>();
        }
        URL url = buildUrlForHeroStats(playerId);
        try {
            return filterJsonHeroStatsResponse(getJsonResponseFromHttp(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static URL buildUrlForRecentMatches(String query) {
        Uri builtUri = Uri.parse(OPENDOTA_BASE_URL).buildUpon()
                .appendPath(PATH_PLAYERS)
                .appendPath(query)
                .appendPath(PATH_RECENT_MATCHES).build();
        Log.v(LOG_TAG, "URL ZA RECENT JE: " + builtUri.toString());
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static URL buildUrlForSearch(String query) {
        Uri builtUri = Uri.parse(OPENDOTA_BASE_URL).buildUpon()
                .appendPath(PATH_SEARCH)
                .appendQueryParameter(PARAM_QUERY, query)
                .appendQueryParameter(PARAM_SIMILARITY, similarity).build();
        Log.v(LOG_TAG, "URL je: " + builtUri.toString());
        URL url = null;


        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    private static URL buildUrlForHeroStats(String playerId) {
        Uri builtUri = Uri.parse(OPENDOTA_BASE_URL).buildUpon()
                .appendPath(PATH_PLAYERS)
                .appendPath(playerId)
                .appendPath(PATH_HEROES).build();
        URL url = null;
        Log.v(LOG_TAG, "BUILT URI = " + builtUri.toString());
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    private static String getJsonResponseFromHttp(URL url) throws IOException {
        long startTime, endTime;
        Log.v(LOG_TAG, "TIMECHECKING: getJsonResponseFtomHttp STARTED");
        startTime = System.currentTimeMillis();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            connection.disconnect();
            endTime = System.currentTimeMillis();
            Log.v(LOG_TAG, "TIMECHECKING: getJsonResponseFtomHttp = " + (endTime - startTime));
            Log.v(LOG_TAG, "TIMECHECKING: getJsonResponseFtomHttp FINISHED");
        }
    }

    private static List<MyListItem> filterJsonSearchResponse(String jsonResponse) {
        long startTime, endTime;
        Log.v(LOG_TAG, "TIMECHECKING: filterJsonSearchResponse STARTED");
        startTime = System.currentTimeMillis();
        List<MyListItem> list = new ArrayList<>();
        try {
            JSONArray baseJson = new JSONArray(jsonResponse);
            for (int i = 0; i < baseJson.length(); i++) {
                JSONObject currentPlayer = baseJson.getJSONObject(i);
                list.add(new Player(
                        currentPlayer.getString("personaname"),
                        currentPlayer.getLong("account_id"),
                        currentPlayer.getString("avatarfull")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        endTime = System.currentTimeMillis();
        Log.v(LOG_TAG, "TIMECHECKING: filterJsonSearchResponse: " + (endTime - startTime));
        Log.v(LOG_TAG, "TIMECHECKING: filterJsonSearchResponse FINISHED");
        return list;
    }

    private static List<MyListItem> filterJsonHeroStatsResponse(String jsonResponse) {
        Log.v(LOG_TAG, "JSON full: " + jsonResponse);
        List<MyListItem> list = new ArrayList<>();
        try {
            JSONArray baseJson = new JSONArray(jsonResponse);
            for (int i = 0; i < baseJson.length(); i++) {
                JSONObject currentHero = baseJson.getJSONObject(i);
                list.add(new HeroStats(
                        currentHero.getInt("games"),
                        currentHero.getInt("win"),
                        currentHero.getInt("hero_id")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    private static List<MyListItem> filterJsonResponseForRecentMathces(String jsonReponse) {
        List<MyListItem> list = new ArrayList<>();
        try {
            JSONArray baseJson = new JSONArray(jsonReponse);
            for (int i = 0; i < baseJson.length(); i++) {
                JSONObject currentMatch = baseJson.getJSONObject(i);
                list.add(new RecentMatches(
                        currentMatch.getInt("hero_id"),
                        currentMatch.getBoolean("radiant_win"),
                        currentMatch.getInt("player_slot"),
                        currentMatch.getString("skill"),
                        currentMatch.getInt("game_mode"),
                        currentMatch.getInt("lobby_type"),
                        currentMatch.getInt("duration"),
                        currentMatch.getLong("start_time")
                ));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;

    }


}
