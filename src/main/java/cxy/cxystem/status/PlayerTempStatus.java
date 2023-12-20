package cxy.cxystem.status;

public enum PlayerTempStatus {
    COMFORTABLE(0, "我感觉很舒服"),
    COOL(1, "我感觉有点冷"),
    HOT(2, "我感觉有点热"),
    VERY_HOT(3, "我感觉快热死了"),
    VERY_COOL(4, "我感觉快冷死了");

    private final int code;

    private final String message;

    PlayerTempStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static PlayerTempStatus getByCode(int code) {
        for (PlayerTempStatus status : PlayerTempStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return COMFORTABLE;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

}
