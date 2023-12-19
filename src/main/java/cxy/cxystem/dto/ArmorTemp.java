package cxy.cxystem.dto;

public class ArmorTemp {
    private String name;

    /**
     * 适应温度
     */
    private Double adaptTemperature;

    /**
     * 装备影响的 适温时间。
     */
    private Double adaptTickAdd;

    public ArmorTemp(String name, Double adaptTemperature, Double adaptTickAdd) {
        this.name = name;
        this.adaptTemperature = adaptTemperature;
        this.adaptTickAdd = adaptTickAdd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAdaptTemperature() {
        return adaptTemperature;
    }

    public void setAdaptTemperature(Double adaptTemperature) {
        this.adaptTemperature = adaptTemperature;
    }

    public Double getAdaptTickAdd() {
        return adaptTickAdd;
    }

    public void setAdaptTickAdd(Double adaptTickAdd) {
        this.adaptTickAdd = adaptTickAdd;
    }
}
