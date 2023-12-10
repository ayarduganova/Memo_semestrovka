package itis.semestrovka.memo.server;

import itis.semestrovka.memo.client.Client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Room {

    private String name;
    private Integer maxSize;
    private Set<Client> clients = new HashSet<>();

    public Room(String name, Integer maxSize) {
        this.name = name;
        this.maxSize = maxSize;
    }

//    public static void setClient(Client client){
//        clients.add(client);
//    }
//
//    public static Set<Client> getClients(){
//        return clients;
//    }


    public Set<Client> getClients() {
        return clients;
    }

    public void setClient(Client client) {
        this.clients.add(client);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }


    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()){
            return false;
        }

        Room room = (Room) obj;

        return getName().equals(room.getName());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
    //    @Override
//    public String toString() {
//        return getName();
//    }

}
