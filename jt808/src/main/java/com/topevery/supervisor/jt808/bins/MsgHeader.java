package com.example.android_supervisor.jt808.bins;

import android.util.Log;

import com.example.android_supervisor.jt808.utils.BCDUtils;

/**
 * @author wujie
 */
public class MsgHeader extends Binary {
    // 消息ID
    private final Int16 msgId = new Int16();

    // 消息体属性
    private final Int16 msgBodyInfo = new Int16();

    // 终端手机号
    private final VarChar phoneNumber = new VarChar(6);

    // 流水号
    private final Int16 serialId = new Int16();

    public MsgHeader() {
    }

    public MsgHeader(byte[] bytes) {
        super(bytes);
    }

    public int getMsgId() {
        return msgId.get();
    }

    public void setMsgId(int msgId) {
        this.msgId.set(msgId);
    }

    public String getPhoneNumber() {
        byte[] bcdCode = phoneNumber.get();
        return BCDUtils.bcd2Str(bcdCode);
    }

    public void setPhoneNumber(String phoneNumber) {
        byte[] bcdCode = BCDUtils.str2Bcd(phoneNumber);
        this.phoneNumber.set(bcdCode);
    }

    public int getSerialId() {
        return this.serialId.get();
    }

    public void setSerialId(int serialId) {
        this.serialId.set(serialId);
    }

    public void setMsgBodyInfo(int bodyLength, int encryptType, int reservedBits) {
        if (bodyLength >= 1024)
            Log.w("JT808", String.format("The max value of msgLen is 1023, but %d .", bodyLength));

        int hasSubPkg = 0;
        int bodyInfo = (bodyLength & 0x3FF) | ((encryptType << 10) & 0x1C00) | ((hasSubPkg << 13) & 0x2000)
                | ((reservedBits << 14) & 0xC000);

        this.msgBodyInfo.set(bodyInfo & 0xffff);
    }

    public int getMsgBodyLength() {
        return this.msgBodyInfo.get() & 0x3ff;
    }

    public int getEncryptType() {
        return (this.msgBodyInfo.get() & 0x1c00) >> 10;
    }

    public int getReservedBits() {
        return (this.msgBodyInfo.get() & 0xc000) >> 14;
    }
}
