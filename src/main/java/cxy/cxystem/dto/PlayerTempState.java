package cxy.cxystem.dto;

/**
 * 玩家的体感温度
 */
public class PlayerTempState {
    public int freezeCount = 0;
    public Double feelTemp = 26d;
    public int playerTempStatus = 0;

    @Override
    public String toString() {
        return "PlayerTempState{" +
                "freezeCount=" + freezeCount +
                ", feelTemp=" + feelTemp +
                ", playerTempStatus=" + playerTempStatus +
                '}';
    }

    public PlayerStateDTO toDto() {
        PlayerStateDTO dto = new PlayerStateDTO();


        dto.setFeelTemp(this.feelTemp);
        dto.setPlayerTempStatus(this.playerTempStatus);
        dto.setFreezeCount(this.freezeCount);
        //

        return dto;
    }


    public void reset() {
        this.freezeCount = 0;
        this.feelTemp = 26d;
        this.playerTempStatus = 0;
    }
}
