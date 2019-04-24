package dive.sugar.model;

import dive.sugar.Sugar;
import dive.sugar.annotate.TRANSIENT;
import dive.sugar.annotate.index.INDEX;
import dive.sugar.annotate.index.PRIMARY;
import dive.sugar.annotate.index.UNIQUE;
import dive.sugar.annotate.prop.BINARY;
import dive.sugar.annotate.prop.*;
import dive.sugar.annotate.type.*;
import dive.sugar.model.type.number.Double;
import dive.sugar.model.type.number.Float;
import dive.sugar.model.type.number.*;
import dive.sugar.model.type.string.Enum;
import dive.sugar.model.type.string.*;
import dive.sugar.model.type.time.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * 基本属性类型
 * @author dawn
 */
public class Column extends Base {

    /**
     * 配置
     */
    private Sugar sugar;
    /**
     * 属性
     */
    protected Field field;
    /**
     * 模板
     */
    protected Column model;

    /**
     * 列名
     */
    public String name;
    /**
     * 属性
     */
    public String type;
    /**
     * 值
     */
    public String[] values;
    /**
     * 长度
     */
    public Integer length;
    /**
     * 精度
     */
    public Integer decimals;
    /**
     * 时间精度
     */
    public Integer fsp;

    /**
     * 是否无符号
     */
    public Boolean unsigned;
    /**
     * 是否0填充
     */
    public Boolean zerofill;
    /**
     * 是否二进制
     */
    public Boolean binary;
    /**
     * 字符集
     */
    public String charset;
    /**
     * 排序方式
     */
    public String collate;
    /**
     * 是否非空
     */
    public Boolean notNull ;
    /**
     * 是否允许空
     */
    protected Boolean nullable;
    /**
     * 默认值
     */
    public String defaultValue;
    /**
     * 更新动作
     */
    public String onUpdate;
    /**
     * 自增
     */
    public Boolean increment;
    /**
     * 是否主键
     */
    public Boolean primary;
    /**
     * 注释
     */
    public String comment;

    /**
     * 保存的索引信息
     */
    java.util.List<Key> keys;

    /**
     * 是否从其他列名改名而来
     */
    protected String from;
    /**
     * 位置
     */
    public String place;

    protected Column() {}

    public Column(Field field, Sugar sugar, Column model) {
        this.field = field;
        this.sugar = sugar;
        this.model = model;
        this.init();
    }

    protected void init() {
        if (!this.initName()) {
            return;
        }

        this.initType();

        if (!this.initValues()
                || !this.initLength()
                || !this.initDecimals()
                || !this.initFSP()) {
            return;
        }

        this.initUnsigned();
        this.initZerofill();
        this.initBinary();

        if (!this.initCharset() || !this.initCollate()) {
            return;
        }

        this.initNotNull();
        if (!this.initDefaultValue()) {
            return;
        }
        this.initPrimary();
        if (!this.initNullable()) {
            return;
        }

        if (!this.initOnUpdate()) {
            return;
        }
        this.initIncrement();

        this.initComment();

        this.initFrom();

        this.initKeys();

        valid = true;
    }

    private boolean initName() {
        // from NAME
        NAME nameAnnotation = field.getAnnotation(NAME.class);
        if (exist(nameAnnotation)) {
            this.name = nameAnnotation.value().trim();
            if (!useful(this.name)) {
                log.error("name can not be empty, " +
                        "please check annotation NAME");
                return false;
            }
        }
        // from field name
        if (!useful(this.name)) {
            this.name = Base.name(field.getName(), sugar.isCamel());
        }
        return true;
    }

    protected final void initType() {
        this.type = this.getClass().getSimpleName().toUpperCase();
    }

    protected boolean initValues() { return true; }

    protected boolean initLength() {
        String from = null;
        // from LENGTH
        LENGTH lengthAnnotate = field.getAnnotation(LENGTH.class);
        if (exist(lengthAnnotate)) {
            this.length = lengthAnnotate.value();
            from = "LENGTH";
        } else {
            // from model
            if (exist(this.model) && exist(this.model.length)) {
                this.length = this.model.length;
                from = "Model";
            }
        }
        return this.check(length, from);
    }

    /**
     * 子类进行检查长度是否正确 或 检查时间精度
     * @param length 长度
     * @param from 长度来源
     * @return 长度是否正确
     */
    protected boolean check(Integer length,
                            String from) {
        return false;
    }

    protected boolean initDecimals() {
        String from = null;
        Integer precision = null;
        Integer scale = null;
        // from DECIMALS
        DECIMALS decimalsAnnotate =
                field.getAnnotation(DECIMALS.class);
        if (exist(decimalsAnnotate)) {
            this.decimals = decimalsAnnotate.value();
            from = "DECIMALS";
        } else {
            // from model
            if (exist(this.model) && exist(this.model.decimals)) {
                this.decimals = this.model.decimals;
                from = "Model";
            }
        }
        return this.check(this.decimals, from, precision, scale, d -> this.decimals = d);
    }

    /**
     * 检查精度是否正
     * @param decimal 精度
     * @param from 精度来源
     * @param precision Column精度
     * @param scale Column精度
     * @param consumer 结果赋值
     * @return 精度是否正确
     */
    protected boolean check(Integer decimal, String from,
                            Integer precision, Integer scale,
                            Consumer<Integer> consumer) {
        return false;
    }

    protected boolean initFSP() {
        String from = null;
        // from FSP
        FSP fspAnnotate = field.getAnnotation(FSP.class);
        if (exist(fspAnnotate)) {
            this.fsp = fspAnnotate.value();
            from = "FSP";
        } else {
            // from model
            if (exist(this.model) && exist(this.model.fsp)) {
                this.fsp = this.model.fsp;
                from = "Model";
            }
        }
        return this.check(fsp, from);
    }

    protected void initUnsigned() {
        // from Unsigned
        UNSIGNED unsignedAnnotate = field.getAnnotation(UNSIGNED.class);
        if (exist(unsignedAnnotate)) {
            this.unsigned = true;
        }

        // from model
        if (!exist(this.unsigned) && exist(this.model) && exist(this.model.unsigned)) {
            this.unsigned = this.model.unsigned;
        }
    }

    protected void initZerofill() {
        // from ZEROFILL
        ZEROFILL zerofillAnnotate = field.getAnnotation(ZEROFILL.class);
        if (exist(zerofillAnnotate)) {
            this.zerofill = true;
        }

        // from model
        if (!exist(this.zerofill) && exist(this.model) && exist(this.model.zerofill)) {
            this.zerofill = this.model.zerofill;
        }
    }

    protected void initBinary() {
        // from BINARY
        BINARY binaryAnnotate =
                field.getAnnotation(BINARY.class);
        if (exist(binaryAnnotate)) {
            this.binary = true;
        }

        // from model
        if (!exist(this.binary) && exist(this.model) && exist(this.model.binary)) {
            this.binary = this.model.binary;
        }
    }

    protected boolean initCharset() {
        // from CHARSET
        CHARSET charsetAnnotate = field.getAnnotation(CHARSET.class);
        if (exist(charsetAnnotate)) {
            this.charset = charsetAnnotate.value();
            if (!useful(this.charset)) {
                log.error("charset can not be {}", charset);
                return false;
            }
        }

        // from model
        if (!exist(this.charset) && exist(this.model) && useful(this.model.charset)) {
            this.charset = this.model.charset;
        }

        return true;
    }

    protected boolean initCollate() {
        // from COLLATE
        COLLATE collateAnnotate = field.getAnnotation(COLLATE.class);
        if (exist(collateAnnotate)) {
            this.collate = collateAnnotate.value();
            if (!useful(this.collate)) {
                log.error("collate can not be {}", collate);
                return false;
            }
        }

        // from model
        if (!exist(this.collate) && exist(this.model) && useful(this.model.collate)) {
            this.collate = this.model.collate;
        }

        return true;
    }

    private void initNotNull() {
        // from NOTNULL
        NOTNULL notNullAnnotate = field.getAnnotation(NOTNULL.class);
        if (exist(notNullAnnotate)) {
            notNull = true;
        }

        // from model
        if (!exist(this.notNull) && exist(this.model) && exist(this.model.notNull)) {
            this.notNull = this.model.notNull;
        }
    }

    private boolean initDefaultValue() {
        // from DEFAULT
        DEFAULT defaultAnnotate = field.getAnnotation(DEFAULT.class);
        if (exist(defaultAnnotate)) {
            this.defaultValue = defaultAnnotate.value();
            return this.check(defaultValue, "DEFAULT");
        }

        // from model
        if (exist(this.model) && check(this.model.defaultValue, "Model")) {
            this.defaultValue = this.model.defaultValue;
        }

        return true;
    }

    /**
     * 子类实现默认值的检查
     * @param defaultValue 默认值
     * @param from 默认值来源
     * @return 默认值是否正确
     */
    protected boolean check(String defaultValue,
                            String from) {
        return false;
    }

    private void initPrimary() {
        // from PRIMARY
        PRIMARY primaryAnnotate = field.getAnnotation(PRIMARY.class);
        if (exist(primaryAnnotate)) {
            this.primary = true;
        }

        // from model
        if (!exist(this.primary) && exist(this.model) && exist(this.model.primary)) {
            this.primary = this.model.primary;
        }

        if (isTrue(this.primary)) {
            notNull = true;
        }
    }

    private boolean initNullable() {
        // from NULLABLE
        NULLABLE nullableAnnotate = field.getAnnotation(NULLABLE.class);
        if (exist(nullableAnnotate)) {
            this.nullable = true;
            if (isTrue(this.primary) && !isTrue(this.notNull)) {
                log.error("primary key must be not null");
                return false;
            }
            NOTNULL notNullAnnotate = field.getAnnotation(NOTNULL.class);
            if (!exist(notNullAnnotate)) {
                notNull = false;
            }
        }

        // from model
        if (!exist(this.nullable) && exist(this.model) && exist(this.model.nullable)) {
            this.nullable = this.model.nullable;
            if (isTrue(this.nullable) && isTrue(this.primary) && !isTrue(this.notNull)) {
                log.error("primary key must be not null");
                return false;
            }
        }

        return true;
    }

    protected boolean initOnUpdate() { return true; }

    protected void initIncrement() {
        // from AUTO_INCREMENT
        AUTO_INCREMENT autoIncrementAnnotate =
                field.getAnnotation(AUTO_INCREMENT.class);
        if (exist(autoIncrementAnnotate)) {
            this.increment = true;
        }

        // from model
        if (!exist(this.increment) && exist(this.model)
                && exist(this.model.increment)) {
            this.increment = this.model.increment;
        }
    }

    private void initComment() {
        // from COMMENT
        COMMENT commentAnnotate = field.getAnnotation(COMMENT.class);
        if (exist(commentAnnotate) && useful(commentAnnotate.value().trim())) {
            this.comment = commentAnnotate.value().trim();
        }

        // from model
        if (!exist(this.comment) && exist(this.model) && useful(this.model.comment)) {
            this.comment = this.model.comment;
        }
    }

    private void initFrom() {
        // from FROM
        FROM fromAnnotate = field.getAnnotation(FROM.class);
        if (exist(fromAnnotate) && useful(fromAnnotate.value().trim())) {
            this.from = fromAnnotate.value().trim();
        }
    }

    private void initKeys() {
        this.keys = new java.util.ArrayList<>();
        INDEX[] indices = field.getAnnotationsByType(INDEX.class);
        this.keys.addAll(Key.build(indices, name));
        UNIQUE[] uniques = field.getAnnotationsByType(UNIQUE.class);
        this.keys.addAll(Key.build(uniques, name));
    }

    public Column(String definition) {
        if (!useful(definition)) {
            return;
        }
        String d = definition.trim();
        if (Pattern.matches("^(PRIMARY\\s|UNIQUE\\s|)KEY.*", d)
                || 0 == d.length()) {
            return;
        }
        if (!d.startsWith("`")) {
            log.error("wrong column definition: {}", definition);
            return;
        }
        d = substring(d, 1);
        this.name = d.substring(0, d.indexOf("`"));
        d = substring(d, d.indexOf("`") + 2);
        String typeName = substring(d, 0, d.indexOf(" "));
        String brackets = pattern(typeName, "\\(", "\\)");
        if (useful(brackets)) {
            this.type = typeName.substring(0, typeName.indexOf("(")).toUpperCase();
            if (!this.parseBrackets(brackets)) {
                return;
            }
        } else {
            this.type = typeName.toUpperCase();
        }
        d = substring(d, d.indexOf(" ") + 1);
        if (d.startsWith("unsigned")) {
            this.unsigned = true;
            d = substring(d,9);
        }
        if (d.startsWith("zerofill")) {
            this.zerofill = true;
            d = substring(d, 9);
        }
        if (d.startsWith("binary")) {
            this.binary = true;
            d = substring(d, 7);
        }
        if (d.startsWith("CHARACTER SET")) {
            d = substring(d, 14);
            this.charset =  substring(d, 0, d.indexOf(" "));
            d = substring(d, d.indexOf(" ") + 1);
        }
        if (d.startsWith("COLLATE")) {
            d = substring(d, 8);
            this.collate =  substring(d, 0, d.indexOf(" "));
            d = substring(d, d.indexOf(" ") + 1);
        }
        if (d.startsWith("NOT NULL")) {
            this.notNull = true;
            d = substring(d, 9);
        }
        if (d.startsWith("DEFAULT NULL")) {
            this.nullable = true;
            d = substring(d, 13);
        }
        if (d.startsWith("DEFAULT")) {
            d = substring(d, 8);
            if (d.startsWith("'")) {
                d = substring(d, 1);
                this.defaultValue =  substring(d, 0, d.indexOf("'"));
                d = substring(d, d.indexOf("'") + 2);
            } else {
                this.defaultValue =  substring(d, 0, d.indexOf(" "));
                d = substring(d, d.indexOf(" ") + 1);
            }
        }
        if (d.startsWith("ON UPDATE")) {
            d = substring(d, 10);
            int index = d.indexOf(" ");
            if (-1 == index) {
                this.onUpdate = d;
                d = "";
            } else {
                this.onUpdate = d.substring(0, index);
                d = substring(d, index + 1);
            }
        }
        if (d.startsWith("AUTO_INCREMENT")) {
            this.increment = true;
            d = substring(d, 15);
        }
        if (d.startsWith("COMMENT")) {
            this.comment =  substring(d,
                    d.indexOf("'") + 1, d.lastIndexOf("'"));
        }

        this.valid = true;
    }

    /**
     * 解析定义里括号内的内容
     * @param brackets 括号内容
     * @return 是否解析完成
     */
    private boolean parseBrackets(String brackets) {
        try {
            switch (this.type) {
                case "BOOLEAN" : break;
                case "BIT" :
                case "TINYINT" :
                case "SMALLINT" :
                case "MEDIUMINT" :
                case "INT" :
                case "INTEGER" :
                case "BIGINT" :
                    this.length = Integer.parseInt(brackets);
                    break;
                case "REAL" :
                case "DOUBLE" :
                case "FLOAT" :
                case "DECIMAL" :
                case "NUMERIC" :
                    String[] s = brackets.split(",");
                    this.length = Integer.parseInt(s[0]);
                    if (1 < s.length) {
                        this.decimals = Integer.parseInt(s[1]);
                    }
                    break;
                case "DATE" : break;
                case "TIME" :
                case "TIMESTAMP" :
                case "DATETIME" :
                    this.fsp = Integer.parseInt(brackets);
                    break;
                case "YEAR" :
                    this.length = Integer.parseInt(brackets);
                    break;
                case "CHAR" :
                case "VARCHAR" :
                case "BINARY" :
                case "VARBINARY" :
                    this.length = Integer.parseInt(brackets);
                    break;
                case "TINYBLOB" :
                case "BLOB" :
                case "MEDIUMBLOB" :
                case "LONGBLOB" : break;
                case "TINYTEXT" :
                case "TEXT" :
                case "MEDIUMTEXT" :
                case "LONGTEXT" : break;
                case "ENUM" :
                case "SET" :
                    this.values = brackets.replace("'", "")
                        .split(",");
                    break;
                default:
                    log.error("can not recognise type {}: {}",
                            type, brackets);
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("analysis type {} failed: {}",
                    type, brackets);
            return false;
        }
        return true;
    }

    /**
     * 获取该列定义
     * @return 定义
     */
    public String definition() {
        return null;
    }

    /**
     * 初始化值 变 字符串
     * @param value 初始化值
     * @return 字符串值
     */
    public String value(Object value) {
        return null;
    }

    /**
     * 是否相等
     * @param s 另一个列定义
     * @return 是否相等
     */
    public boolean same(Column s) {
        return false;
    }

    String getName() {
        return this.name;
    }

    void setPlace(String place) {
        this.place = place;
    }

    boolean isPrimary() {
        return isTrue(this.primary);
    }

    void setPrimary() {
        this.primary = true;
        this.notNull = true;
    }

    void setIncrement() {
        if (this instanceof BaseIntegerColumn) {
            this.increment = true;
        } else {
            log.error("{} can not be auto increment", type);
        }
    }

    String getPlace() {
        return this.place;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", values=" + java.util.Arrays.toString(values) +
                ", length=" + length +
                ", decimals=" + decimals +
                ", fsp=" + fsp +
                ", unsigned=" + unsigned +
                ", zerofill=" + zerofill +
                ", binary=" + binary +
                ", charset='" + charset + '\'' +
                ", collate='" + collate + '\'' +
                ", notNull=" + notNull +
                ", nullable=" + nullable +
                ", defaultValue='" + defaultValue + '\'' +
                ", onUpdate='" + onUpdate + '\'' +
                ", increment=" + increment +
                ", primary=" + primary +
                ", comment='" + comment + '\'' +
                ", place='" + place + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Column that = (Column) o;
        return Objects.equals(sugar, that.sugar) &&
                Objects.equals(field, that.field) &&
                Objects.equals(model, that.model) &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Arrays.equals(values, that.values) &&
                Objects.equals(length, that.length) &&
                Objects.equals(decimals, that.decimals) &&
                Objects.equals(fsp, that.fsp) &&
                Objects.equals(unsigned, that.unsigned) &&
                Objects.equals(zerofill, that.zerofill) &&
                Objects.equals(binary, that.binary) &&
                Objects.equals(charset, that.charset) &&
                Objects.equals(collate, that.collate) &&
                Objects.equals(notNull, that.notNull) &&
                Objects.equals(nullable, that.nullable) &&
                Objects.equals(defaultValue, that.defaultValue) &&
                Objects.equals(onUpdate, that.onUpdate) &&
                Objects.equals(increment, that.increment) &&
                Objects.equals(primary, that.primary) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(keys, that.keys) &&
                Objects.equals(from, that.from) &&
                Objects.equals(place, that.place);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(sugar, field, model,
                name, type,
                length, decimals, fsp,
                unsigned, zerofill, binary, charset, collate,
                notNull, nullable, defaultValue, onUpdate,
                increment, primary, comment, keys, from, place);
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }

    public static class Builder {

        String[] values;
        Integer length;
        Integer decimals;
        Integer fsp;

        Boolean unsigned;
        Boolean zerofill;
        Boolean binary;
        String charset;
        String collate;
        Boolean notNull ;
        Boolean nullable;
        String defaultValue;
        String onUpdate;
        Boolean increment;
        Boolean primary;
        String comment;

        public Builder values(String[] values) {
            this.values = values;
            return this;
        }

        public Builder length(Integer length) {
            this.length = length;
            return this;
        }

        public Builder decimals(Integer decimals) {
            this.decimals = decimals;
            return this;
        }

        public Builder fsp(Integer fsp) {
            this.fsp = fsp;
            return this;
        }

        public Builder unsigned(Boolean unsigned) {
            this.unsigned = unsigned;
            return this;
        }

        public Builder zerofill(Boolean zerofill) {
            this.zerofill = zerofill;
            return this;
        }

        public Builder binary(Boolean binary) {
            this.binary = binary;
            return this;
        }

        public Builder charset(String charset) {
            this.charset = charset;
            return this;
        }

        public Builder collate(String collate) {
            this.collate = collate;
            return this;
        }

        public Builder notNull(Boolean notNull) {
            this.notNull = notNull;
            return this;
        }

        public Builder nullable(Boolean nullable) {
            this.nullable = nullable;
            return this;
        }

        public Builder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder onUpdate(String onUpdate) {
            this.onUpdate = onUpdate;
            return this;
        }

        public Builder increment(Boolean increment) {
            this.increment = increment;
            return this;
        }

        public Builder primary(Boolean primary) {
            this.primary = primary;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public Column build() {
            Column c = new Column();

            c.values = this.values;
            c.length = this.length;
            c.decimals = this.decimals;
            c.fsp = this.fsp;

            c.unsigned = this.unsigned;
            c.zerofill = this.zerofill;
            c.binary = this.binary;
            c.charset = this.charset;
            c.collate = this.collate;
            c.notNull = this.notNull;
            c.nullable = this.nullable;
            c.defaultValue = this.defaultValue;
            c.onUpdate = this.onUpdate;
            c.increment = this.increment;
            c.primary = this.primary;
            c.comment = this.comment;
            return c;
        }
    }

    // ===================== tools =====================

    /**
     * 是否忽略
     * @param field 属性
     * @return 是否忽略
     */
    private static boolean isTransient(Field field) {
        return field.isAnnotationPresent(TRANSIENT.class);
    }

    /**
     * 按顺序检查类型
     * @param field 属性
     * @param annotations 注解类型
     * @return 数据类型
     */
    @SuppressWarnings("unchecked")
    private static String getType(Field field,
                                  Class<?>... annotations) {
        for (Class<?> annotation : annotations) {
            if (field.isAnnotationPresent((Class<Annotation>)annotation)) {
                return annotation.getSimpleName();
            }
        }
        return null;
    }

    /**
     * 根据注解获取类型
     * @param field 属性
     * @return 数据类型
     */
    private static String getTypeByAnnotation(Field field) {
        String type = getType(field,
                // 常用数据类型
                BIGINT.class,
                INT.class,
                INTEGER.class,
                DECIMAL.class,
                DATETIME.class,
                VARCHAR.class,
                // 整型类型
                BIT.class,
                TINYINT.class,
                SMALLINT.class,
                MEDIUMINT.class,
                // 小数类型
                DECIMAL.class,
                NUMERIC.class,
                FLOAT.class,
                DOUBLE.class,
                REAL.class,
                // 日期时间类型
                DATE.class,
                YEAR.class,
                TIMESTAMP.class,
                TIME.class,
                // 字符和二进制类型
                CHAR.class,
                BINARY.class,
                VARBINARY.class,
                // 大数据 binary large object
                TINYBLOB.class,
                BLOB.class,
                MEDIUMBLOB.class,
                LONGBLOB.class,
                // 文本类型
                TINYTEXT.class,
                TEXT.class,
                MEDIUMTEXT.class,
                LONGTEXT.class,
                // 集合类型
                ENUM.class,
                SET.class);

        if (null == type && field.isAnnotationPresent(BOOLEAN.class)) {
            return "TINYINT";
        }

        return null;
    }

    /**
     * 内置根据Java类型确定数据类型
     */
    private static final java.util.Map<Class, String> CLASS_MAP
            = new java.util.HashMap<>();
    static {
        CLASS_MAP.put(long.class, "BIGINT");
        CLASS_MAP.put(Long.class, "BIGINT");
        CLASS_MAP.put(BigInteger.class, "BIGINT");
        CLASS_MAP.put(int.class, "INT");
        CLASS_MAP.put(Integer.class, "INT");
        CLASS_MAP.put(BigDecimal.class, "DECIMAL");
        CLASS_MAP.put(java.util.Date.class, "DATETIME");
        CLASS_MAP.put(String.class, "VARCHAR");

        CLASS_MAP.put(boolean.class, "TINYINT");
        CLASS_MAP.put(Boolean.class, "TINYINT");

        //if (clazz == String.class) return "BINARY";
        //if (clazz == String.class) return "BIT";
        CLASS_MAP.put(byte[].class, "BLOB");
        CLASS_MAP.put(Byte[].class, "BLOB");
        //if (clazz == String.class) return "CHAR";
        CLASS_MAP.put(java.sql.Date.class, "DATE");
        CLASS_MAP.put(double.class, "DOUBLE");
        CLASS_MAP.put(java.lang.Double.class, "DOUBLE");
        CLASS_MAP.put(float.class, "FLOAT");
        CLASS_MAP.put(java.lang.Float.class, "FLOAT");
        //if (clazz == String.class) return "LONGBLOB";
        //if (clazz == String.class) return "LONGTEXT";
        //if (clazz == String.class) return "MEDIUMBLOB";
        //if (clazz == String.class) return "MEDIUMINT";
        //if (clazz == String.class) return "MEDIUMTEXT";
        //if (clazz == String.class) return "NUMERIC";
        //if (clazz == String.class) return "REAL";

        //if (clazz == String.class) return "TEXT";
        CLASS_MAP.put(java.sql.Time.class, "TIME");
        CLASS_MAP.put(java.sql.Timestamp.class, "TIMESTAMP");
        //if (clazz == String.class) return "TINYBLOB";
        //if (clazz == String.class) return "TINYINT";
        //if (clazz == String.class) return "TINYTEXT";
        //if (clazz == String.class) return "VARBINARY";
        CLASS_MAP.put(java.time.Year.class, "YEAR");

    }

    /**
     * 根据类型确定数据类型
     * @param clazz 类
     * @return 数据类型
     */
    private static String getTypeByClass(Class<?> clazz) {
        String type = CLASS_MAP.get(clazz);
        if (useful(type)) {
            return type;
        }

        if (clazz.isEnum()) {
            return "ENUM";
        }
        if (clazz.isAssignableFrom(java.util.Set.class)) {
            return "SET";
        }

        return null;
    }

    /**
     * 获取列实例
     * @param type 数据类型
     * @param field 属性
     * @param sugar 配置
     * @param model 模板
     * @return 列实例
     */
    private static Column getColumn(String type, Field field,
                                    Sugar sugar, Column model) {
        try {
            switch (type) {
                case "BIT": return new Bit(field, sugar, model);

                case "TINYINT": return new TinyInt(field, sugar, model);
                case "SMALLINT": return new SmallInt(field, sugar, model);
                case "MEDIUMINT": return new MediumInt(field, sugar, model);
                case "INT":
                case "INTEGER": return new Int(field, sugar, model);
                case "BIGINT": return new BigInt(field, sugar, model);

                case "DECIMAL":
                case "NUMERIC": return new Decimal(field, sugar, model);
                case "FLOAT": return new Float(field, sugar, model);
                case "DOUBLE":
                case "REAL": return new Double(field, sugar, model);

                case "DATE": return new Date(field, sugar, model);
                case "YEAR": return new Year(field, sugar, model);
                case "DATETIME": return new DateTime(field, sugar, model);
                case "TIMESTAMP": return new Timestamp(field, sugar, model);
                case "TIME": return new Time(field, sugar, model);

                case "CHAR": return new Char(field, sugar, model);
                case "VARCHAR": return new VarChar(field, sugar, model);

                case "BINARY": return new Binary(field, sugar, model);
                case "VARBINARY": return new VarBinary(field, sugar, model);

                case "TINYBLOB": return new TinyBlob(field, sugar, model);
                case "BLOB": return new Blob(field, sugar, model);
                case "MEDIUMBLOB": return new MediumBlob(field, sugar, model);
                case "LONGBLOB": return new LongBlob(field, sugar, model);

                case "TINYTEXT": return new TinyText(field, sugar, model);
                case "TEXT": return new Text(field, sugar, model);
                case "MEDIUMTEXT": return new MediumText(field, sugar, model);
                case "LONGTEXT": return new LongText(field, sugar, model);

                case "ENUM": return new Enum(field, sugar, model);
                case "SET": return new Set(field, sugar, model);
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("can not cast {} to {}", model, type);
        }
        return null;
    }

    /**
     * 根据属性和配置获取类型实例
     * @param field 属性
     * @param sugar 配置
     * @return 列对象
     */
    static Column build(Field field, Sugar sugar) {
        if (isTransient(field)) {
            return null;
        }

        Column model = null;
        Class clazz = field.getType();

        // 先确定数据类型
        String type = getTypeByAnnotation(field);
        if (!useful(type)) {
            model = sugar.getOmitByClass().get(clazz);
            if (exist(model)) {
                type = model.type;
            }
        }
        if (!useful(type)) {
            type = getTypeByClass(clazz);
        }
        if (!useful(type)) {
            log.error("can not recognise the type of field: {}", field);
            return null;
        }

        // 尽量确定配置列模型
        Column model2 = sugar.getOmitByType().get(type);
        if (exist(model2)) {
            model = model2;
        }
        Column column = getColumn(type, field, sugar, model);
        if (exist(column)) {
            return column;
        }
        String info = field.toGenericString().split(" ")[2];
        log.error("can not find type: {} {}", info, model);
        return null;
    }

}
