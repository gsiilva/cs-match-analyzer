public class Main {

    public static void main(String[] args) throws Exception {

        String customUrlNick = "";

        SteamService steamService = new SteamService();

        String steamId = steamService.getSteamIdFromNickname(customUrlNick);

        Player player = steamService.getPlayerInfo(steamId);

        int csHours = steamService.getCSHours(steamId);

        System.out.println("Nome: " + player.getName());
        System.out.println("Horas em CS: " + csHours);
    }
}
