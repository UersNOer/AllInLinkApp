package com.example.android_supervisor.jt808.bins;


import com.example.android_supervisor.jt808.Constants;

/**
 * @author wujie
 */
public class RegMsgBody extends Binary {
    // 省域ID，设备安装车辆所在的省域，省域ID采用GB/T2260中规定的行政区划代码6位中前两位
    private final Int16 provinceId = new Int16();

    // 市县域ID，设备安装车辆所在的市域或县域,市县域ID采用GB/T2260中规定的行 政区划代码6位中后四位
    private final Int16 cityId = new Int16();

    // 制造商ID，终端制造商编码
    private final VarChar manufacturerId = new VarChar(5);

    // 终端型号，此终端型号 由制造商自行定义 位数不足八位的，补空格。
    private final VarChar terminalType = new VarChar(8);

    // 终端ID，由大写字母 和数字组成， 此终端 ID由制造 商自行定义
    private final VarChar terminalId = new VarChar(7);

    /**
     *
     * 车牌颜色(1) 车牌颜色，按照 JT/T415-2006 的 5.4.12 未上牌时，取值为0<br>
     * 0===未上车牌<br>
     * 1===蓝色<br>
     * 2===黄色<br>
     * 3===黑色<br>
     * 4===白色<br>
     * 9===其他
     */
    private final Int8 licensePlateColor = new Int8();

    // 车牌，公安交通管理部门颁发的机动车号牌
    private final VarChar licensePlate = new VarChar(32);

    public RegMsgBody() {
    }

    public RegMsgBody(byte[] bytes) {
        super(bytes);
    }

    public int getProvinceId() {
        return provinceId.get();
    }

    public void setProvinceId(int provinceId) {
        this.provinceId.set(provinceId);
    }

    public int getCityId() {
        return cityId.get();
    }

    public void setCityId(int cityId) {
        this.cityId.set(cityId);
    }

    public String getManufacturerId() {
        return manufacturerId.get(Constants.STRING_ENCODING);
    }

    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId.set(manufacturerId, Constants.STRING_ENCODING);
    }

    public String getTerminalType() {
        return terminalType.get(Constants.STRING_ENCODING);
    }

    public void setTerminalType(String terminalType) {
        this.terminalType.set(terminalType, Constants.STRING_ENCODING);
    }

    public String getTerminalId() {
        return terminalId.get(Constants.STRING_ENCODING);
    }

    public void setTerminalId(String terminalId) {
        this.terminalId.set(terminalId, Constants.STRING_ENCODING);
    }

    public int getLicensePlateColor() {
        return licensePlateColor.get();
    }

    public void setLicensePlateColor(int licensePlateColor) {
        this.licensePlateColor.set(licensePlateColor);
    }

    public String getLicensePlate() {
        return licensePlate.get(Constants.STRING_ENCODING);
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate.set(licensePlate, Constants.STRING_ENCODING);
    }
}
