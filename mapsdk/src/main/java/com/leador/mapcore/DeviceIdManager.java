package com.leador.mapcore;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.unistrong.api.mapcore.util.MD5;

/**
 * desciption:获取硬件信息的管理类,比如:手机的串号
 *
 */
public class DeviceIdManager {


    private  DeviceIdManager(){}

    public static byte [] getProductSN(String rootPath){
        try {
            String fileName = rootPath + "/product.sn";
            File file = new File(fileName);
            if(file.exists()){
                InputStream in = new FileInputStream(file);
                byte bytes[] = new byte[(int) file.length()];
                in.read(bytes);
                in.close();
                return bytes;
            }
        }catch (Exception ee){

        }
        return null;
    }
   public static void saveDeviceID(String rootPath,String deviceid){
        String fileName = rootPath+"/deviceid.dat";
        try{
            File file = new File(fileName);
            if(file.exists()){
                return ;
            }else{
                file.createNewFile();
            }
            OutputStream out = new FileOutputStream(file);
            out.write((deviceid).getBytes());
            out.flush();
            out.close();
        }catch (Exception ex){

        }
    }
    /**
     * @Description: 获取设备的硬件号
     *
     * @return : 返回的硬件号
     */
    public static String getDeviceID(Context context)
    {
        try{

            String strDeviceID="";
            if(strDeviceID != null &&!strDeviceID.equals("")){
                return strDeviceID;
            }

        if(context != null)
        {
             //获取IMEI号或MEID、ESN号
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null){
                strDeviceID = tm.getDeviceId();
            }
            if (strDeviceID == null || strDeviceID.equals("")){
                int mmcindex[] = getMMCList();
                if(mmcindex!=null){
                    for(int i=0;i<mmcindex.length;i++){
                        String cid = getSDCardCID(i);
                        if(cid!=null&&cid.length()>5){
                            strDeviceID = cid;
                            break;
                        }
                    }
                }
            }
            if(strDeviceID==null || strDeviceID.equals("")){
                strDeviceID = getCPUSerial();
            }
        }
            if(strDeviceID!=null&&strDeviceID.length()>0) {
                strDeviceID = MD5.encryptString(strDeviceID);
            }
            return strDeviceID +";";
        }catch (Exception ex){
            return "";
        }
    }

    private static String getCPUSerial() {
        String str = "", strCPU = "", cpuAddress = "0000000000000000";
        try {
            //读取CPU信息
            Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            //查找CPU序列号
            str = input.readLine();
            while(str != null) {
                //查找到序列号所在行
                if (str.indexOf("Serial") > -1) {
                    //提取序列号
                    strCPU = str.substring(str.indexOf(":") + 1,
                            str.length());
                    //去空格
                    cpuAddress = strCPU.trim();
                    break;
                }
                str = input.readLine();
            }
        } catch (IOException ex) {
            //赋予默认值
            ex.printStackTrace();
        }
        return cpuAddress;
    }
    /**
     * 获取SD卡索引列表。
     * @return 索引列表
     */
    public static int[] getMMCList()
    {
        File[] files = new File("/sys/class/mmc_host").listFiles(new FileFilter()
        {
            public boolean accept(File pathname) {
                if (pathname.isDirectory())
                {
                    String filename = pathname.getName();
                    if (filename.substring(0, 3).equalsIgnoreCase("mmc"))
                    {
                        int nIndex = Integer.parseInt(filename.substring(3));
                        String mmcType = getMMCType(nIndex);

                        return ((mmcType == null) || (!(mmcType.equalsIgnoreCase("SDIO"))));
                    }

                }

                return false;
            }

        });
        if ((files == null) || (files.length < 1))
            return null;
        int[] results = new int[files.length];
        for (int i = 0; i < files.length; ++i)
        {
            results[i] = Integer.parseInt(files[i].getName().substring(3));
        }
        return results;
    }
    private static String getMMCType(int nIndex)
    {
        String mmcType = null;
        FileReader fileReader = null;
        LineNumberReader input = null;
        try {
            String mmcPath = "/sys/class/mmc_host/mmc" + nIndex;
            final String filter = "mmc" + nIndex + ":";
            File[] files = new File(mmcPath).listFiles(new FileFilter()
            {
                public boolean accept(File pathname) {
                    if (!(pathname.isDirectory())){
                        return false;
                    }
                    String filename = pathname.getName();
                    return (filename.startsWith(filter));
                }
            });
            if (files.length < 1) {
                return null;
            }

            File cidFile = new File(files[0].getPath() + "/type");
            if (!(cidFile.exists()))
                return null;
            fileReader = new FileReader(cidFile);
            input = new LineNumberReader(fileReader);
            mmcType = input.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            try{
                if(fileReader!=null){
                   fileReader.close();
                }
                if(input!=null){
                    input.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return mmcType;
    }

    /**
     * 获取某个SD卡的ID。
     * @param nIndex SD卡索引。
     * @return SD卡的ID。
     */
    private static String getSDCardCID(int nIndex)
    {
        String sdcardCID = null;
        LineNumberReader input = null;
        FileReader fileReader = null;
        try {
            String mmcPath = "/sys/class/mmc_host/mmc" + nIndex;
            final String filter = "mmc" + nIndex + ":";
            File[] files = new File(mmcPath).listFiles(new FileFilter()
            {
                public boolean accept(File pathname) {
                    if (pathname.isDirectory()){
                        String filename = pathname.getName();
                        return filename.startsWith(filter);
                    }else{
                        return false;
                    }
                }
            });
            if (files.length < 1) {
                return null;
            }

            File cidFile = new File(files[0].getPath() + "/cid");
            if (!(cidFile.exists()))
                return null;
            fileReader = new FileReader(cidFile);
            input = new LineNumberReader(fileReader);
            sdcardCID = input.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            try{
                if(fileReader!=null){
                    fileReader.close();
                }
                if(input!=null){
                    input.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return sdcardCID;
    }

}
