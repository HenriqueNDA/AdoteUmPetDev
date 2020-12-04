package br.com.adoteumpet.Main.Cards;

public class cards {
    private String sUserId;
    private String sUsername;
    private String profileImageUrl;

    public cards(String sUserId, String sUsername, String profileImageUrl){
        this.sUserId = sUserId;
        this.sUsername = sUsername;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId(){
        return sUserId;
    }

    public void setUserID(String userID){
        this.sUserId = sUserId;
    }

    public String getNome(){
        return sUsername;
    }

    public void setNome(String nome){
        this.sUsername = sUsername;
    }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

}
