package br.com.adoteumpet.Chat;

public class ChatObject {
    private String message;
    private Boolean currentUser;
    private String profileImageUrl;
    public ChatObject(String message, Boolean currentUser, String profileImageUrl){
        this.message = message;
        this.currentUser = currentUser;
        this.profileImageUrl = profileImageUrl;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public Boolean getcurrentUser(){
        return currentUser;
    }

    public void setcurrentUser(Boolean currentUser){
        this.currentUser = currentUser;
    }

    public String getprofileImageUrl(){
        return profileImageUrl;
    }

    public void setprofileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }
}
