package cxy.cxystem.dto;

import net.minecraft.network.PacketByteBuf;

/**
 * 玩家的体感温度
 */
public class PlayerTempState {

    public int freezeCount = 0;
    public Double feelTemp = 26d;
    /**
     * 、
     * 玩家的体感状态
     */
    public int playerTempStatus = 0;
    /**
     * 口渴值
     */
    public int thirstValue = 20;
    /**
     * 多少次发送数据包后 减少一点口渴值
     */
    public int thirstCount = 10;
    public int hotCount = 0;

    public static void readData(PlayerTempState playerData, PacketByteBuf buf) {
        playerData.feelTemp = buf.readDouble();
        playerData.playerTempStatus = buf.readInt();
        playerData.freezeCount = buf.readInt();
        playerData.thirstValue = buf.readInt();
        playerData.hotCount = buf.readInt();
        playerData.thirstCount = buf.readInt();
    }

    public static void writeData(PacketByteBuf buf, PlayerTempState dto) {
        buf.writeDouble(dto.feelTemp);
        buf.writeInt(dto.playerTempStatus);
        buf.writeInt(dto.freezeCount);
        buf.writeInt(dto.thirstValue);
        buf.writeInt(dto.hotCount);
        buf.writeInt(dto.thirstCount);
    }

    /**
     * 有些东西不需要 client传入
     *
     * @param playerData
     * @param buf
     */
    public static void serverReadData(PlayerTempState playerData, PacketByteBuf buf) {


        playerData.freezeCount = buf.readInt();
        playerData.hotCount = buf.readInt();
        playerData.thirstCount = buf.readInt();
    }

    /**
     * 有些东西不需要 client传入
     *
     * @param buf
     */
    public static void clientWriteData(PacketByteBuf buf, PlayerTempState dto) {


        buf.writeInt(dto.freezeCount);
        buf.writeInt(dto.hotCount);
        buf.writeInt(dto.thirstCount);
    }


    public void addThirst(int thirst) {
        this.thirstValue = Math.min(this.thirstValue + thirst, 20);
    }

    public void addFreezeCount(int freezeCount) {
        this.freezeCount = Math.min(this.freezeCount + freezeCount, 200);
    }

    public void addHotCount(int hotCount) {
        this.hotCount = Math.min(this.hotCount + hotCount, 300);
    }

    public boolean fullThirst() {
        return this.thirstValue == 20;
    }

    public void reset() {
        this.freezeCount = 0;
        this.feelTemp = 26d;
        this.playerTempStatus = 0;
        this.thirstValue = 20;
        this.hotCount = 0;
        this.thirstCount = 10;
    }


}
