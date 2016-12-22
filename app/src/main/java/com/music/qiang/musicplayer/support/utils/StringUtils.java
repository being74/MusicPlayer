package com.music.qiang.musicplayer.support.utils;

import android.content.Context;

import com.music.qiang.musicplayer.application.MyApp;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 帮助类
 *
 * Created by xuqiang on 2016/12/22.
 */
public class StringUtils {

    /**
     * 判断是否为空
     *
     * @param text 待判定字符串
     *
     * @return true:是空的，false:不是空的
     */
    public static boolean isNullOrEmpty(String text) {
        if (text == null || "".equals(text.trim()) || text.trim().length() == 0
                || "null".equals(text.trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串数组texts中是否有一个字符串为空
     *
     * @param texts 待判定字符串
     * @return 如果字符串数组texts中有一个为空或texts为空，返回true;otherwise return false;
     */
    public static boolean isEmpty(String... texts) {
        if (texts == null || texts.length == 0) {
            return true;
        }
        for (String text : texts) {
            if (text == null || "".equals(text.trim()) || text.trim().length() == 0
                    || "null".equals(text.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @see #dip2px(float)
     */
    public static int dip2px(Context context, float dpValue) {
        if (context == null) {
            context = MyApp.getSelf();
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dip2px(float dpValue) {
        final float scale = MyApp.getSelf().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = MyApp.getSelf().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     */
    public static int sp2px(float spValue) {
        final float scale = MyApp.getSelf().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * 电话号码验证
     *
     * @param phoneNumber 手机号码
     * @return
     */
    public static boolean validatePhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern
                .compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = pattern.matcher(phoneNumber);
        return m.matches();
    }

    /**
     * 邮箱验证
     *
     * @param mail 邮箱
     * @return
     */
    public static boolean validateEmail(String mail) {
        Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = pattern.matcher(mail);
        return m.matches();
    }

    /**
     * 验证输入的身份证号是否符合格式要求
     *
     * @param IDNum 身份证号
     * @return 符合国家的格式要求为 true;otherwise,false;
     */
    public static boolean validateIDcard(String IDNum) {
        String id_regEx1 = "^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|(3[0-1]))\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|(3[0-1]))\\d{3}[0-9Xx])$";
        Pattern pattern = Pattern.compile(id_regEx1);
        Matcher m = pattern.matcher(IDNum);
        return m.matches();
    }

    /**
     * 验证一个字符串是否能解析成整数
     *
     * @param numberStr
     * @return
     */
    public static boolean canParseInt(String numberStr) {
        try {
            Integer.parseInt(numberStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * 验证一个字符串是否能解析成双精度浮点数
     *
     * @param numberStr
     * @return
     */
    public static boolean canParseDouble(String numberStr) {
        try {
            Double.parseDouble(numberStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * 验证一个字符串是否能解析成浮点数
     *
     * @param numberStr
     * @return
     */
    public static boolean canParseFloat(String numberStr) {
        try {
            Float.parseFloat(numberStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * 验证一个字符串是否能解析成长整型数
     *
     * @param numberStr
     * @return
     */
    public static boolean canParseLong(String numberStr) {
        try {
            Long.parseLong(numberStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * 字符串 转换 double
     *
     * @param s 需要转化的数据
     * @return 转化后的double数据
     */

    public static double parseDouble(String s) {
        String ss = "";
        if (!isNullOrEmpty(s) && s.contains(",")) {
            int len = s.split(",").length;
            for (int i = 0; i < len; i++) {
                ss = ss + s.split(",")[i];
            }
            return canParseDouble(ss) ? Double.parseDouble(ss) : 0;
        }
        return canParseDouble(s) ? Double.parseDouble(s) : 0;
    }

    /**
     * @param d   double数据
     * @param len 小数点位数
     * @return
     */
    public static String formatNumber(double d, int len) {
        try {
            DecimalFormat df = null;
            if (len == 0) {
                df = new DecimalFormat("###0");
            } else {
                String s = "#,##0.";
                String ss = "";
                for (int i = 0; i < len; i++) {
                    s = s + "0";
                    ss = ss + "0";
                }
                df = new DecimalFormat(s);
                if (df.format(d).split("\\.")[1].equals(ss)) {
                    return df.format(d).split("\\.")[0];
                }
            }
            return df.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 保留小数点后一位
     */
    public static String formatNumber(double d) {
        try {
            DecimalFormat df = new DecimalFormat("#,##0.0");
            return df.format(d);
        } catch (Exception e) {
        }
        return "";
    }

}
