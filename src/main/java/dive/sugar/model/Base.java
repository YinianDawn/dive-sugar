package dive.sugar.model;

import dive.sugar.SimpleLogger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基本工具
 *
 * @author dawn
 */
public class Base {

    public static SimpleLogger log;

    /**
     * 标识是否有效
     */
    protected boolean valid = false;

    public boolean isValid() {
        return this.valid;
    }

    /**
     * 名称的驼峰检查
     * @param name 省略名称
     * @param camel 是否驼峰
     * @return 处理后名称
     */
    static String name(String name, boolean camel){
        if (camel) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                if ('A' <= c && c <= 'Z') {
                    sb.append((char)(c + 32));
                } else {
                    sb.append(name.substring(i));
                    break;
                }
            }
            return sb.toString();
        }
        StringBuilder sb = new StringBuilder();
        boolean lastCase = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if ('A' <= c && c <= 'Z') {
                sb.append(!lastCase ? "_" : "").append((char)(c + 32));
                lastCase = true;
            } else {
                sb.append(c);
                lastCase = false;
            }
        }
        name = sb.toString();
        if (name.startsWith("_")) {
            name = name.substring(1);
        }
        return name;
    }

    /**
     * 字符串匹配
     * @param content 字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 匹配结果
     */
    protected static String pattern(String content, String prefix, String suffix) {
        if (exist(content) && exist(prefix) && exist(suffix)) {
            Pattern pattern =
                    Pattern.compile("(?:" + prefix + ").+(?:(" + suffix + "))");
            Matcher m = pattern.matcher(content);
            if (m.find()){
                return m.group(0).split(prefix)[1].split(suffix)[0];
            }
        }
        return null;
    }

    /**
     * 子串
     * @param d 定义
     * @param index 跳过长度
     * @return 子串
     */
    protected static String substring(String d, int index) {
        if (!exist(d)) {
            return "";
        }
        return 0 < index && index < d.length() ? d.substring(index) : "";
    }

    /**
     * 截取字符串
     * @param d 定义
     * @param start 开始位置
     * @param end 结束位置
     * @return 子串
     */
    protected static String substring(String d, int start, int end) {
        if (!exist(d)) {
            return "";
        }
        return -1 == end || d.length() < end ?
                d : d.substring(start, end);
    }

    /**
     * 注释是否相同
     * @param c1 原注释
     * @param c2 新注释
     * @return 是否相同
     */
    protected static boolean checkComment(String c1, String c2) {
        if (useful(c1) && !useful(c2)) {
            return false;
        }
        if (!useful(c1) && useful(c2)) {
            return false;
        }
        return !useful(c1) || c1.equals(c2);
    }

    protected static boolean exist(Object check) {
        return null != check;
    }

    protected static boolean isTrue(Boolean check) {
        return null != check && check;
    }

    protected static boolean useful(String check) {
        return null != check && !check.trim().isEmpty();
    }

}
