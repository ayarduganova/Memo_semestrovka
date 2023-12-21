package itis.semestrovka.memo.protocol;

public class Message {

    public static final String type = "type=";
    public static String createMessage(String typeMessage, String message){
        return type + typeMessage + ";" + message;
    }

}
