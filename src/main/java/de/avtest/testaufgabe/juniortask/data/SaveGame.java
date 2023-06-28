package de.avtest.testaufgabe.juniortask.data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SaveGame {
    @Id
    private String uuid;

    private int size;

    private String lastPlayer;

    private String gameBoard;

    public SaveGame() {
    }

    public SaveGame(String uuid, int size, String lastPlayer, String gameBoard) {
        this.uuid = uuid;
        this.size = size;
        this.lastPlayer = lastPlayer;
        this.gameBoard = gameBoard;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getLastPlayer() {
        return lastPlayer;
    }

    public void setLastPlayer(String lastPlayer) {
        this.lastPlayer = lastPlayer;
    }

    public String getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(String gameBoard) {
        this.gameBoard = gameBoard;
    }
}
