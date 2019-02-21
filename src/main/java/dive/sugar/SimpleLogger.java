package dive.sugar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 工具类
 *
 * @author dawn
 */
public class SimpleLogger {

    /**
     * 包装日志方法
     */
    private static final Function<String, String> PACK1 =
            s -> {
                StackTraceElement e = new Throwable().getStackTrace()[3];
                Date now = new Date();
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date())
                        + "." + now.getTime() % 1000
                        + ": " + e.getClassName()
                        + "." + e.getMethodName()
                        + " " + e.getLineNumber()
                        + ": " + s;
            };

    private static final Function<String, String> PACK2 = s -> s;

    /**
     * 是否输出日志
     */
    private boolean log = true;

    /**
     * 正常日志输出
     */
    private Consumer<String> info = System.out::println;

    /**
     * 异常日志输出
     */
    private Consumer<String> error = System.err::println;

    /**
     * 包装
     */
    private Function<String, String> infoPack = PACK1;

    /**
     * 包装
     */
    private Function<String, String> errorPack = PACK1;

    SimpleLogger() {}

    /**
     * 设置是否输出日志
     * @param log 是否输出日志
     */
    void log(boolean log) {
        this.log = log;
    }

    /**
     * 设置普通信息输出
     * @param info 普通信息输出方法
     */
    void info(Consumer<String> info) {
        if (null != info) {
            this.info = info;
            this.infoPack = PACK2;
        }
    }

    /**
     * 设置错误信息输出
     * @param error 错误信息输出方法
     */
    void error(Consumer<String> error) {
        if (null != error) {
            this.error = error;
            this.errorPack = PACK2;
        }
    }

    /**
     * 统一日志输出方法
     * @param info 是否普通输出
     * @param format 格式
     * @param param 替换参数
     */
    void log(boolean info, String format, Object... param) {
        if (!this.log) {
            return;
        }
        if (null != format && null != param) {
            for (Object p : param) {
                format = format.replaceFirst("\\{}", null != p ? p.toString() : "null");
            }
        }
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (info) {
            this.info.accept(this.infoPack.apply(format));
        } else {
            this.error.accept(this.errorPack.apply(format));
        }
    }

    /**
     * 普通信息输出
     * @param message 信息
     * @param param 参数
     */
    public void info(String message, Object... param) {
        this.log(true, message, param);
    }

    /**
     * 错误信息输出
     * @param message 信息
     * @param param 参数
     */
    public void error(String message, Object... param) {
        this.log(false, message, param);
    }

}
