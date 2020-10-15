package br.com.adoteumpet;

public class cards {
    private String userId;
    private String nome;

    public cards(String userId, String nome){
        this.userId = userId;
        this.nome = nome;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserID(String userID){

        this.userId = userId;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }
}
