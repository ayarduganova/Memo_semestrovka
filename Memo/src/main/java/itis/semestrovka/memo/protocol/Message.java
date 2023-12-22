package itis.semestrovka.memo.protocol;

public class Message {

    public static final String type = "type=";
    public static final String room = "room=";
    public static String createMessage(String typeMessage, String message){
        return type + typeMessage + ";" + message;
    }

    public static String createMessageToRoom(String typeMessage, String roomMessage, String message){
        return type + typeMessage + ";" + room + roomMessage + ";" + message;
    }

}
