package net.vpnsdk.vpn.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static boolean isEmpty(String url) {
        if (null == url || "".equals(url))
            return true;
        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);
            if (c != ' ' && c != '\t' && c != '\n' && c != '\r') {
                return false;
            }
        }
        return true;
    }


    public static String Date2Str(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }

    public static boolean isURL(String url) {
        if (isEmpty(url)) {
            return false;
        }
        //not support parameters
        String regex = "^((https|http|ftp|rtsp|mms)?://)?"//http or https
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // 199.194.52.184
                + "|" //
                + "([0-9a-z_!~*'()-]+\\.)*" // www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." //baidu. or sina.
                + "[a-z]{2,6})" // .com or .museum
                + "(:[0-9]{1,4})?" // port
                + "((/?)|" // '/' isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url.toLowerCase(Locale.getDefault()));
        try {
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidIP(String addr) {
        //For decimal IP,such as 192.168.0.1
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        String rexp = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        boolean ipAddress = mat.matches();
        return ipAddress;
    }

    public static String integerToStringIP(int ip) {
        return (ip & 0xFF) + "." +

                ((ip >> 8) & 0xFF) + "." +

                ((ip >> 16) & 0xFF) + "." +

                ((ip >> 24) & 0xFF);
    }

    public static Long StringToIntIP(String addr) {
        String[] addrArray = addr.split("\\.");

        long num = 0;

        for (int i = 0; i < addrArray.length; i++) {

            int power = 3 - i;

            num += ((Integer.parseInt(addrArray[i]) % 256 * Math.pow(256, power)));

        }

        return num;

    }

}
