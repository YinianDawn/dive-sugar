package dive.sugar.model.type;

import dive.sugar.model.BaseColumn;

import java.util.function.Consumer;

import static sun.management.Agent.error;

/**
 * 样例列
 *
 * @author dawn
 */
public class ModelColumn extends BaseColumn {

    public ModelColumn() {}

    public ModelColumn(String definition) {
        if (!useful(definition)) {
            return;
        }
        String d = definition.trim();
        if (d.startsWith("PRIMARY KEY")) {
            return;
        }
        if (d.startsWith("UNIQUE KEY")) {
            return;
        }
        if (d.startsWith("KEY")) {
            return;
        }

        if (0 == d.length()) {
            return;
        }
        if (!d.startsWith("`")) {
            error("wrong column definition: {}", definition);
            return;
        }
        d = substring(d, 1);
        name = d.substring(0, d.indexOf("`"));
        d = substring(d, d.indexOf("`") + 2);
        String typeName = substring(d, 0, d.indexOf(" "));
        String brackets = pattern(typeName, "\\(", "\\)");
        if (useful(brackets)) {
            type = typeName.substring(0, typeName.indexOf("(")).toUpperCase();
            if (!parseBrackets(brackets)) {
                return;
            }
        } else {
            type = typeName.toUpperCase();
        }
        d = substring(d, d.indexOf(" ") + 1);
        if (d.startsWith("unsigned")) {
            unsigned = true;
            d = substring(d,9);
        }
        if (d.startsWith("zerofill")) {
            zerofill = true;
            d = substring(d, 9);
        }
        if (d.startsWith("binary")) {
            binary = true;
            d = substring(d, 7);
        }
        if (d.startsWith("CHARACTER SET")) {
            d = substring(d, 14);
            charset =  substring(d, 0, d.indexOf(" "));
            d = substring(d, d.indexOf(" ") + 1);
        }
        if (d.startsWith("COLLATE")) {
            d = substring(d, 8);
            collate =  substring(d, 0, d.indexOf(" "));
            d = substring(d, d.indexOf(" ") + 1);
        }
        if (d.startsWith("NOT NULL")) {
            notNull = true;
            d = substring(d, 9);
        }
        if (d.startsWith("DEFAULT NULL")) {
            nullable = true;
            d = substring(d, 13);
        }
        if (d.startsWith("DEFAULT")) {
            d = substring(d, 8);
            if (d.startsWith("'")) {
                d = substring(d, 1);
                defaultValue =  substring(d, 0, d.indexOf("'"));
                d = substring(d, d.indexOf("'") + 2);
            } else {
                defaultValue =  substring(d, 0, d.indexOf(" "));
                d = substring(d, d.indexOf(" ") + 1);
            }
        }
        if (d.startsWith("ON UPDATE")) {
            d = substring(d, 10);
            int index = d.indexOf(" ");
            if (-1 == index) {
                onUpdate = d;
                d = "";
            } else {
                onUpdate = d.substring(0, index);
                d = substring(d, index + 1);
            }
        }
        if (d.startsWith("AUTO_INCREMENT")) {
            autoIncrement = true;
            d = substring(d, 15);
        }
        if (d.startsWith("COMMENT")) {
            comment =  substring(d,
                    d.indexOf("'") + 1, d.lastIndexOf("'"));
        }
        valid = true;
    }

    private boolean parseBrackets(String brackets) {
        try {
            switch (type) {
                case "BOOLEAN" : break;
                case "BIT" :
                case "TINYINT" :
                case "SMALLINT" :
                case "MEDIUMINT" :
                case "INT" :
                case "INTEGER" :
                case "BIGINT" : length = Integer.parseInt(brackets); break;
                case "REAL" :
                case "DOUBLE" :
                case "FLOAT" :
                case "DECIMAL" :
                case "NUMERIC" :
                    String[] s = brackets.split(",");
                    length = Integer.parseInt(s[0]);
                    if (1 < s.length) {
                        decimals = Integer.parseInt(s[1]);
                    }
                    break;
                case "DATE" : break;
                case "TIME" :
                case "TIMESTAMP" :
                case "DATETIME" : fsp = Integer.parseInt(brackets); break;
                case "YEAR" : length = Integer.parseInt(brackets); break;
                case "CHAR" :
                case "VARCHAR" :
                case "BINARY" :
                case "VARBINARY" : length = Integer.parseInt(brackets); break;
                case "TINYBLOB" :
                case "BLOB" :
                case "MEDIUMBLOB" :
                case "LONGBLOB" : break;
                case "TINYTEXT" :
                case "TEXT" :
                case "MEDIUMTEXT" :
                case "LONGTEXT" : break;
                case "ENUM" :
                case "SET" : values = brackets.replace("'", "")
                        .split(","); break;
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

    @Override
    protected boolean check(Integer length, String from) {
        return false;
    }

    @Override
    protected boolean check(Integer decimal, String from, Integer precision, Integer scale, Consumer<Integer> consumer) {
        return false;
    }

    @Override
    protected boolean check(String defaultValue, String from) {
        return false;
    }

    @Override
    public String definition() {
        return null;
    }

    @Override
    public String value(Object value) {
        return null;
    }

    @Override
    public boolean same(BaseColumn s) {
        return false;
    }
}
