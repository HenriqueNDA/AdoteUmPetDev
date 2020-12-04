package br.com.adoteumpet.Petchs;

public class PetchObject {
    private String userId;
    private String sName;
    private String sResponsiblePetOtherUser;
    private String profileImageUrl;
    public PetchObject(String userId, String sName, String sResponsiblePetOtherUser, String profileImageUrl){
        this.userId = userId;
        this.sName = sName;
        this.sResponsiblePetOtherUser = sResponsiblePetOtherUser;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserID(String userID){
        this.userId = userId;
    }

    public String getName(){
        return sName;
    }

    public void setName(String sName){
        this.sName = sName;
    }

    public String getprofileImageUrl(){
        return profileImageUrl;
    }

    public void setprofileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

    public String getResponsiblePetOtherUser(){
        return sResponsiblePetOtherUser;
    }

    public void setResponsiblePetOtherUser(String sResponsiblePetOtherUser){
        this.sResponsiblePetOtherUser = sResponsiblePetOtherUser;
    }
}
