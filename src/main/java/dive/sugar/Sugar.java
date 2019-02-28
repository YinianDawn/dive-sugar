package dive.sugar;

import dive.sugar.annotate.TRANSIENT;
import dive.sugar.model.Base;
import dive.sugar.model.Column;
import dive.sugar.model.Table;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Consumer;

/**
 * 对外提供的工具
 *
 * @author dawn
 */
public class Sugar {

    private static final String AUTO = "recreate|create|update";

    /**
     * 日志
     */
    private SimpleLogger log;

    /**
     * 数据库驱动
     */
    private String driver = "com.mysql.jdbc.Driver";
    /**
     * 数据库地址
     */
    private String url = "jdbc:mysql:///test?useSSL=true";
    /**
     * 用户名
     */
    private String username = "root";
    /**
     * 密码
     */
    private String password = "123456";

    /**
     * 是否需要注解才构建
     */
    private boolean annotate = false;
    /**
     * 是否从父类继承列属性
     */
    private boolean extend = false;
    /**
     * 是否驼峰命名
     * false 会将 大写字母变小写加下划线
     */
    private boolean camel = true;
    /**
     * 表前缀
     */
    private String prefix = "";
    /**
     * 是否自动设置第一个列为主键
     */
    private boolean primary = true;
    /**
     * 是否自增
     */
    private boolean increment = true;
    /**
     * 默认引擎
     */
    private String engine;
    /**
     * 默认字符集
     */
    private String charset = "utf8mb4";
    /**
     * 默认排序方式
     */
    private String collate = "utf8mb4_general_ci";
    /**
     * 构建方式
     * recreate 总是删除重建
     * create 没有的表才会重建
     * update 只更新表结构，不重建
     */
    private String auto = "create";

    /**
     * 某一类型的默认列类型
     */
    private Map<Class, Column> omitByClass = new HashMap<>();
    /**
     * 某一数据类型的默认列类型
     */
    private Map<String, Column> omitByType = new HashMap<>();

    /**
     * 忽略不用建表的类
     */
    private Set<Class> deleted = new HashSet<>();

    /**
     * 继承的类信息
     */
    private HashMap<Class, Class> extendInfo = new HashMap<>();

    /**
     * 不继承的表
     */
    private Set<Class> noExtend = new HashSet<>();

    /**
     * 若是新建表，需要插入的对象
     */
    private HashMap<Class, List<Object>> insert = new HashMap<>();


    /**
     * 候选核对的表信息
     */
    private HashMap<Class, Table> prepare = new HashMap<>();

    /**
     * 准备核对的表信息
     */
    private List<Table> ready = new ArrayList<>();



    private Sugar() {
        this.log = new SimpleLogger();
        Base.log = this.log;
    }

    /**
     * 构建实例对象
     * @return 本实例
     */
    public static Sugar build() {
        return new Sugar();
    }

    /**
     * 是否输出日志，默认输出
     * @param log 是否输出日志
     * @return 本实例
     */
    public Sugar log(boolean log) {
        this.log.log(log);
        return this;
    }

    /**
     * 普通日志输出方式
     * @param info 输出函数
     * @return 本实例
     */
    public Sugar info(Consumer<String> info) {
        this.log.info(info);
        return this;
    }

    /**
     * 错误日志输出方式
     * @param error 输出函数
     * @return 本实例
     */
    public Sugar error(Consumer<String> error) {
        this.log.error(error);
        return this;
    }

    /**
     * 数据库连接信息
     * @param driver 连接驱动
     * @param url 数据库地址
     * @param username 用户名
     * @param password 密码
     * @return 本实例
     */
    public Sugar connect(String driver, String url, String username, String password) {
        if (null != driver && !driver.isEmpty()) {
            this.driver = driver;
        }
        this.url = url;
        this.username = username;
        this.password = password;
        return this;
    }

    /**
     * 数据块连接信息
     * @param url 数据库地址
     * @param username 用户名
     * @param password 密码
     * @return 本实例
     */
    public Sugar connect(String url, String username, String password) {
        return this.connect(null, url, username, password);
    }

    /**
     * 是否必须有注解才管理表，默认不需要
     * @param annotate 是否需要注解
     * @return 本实例
     */
    public Sugar annotate(boolean annotate) {
        this.annotate = annotate;
        return this;
    }

    /**
     * 是否继承父类属性
     * @param extend 是否继承父类属性
     * @return 本实例
     */
    public Sugar extend(boolean extend) {
        this.extend = extend;
        return this;
    }

    /**
     * 是否驼峰命名，false会用下划线
     * @param camel 是否驼峰命名
     * @return 本实例
     */
    public Sugar camel(boolean camel) {
        this.camel = camel;
        return this;
    }

    /**
     * 表统一前缀
     * @param prefix 前缀
     * @return 本实例
     */
    public Sugar prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * 是否自动设置第一个属性为默认主键
     * @param primary 是否主键
     * @return 本实例
     */
    public Sugar primary(boolean primary) {
        this.primary = primary;
        return this;
    }

    /**
     * 主键若为数字类型，是否默认自增
     * @param increment 是否自增
     * @return 本实例
     */
    public Sugar increment(boolean increment) {
        this.increment = increment;
        return this;
    }

    /**
     * 默认存储引擎
     * @param engine 存储引擎
     * @return 本实例
     */
    public Sugar engine(String engine) {
        this.engine = engine;
        return this;
    }

    /**
     * 默认表字符集
     * @param charset 字符集
     * @return 本实例
     */
    public Sugar charset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * 默认表排序
     * @param collate 表排序
     * @return 本实例
     */
    public Sugar collate(String collate) {
        this.collate = collate;
        return this;
    }

    /**
     * 表管理方式
     * recreate 总是删除重建
     * create 没有的表才会重建
     * update 只更新表结构，不重建
     * @param auto 管理方式
     * @return 本实例
     */
    public Sugar auto(String auto) {
        Objects.requireNonNull(auto, "auto cannot be null");
        if (!auto.matches(AUTO)) {
            this.log.error("the value of 'auto' is {}, can not recognise.", auto);
            return this;
        }
        this.auto = auto;
        return this;
    }

    /**
     * 根据类设置默认列类型
     * @param type 类
     * @param column 列类型
     * @return 本实例
     */
    public Sugar omit(Class type, Column column) {
        this.omitByClass.put(type, column);
        return this;
    }

    /**
     * 根据数据类型设置默认列类型
     * @param type 数据类型
     * @param column 列类型
     * @return 本实例
     */
    public Sugar omit(String type, Column column) {
        this.omitByType.put(type.toUpperCase(), column);
        return this;
    }

    /**
     * 忽略的类
     * @param table 类
     * @return 本实例
     */
    public Sugar delete(Class table) {
        this.deleted.add(table);
        return this;
    }

    /**
     * 某一表的继承
     * @param table 表
     * @param parent 继承属性
     * @return 本实例
     */
    public Sugar extend(Class table, Class parent) {
        this.extendInfo.put(table, parent);
        return this;
    }

    /**
     * 取消继承
     * @param table 表
     * @return 本实例
     */
    public Sugar noExtend(Class table) {
        this.noExtend.add(table);
        return this;
    }

    /**
     * 初始化对象
     * @param table 表
     * @param insert 插入对象
     * @return 本实例
     */
    public Sugar initial(Class table, Object insert) {
        Objects.requireNonNull(table, "table can not be null");
        Objects.requireNonNull(insert, "insert object can not be null");
        List<Object> objects = this.insert.computeIfAbsent(table, t -> new LinkedList<>());
        objects.add(insert);
        return this;
    }

    /**
     * 将一个类加入准备池
     * @param table 表
     */
    private void prepare(Class<?> table) {
        if (!table.isAnnotationPresent(TRANSIENT.class)
                && !this.deleted.contains(table)) {
            prepare.put(table, new Table(this, table));
        } else {
            this.log.error("class is transient or deleted: {}", table);
        }
    }

    /**
     * 将多个表加入准备
     * @param tables 多个表
     * @return 本实例
     */
    public Sugar prepare(Class<?>... tables) {
        for (Class table : tables) {
            this.prepare(table);
        }
        return this;
    }

    /**
     * 根据全类名添加表
     * @param className 全类名
     */
    private void prepare(String className) {
        try {
            this.prepare(Class.forName(className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            this.log.error("can not find class: {}", className);
        }
    }

    /**
     * 将某包下所有类加入准备池
     * @param packageName 包名
     * @return 本实例
     */
    public Sugar prepareAll(String packageName) {
        String packagePath = packageName.replace(".", "/");
        Enumeration<URL> urls;
        try {
            urls = Thread.currentThread().getContextClassLoader().getResources(packagePath);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String path = URLDecoder.decode(url.getFile(), "UTF-8");
                    File[] files = new File(path).listFiles();
                    if (null != files) {
                        for (File file : files){
                            String fileName = file.getName();
                            if(fileName.endsWith(".class")){
                                String className = (!packageName.isEmpty() ? packageName + "." : "")
                                        + fileName.substring(0, fileName.length() - 6);
                                prepare(className);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 将指定类的包下所有类加入准备池
     * @param tables 指定类
     * @return 本实例
     */
    public Sugar prepareAll(Class<?>... tables) {
        for (Class clazz : tables) {
            String clazzName = clazz.getName();
            String packageName = clazzName
                    .substring(0, clazzName.lastIndexOf("."));
            this.prepareAll(packageName);
        }
        return this;
    }

    /**
     * 检查表
     * @return 本实例
     */
    public Sugar check() {
        this.log.log("recreate".equals(this.auto),
                "the value of 'auto' is {}", this.auto);
        try {
            // 加载类驱动
            Class.forName(this.driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("load driver failed: " + this.driver);
        }
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()){
            for (Class clazz : this.prepare.keySet()) {
                Table table = this.prepare.get(clazz);
                if (table.isValid()) {
                    // 设置初始插入自
                    table.initial(insert.get(clazz));
                    ready.add(table);
                }
            }
            this.check(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    private void check(Statement stmt) {
        this.log.info("----------- build -----------");
        for (Table table : ready) {
            table.build(stmt);
        }
        this.log.info("----------  index  ----------");
        for (Table table : ready) {
            table.index(stmt);
        }
        this.log.info("---------  insert  ---------");
        for (Table table : ready) {
            table.initial(stmt);
        }
        this.log.info("--------  completed  --------");
    }

    public Map<Class, Column> getOmitByClass() {
        return this.omitByClass;
    }

    public Map<String, Column> getOmitByType() {
        return this.omitByType;
    }

    public boolean isCamel() {
        return camel;
    }

    public boolean isAnnotate() {
        return annotate;
    }

    public String getAuto() {
        return auto;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getEngine() {
        return engine;
    }

    public String getCharset() {
        return charset;
    }

    public String getCollate() {
        return collate;
    }

    public Class parent(Class table) {
        if (!this.extend || this.noExtend.contains(table)) {
            return null;
        }
        Class parent = this.extendInfo.get(table);
        if (null == parent) {
            parent = table.getSuperclass();
        }
        if (parent == Object.class) {
            return null;
        }
        return parent;
    }

    public boolean isPrimary() {
        return primary;
    }

    public boolean isIncrement() {
        return increment;
    }
}
