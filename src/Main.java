import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception { // replace later with try/catch

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the SteamId / Profile Url / Custom Url: ");
        String userInput = scanner.nextLine();

        userInput = extractSteamId(userInput);

        SteamService steamService = new SteamService();

        String steamId = steamService.getSteamIdFromNickname(userInput);

        Player player = steamService.getPlayerInfo(steamId);

        int csHours = steamService.getCSHours(steamId);

        System.out.println("Name: " + player.getName());
        System.out.println("CS hours: " + csHours);
    }

    public static String extractSteamId(String input){

        if(input.startsWith("https://steamcommunity.com/id/")){
            String customId = input.substring(30);

            if(customId.endsWith("/")){
                customId = customId.substring(0, customId.length() - 1);
            }

            System.out.println(customId);
            return customId;
        }
        else if(input.startsWith("https://steamcommunity.com/profiles/")){
            String steamId =  input.substring(36);

            if(steamId.endsWith("/")){
                steamId = steamId.substring(0, steamId.length() - 1);
            }

            System.out.println(steamId);
            return steamId;
        }

        System.out.println(input);
        return input;
    }
}
