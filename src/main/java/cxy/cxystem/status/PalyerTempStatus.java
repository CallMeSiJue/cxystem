package cxy.cxystem.status;

public enum PalyerTempStatus {
    COMFORTABLE("我感觉很舒服"),
    COOL("我感觉有点冷"),
    HOT("我感觉有点热"),
    VERY_HOT("我感觉快热死了"),
    VERY_COOL("我感觉快冷死了");

    private final String message;

    PalyerTempStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
