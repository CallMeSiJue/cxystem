package cxy.cxystem.dto;

public class PlayerStateDTO {
    public Double feelTemp;

    public int playerTempStatus;

    public PlayerStateDTO() {
        this.feelTemp = 26d;
        this.playerTempStatus = 0;
    }


    public Double getFeelTemp() {
        return feelTemp;
    }

    public void setFeelTemp(Double feelTemp) {
        this.feelTemp = feelTemp;
    }

    public int getPlayerTempStatus() {
        return playerTempStatus;
    }

    public void setPlayerTempStatus(int playerTempStatus) {
        this.playerTempStatus = playerTempStatus;
    }
}
