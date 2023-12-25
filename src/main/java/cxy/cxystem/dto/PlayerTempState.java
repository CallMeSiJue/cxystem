package cxy.cxystem.dto;

/**
 * 玩家的体感温度
 */
public class PlayerTempState {
    public int freezeCount = 0;
    public Double feelTemp = 26d;
    public int playerTempStatus = 0;
    public int thirstValue = 20;

    public int hotCount = 0;

    public void reset() {
        this.freezeCount = 0;
        this.feelTemp = 26d;
        this.playerTempStatus = 0;
        this.thirstValue = 20;
        this.hotCount = 0;
    }


    @Override
    public String toString() {
        return "PlayerTempState{" +
                "freezeCount=" + freezeCount +
                ", feelTemp=" + feelTemp +
                ", playerTempStatus=" + playerTempStatus +
                '}';
    }

}
