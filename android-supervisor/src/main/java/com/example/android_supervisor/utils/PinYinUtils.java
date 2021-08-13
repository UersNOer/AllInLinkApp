package com.example.android_supervisor.utils;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;

/**
 * @author wujie
 */
public class PinYinUtils {
    private static HanyuPinyinOutputFormat format;

    static {
        format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    public static String getPinyinInitial(String source) {
        if (source == null) {
            throw new IllegalArgumentException();
        }
        if (source.length() == 0) {
            throw new IllegalArgumentException();
        }
        char c = source.charAt(0);
        String pinyin = getPinyin(c);
        c = pinyin.charAt(0);
        if (c >= 0x41 && c <= 0x5a) {
            return Character.toString(c);
        } else {
            return "#";
        }
    }

    public static String getPinyin(String source) {
        if (source == null) {
            throw new IllegalArgumentException();
        }
        char[] chars = source.toCharArray();
        ArrayList<String> pyWords = new ArrayList<>();
        for (char c : chars) {
            pyWords.add(getPinyin(c));
        }
        String pinyin = TextUtils.join(" ", pyWords);
        return pinyin;
    }

    public static String getPinyin(char c) {
        String pinyin = null;
        try {
            String[] strArray = PinyinHelper.toHanyuPinyinStringArray(c, format);
            if (strArray != null && strArray.length > 0) {
                pinyin = strArray[0];
            }
        } catch (BadHanyuPinyinOutputFormatCombination combination) {
            combination.printStackTrace();
        }
        if (pinyin == null) {
            pinyin = Character.toString(c).toUpperCase();
        }
        return pinyin;
    }
}
