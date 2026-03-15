public class Player {

    private String name;
    private String profileUrl;
    private String avatar;

    public Player(String name, String profileUrl, String avatar){
        this.name = name;
        this.profileUrl = profileUrl;
        this.avatar = avatar;
    }

    public String getName(){
        return name;
    }

    public String getProfileUrl(){
        return profileUrl;
    }

    public String getAvatar(){
        return avatar;
    }

}
