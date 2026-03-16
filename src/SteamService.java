import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class SteamService {

    private final String API_KEY = System.getenv("STEAM_API_KEY");

    public String getSteamIdFromNickname(String nickname){

        if(isNumeric(nickname)){
            return nickname;
        }

        String url = buildSteamIdUrl(nickname);

        JsonObject response = request(url).getAsJsonObject("response");

        int success = response.get("success").getAsInt();

        if(success != 1){
            throw new RuntimeException("Player not found");
        }

        return response.get("steamid").getAsString();

    }

    public Player getPlayerInfo(String steamId){

        String playerUrl = buildPlayerUrl(steamId);

        JsonObject response = request(playerUrl).getAsJsonObject("response");

        JsonArray players = response.getAsJsonArray("players");

        try {
            if(players.isEmpty()){
                throw new RuntimeException("Player not found");
            }

            JsonObject player = players.get(0).getAsJsonObject();

            String name = player.get("personaname").getAsString();
            String profile = player.get("profileurl").getAsString();
            String avatar = player.get("avatarfull").getAsString();

            return new Player(name, profile, avatar);

        }
        catch (Exception exception){
            throw new RuntimeException("API error", exception);
        }
    }

    public int getCSHours(String steamId){

        String gamesUrl = buildGamesUrl(steamId);
        String json = getJson(gamesUrl);
        JsonObject obj = getJsonObj(json);

        JsonArray games =
                obj.getAsJsonObject("response")
                        .getAsJsonArray("games");

        for (int i = 0; i < games.size(); i++){

            JsonObject game = games.get(i).getAsJsonObject();

            if(game.get("appid").getAsInt() == 730){

                int minutes = game.get("playtime_forever").getAsInt();
                int hours = minutes / 60;

                return hours;
            }
        }
        return 0; // in case player doesnt have CS
    }

    public JsonObject request(String url){

        try{
            String json = HttpClientHelper.get(url);
            return JsonParser.parseString(json).getAsJsonObject();

        } catch (Exception exception) {
            throw new RuntimeException("API request failed: " + url, exception);
        }
    }

    public String getJson(String url){
        return HttpClientHelper.get(url);
    }

    public JsonObject getJsonObj(String json){
        return JsonParser.parseString(json).getAsJsonObject();
    }

    private String buildPlayerUrl(String steamId){
        return "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/"
                + "?key=" + API_KEY
                + "&steamids=" + steamId;
    }

    private String buildGamesUrl(String steamId){
        return "https://api.steampowered.com/IPlayerService/GetOwnedGames/v1/"
                + "?key=" + API_KEY
                + "&steamid=" + steamId
                + "&include_played_free_games=true"; // cs can be free
    }

    private String buildSteamIdUrl(String nickname){
        return "https://api.steampowered.com/ISteamUser/ResolveVanityURL/v1/"
                + "?key=" + API_KEY
                + "&vanityurl=" + nickname;
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }
}


