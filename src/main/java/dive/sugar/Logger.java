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
class Logger {

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
    private Consumer<String> error = System.out::println;

    /**
     * 包装
     */
    private Function<String, String> infoPack = PACK1;

    /**
     * 包装
     */
    private Function<String, String> errorPack = PACK1;

    Logger() {}

    /**
     * 设置是否输出日志
     * @param log 是否输出日志
     */
    void log(boolean log) {
        this.log = log;
    }

    void info(Consumer<String> info) {
        if (null != info) {
            this.info = info;
            this.infoPack = PACK2;
        }
    }

    void error(Consumer<String> error) {
        if (null != error) {
            this.error = error;
            this.errorPack = PACK2;
        }
    }

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

    public void info(String message, Object... param) {
        this.log(true, message, param);
    }

    public void error(String message, Object... param) {
        this.log(false, message, param);
    }

}
