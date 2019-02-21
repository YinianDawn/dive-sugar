package dive.sugar.model;

import dive.sugar.annotate.EXTEND;
import dive.sugar.annotate.TABLE;
import dive.sugar.annotate.TRANSIENT;
import dive.sugar.Sugar;
import dive.sugar.annotate.prop.*;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * @author dawn
 */
public class Table extends Base {

    private Sugar sugar;

    private Class<?> table;

    private String name;
    private String engine;
    private String charset;
    private String collate;
    private String comment;
    private String from;

    private boolean recreate = false;
    private boolean create = false;

    private List<Column> columns = new ArrayList<>();

    private List<Key> keys = new ArrayList<>();

    private List<Object> initial;

    private String status = "";

    // =============== constructor ===============

    public Table(Sugar sugar, Class table) {
        this.sugar = sugar;
        this.table = table;
        init();
    }

    private void init() {
        if (!exist(this.table)) {
            log.error("can not init table info --> table class is null");
            return;
        }

        if (this.table.isAnnotationPresent(TRANSIENT.class)
                || this.table.isAnnotationPresent(javax.persistence.Transient.class)) {
            return;
        }

        EXTEND extend = this.table.getAnnotation(EXTEND.class);
        if (exist(extend)) {
            this.sugar.extend(table,
                    Object.class.equals(extend.value()) ? null : extend.value());
        }

        TABLE tableAnnotate = this.table.getAnnotation(TABLE.class);
        javax.persistence.Table tablePersistence =
                this.table.getAnnotation(javax.persistence.Table.class);
        if (this.sugar.isAnnotate()
                && !exist(tablePersistence)
                && !exist(tableAnnotate)) {
            log.error("can not init table info --> " +
                    "annotation table is necessary but miss");
            return;
        }

        if (exist(tableAnnotate) && useful(tableAnnotate.value())) {
            this.name = tableAnnotate.value().trim();
        }
        if (!useful(name)
                && exist(tablePersistence)
                && useful(tablePersistence.name())) {
            this.name = tablePersistence.name().trim();
        }
        if (!useful(this.name)) {
            NAME nameAnnotate = this.table.getAnnotation(NAME.class);
            if (exist(nameAnnotate) && useful(nameAnnotate.value())) {
                this.name = nameAnnotate.value().trim();
            }
        }

        ENGINE engineAnnotation = this.table.getAnnotation(ENGINE.class);
        if (exist(engineAnnotation) && useful(engineAnnotation.value())) {
            this.engine = engineAnnotation.value().trim();
        }

        CHARSET charsetAnnotation = this.table.getAnnotation(CHARSET.class);
        if (exist(charsetAnnotation) && useful(charsetAnnotation.value())) {
            this.charset = charsetAnnotation.value().trim();
        }

        COLLATE collateAnnotation = this.table.getAnnotation(COLLATE.class);
        if (exist(collateAnnotation) && useful(collateAnnotation.value())) {
            this.collate = collateAnnotation.value().trim();
        }

        COMMENT commentAnnotation = this.table.getAnnotation(COMMENT.class);
        if (exist(commentAnnotation) && useful(commentAnnotation.value())) {
            this.comment = commentAnnotation.value().trim();
        }

        FROM fromAnnotation = this.table.getAnnotation(FROM.class);
        if (exist(fromAnnotation) && useful(fromAnnotation.value())) {
            this.from = fromAnnotation.value().trim();
        }

        switch (this.sugar.getAuto()) {
            case "recreate": recreate = true; break;
            case "create": create = true; break;
            case "update": break;
            default: log.error("auto is " + this.sugar.getAuto()); return;
        }

        if (!useful(name)) {
            String[] ns = this.table.getName().split("\\.");
            String n = ns[ns.length - 1];
            name = name(n, this.sugar.isCamel());
        }
        name = name.trim();
        name = useful(this.sugar.getPrefix()) ? this.sugar.getPrefix() + name : name;

        if (name.equals(from)) {
            from = null;
        }

        if (!useful(engine) && useful(this.sugar.getEngine())) {
            engine = this.sugar.getEngine().trim();
        }
        if (!useful(charset) && useful(this.sugar.getCharset())) {
            charset = this.sugar.getCharset().trim();
        }
        if (!useful(collate) && useful(this.sugar.getCollate())) {
            collate = this.sugar.getCollate().trim();
        }

        for (Field f : this.table.getDeclaredFields()) {
            Column column = Column.build(f, this.sugar);
            if (exist(column) && column.isValid()) {
                columns.add(column);
            }
        }

        Class parent = this.sugar.parent(this.table);
        if (exist(parent)) {
            Table p = new Table(this.sugar, parent);
            if (0 < p.columns.size()) {
                Set<Integer> removed = new HashSet<>();
                for (int i = 0; i < p.columns.size(); i++) {
                    for (Column c : columns) {
                        if (p.columns.get(i).getName().equals(c.getName())) {
                            removed.add(i);
                            break;
                        }
                    }
                }
                removed.forEach(i -> p.columns.remove(i.intValue()));
                OTTER:
                for (int i = p.columns.size() - 1; 0 <= i; i--) {
                    String name = p.columns.get(i).name;
                    for (Column c : columns) {
                        if (c.name.equals(name)) {
                            p.columns.remove(i);
                            continue OTTER;
                        }
                    }
                }
                p.columns.addAll(columns);
                columns = p.columns;
            }
        }

        if (columns.isEmpty()) {
            log.error("table {}: ({}) does not have any column.",
                    name, this.table.getName());
            return;
        }

        Set<String> set = new HashSet<>();
        for (Column c : columns) {
            if (!set.contains(c.getName())) {
                set.add(c.getName());
            } else {
                log.error("there is repeated column name: `"
                        + c.getName() + "`, check please:" + this.table);
                return;
            }
        }

        columns.get(0).setPlace("FIRST");
        for (int i = 1, l = columns.size(); i < l; i++) {
            columns.get(i).setPlace("AFTER " + columns.get(i - 1).getName());
        }

        List<Column> primaries = new ArrayList<>();
        for (Column c : columns) {
            if (c.isPrimary()) {
                primaries.add(c);
            }
        }
        if (0 == primaries.size()) {
            if (this.sugar.isPrimary()) {
                columns.get(0).setPrimary();
                if (this.sugar.isIncrement()) {
                    columns.get(0).setIncrement();
                }
            } else {
                log.error("table {}: does not have primary key", name);
            }
        }
        if (1 < primaries.size()){
            log.error("table {}: has many primary key:{}",
                    name, Arrays.toString(primaries.toArray()));
            return;
        }

        for (Column c : columns) {
            if (exist(c.keys) && 0 <c.keys.size()) {
                keys.addAll(c.keys);
            }
        }
        List<Key> ks = Key.build(this.table);
        if (exist(ks) && 0 < ks.size()) {
            keys.addAll(ks);
        }

        valid = true;
    }

    public void initial(List<Object> initial) {
        this.initial = initial;
    }

    private Table(Statement stmt, String name) throws SQLException {
        if (!existTable(stmt, name)){
            log.error("table {}: is not exist, can not analysis", name);
            throw new SQLException("table " + name
                    + " is not exist, can not analysis");
        }
        String tableDefinition = getTableDefinition(stmt, name);

        int l = tableDefinition.indexOf("(");
        int r = tableDefinition.lastIndexOf(")");
        String ll = tableDefinition.substring(0, l).trim();
        String rr = tableDefinition.substring(r + 1).trim();
        String sqlName = pattern(ll, "CREATE TABLE `", "`");
        if (!name.equals(sqlName)) {
            log.error("table name {} is not equal this table {}" +
                    ", can not analysis: {}", sqlName, name, tableDefinition);
            throw new SQLException("table name " + sqlName
                    + " is not equal this table " + name +
                    ", can not analysis: " + tableDefinition);
        }
        this.name = name;
        engine = pattern(rr, "ENGINE=", "\\b");
        charset = pattern(rr, "CHARSET=", "\\b");
        collate = pattern(rr, "COLLATE=", "\\b");
        comment = pattern(rr, "COMMENT='", "\\'");

        if ("utf8mb4".equals(charset) && null == collate) {
            collate = "utf8mb4_general_ci";
        }

        String[] cs = tableDefinition.
                substring(l + 1, r).trim().split(",\n\\s+");
        for (String definition : cs){
            Column column = new Column(definition);
            if (column.isValid()) {
                columns.add(column);
            }
        }
        // find primary key
        String primaryKeyName = null;
        for (String definition : cs){
            if (definition.startsWith("PRIMARY KEY (")) {
                String d = definition.substring(13);
                String n = d.substring(0, d.indexOf(")"));
                if (!n.contains(",")) {
                    primaryKeyName = n.substring(1, n.length() - 1);
                } else {
                    log.error("exist union primary key, " +
                            "can not handle, sorry: {}", tableDefinition);
                    return;
                }
                break;
            }
        }
        if (useful(primaryKeyName)) {
            for (Column c : columns) {
                if (c.getName().equals(primaryKeyName)) {
                    c.setPrimary();
                }
            }
        }
        columns.get(0).setPlace("FIRST");
        for (int i = 1, length = columns.size(); i < length; i++) {
            columns.get(i).setPlace("AFTER " + columns.get(i - 1).getName());
        }
        for (String definition : cs){
            Key key = new Key(definition);
            if (key.valid) {
                keys.add(key);
            }
        }

        valid = true;
    }

    // =============== business ===============

    public void build(Statement stmt) {
        if (recreate) {
            dropTable(stmt, from);
            dropTable(stmt, name);
            status = "recreate";
        }

        renameTable(stmt, from, name);

        if (create && existTable(stmt, name)) {
            status = "nothing";
            return;
        }

        if (existTable(stmt, name)) {
            log.info("table {}: is exist, checking...", name);

            for (Column c : columns) {
                String from = c.from;
                if (useful(from)) {
                    renameColumn(stmt, from, c.getName(), name);
                }
            }

            Table exist = null;
            boolean changed = false;
            for (int i = 0, l = columns.size(); i < l; i++) {
                if (!exist(exist) || changed) {
                    try {
                        exist = new Table(stmt, name);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (!exist(exist) || !exist.isValid()) {
                    log.error("analysis existed table {}: failed, " +
                            "skip this checking", name);
                    continue;
                }
                Column d  = columns.get(i);
                Column e = getColumnByName(d.getName(), exist.columns);
                if (exist(e)) {
                    if (!d.same(e)){
                        log.error("table {}: d --> {}", name, d);
                        log.error("table {}: e --> {}", name, e);
//                        if (d.isPrimary()) dropPrimaryKey(stmt, name);
                        changeColumn(stmt, d, name);
                        changed = true;
                    }
                } else { // no same column name
                    if (d.isPrimary()) {
                        dropPrimaryKey(stmt, name);
                    }
                    addColumn(stmt, d, name);
                    changed = true;
                }
            }

            try {
                exist = new Table(stmt, name);
                if (!exist.isValid()) {
                    log.error("analysis existed table {}: failed, " +
                            "skip this checking", name);
                    return;
                }
                checkTableInfo(stmt, exist.engine, engine, name, "ENGINE");
                checkTableInfo(stmt, exist.charset, charset, name, "CHARSET");
                checkTableInfo(stmt, exist.collate, collate, name, "COLLATE");
                checkTableInfo(stmt, exist.comment, comment, name, "COMMENT");
            } catch (SQLException e) {
                e.printStackTrace();
                log.error("analysis existed table {}: failed, " +
                        "skip this checking", name);
            }

            status = "update";
        } else {
            log.info("table {}: is not exist, creating...", name);
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE `").append(name).append("`(\n");
            for (Column c : columns) {
                sb.append("    ").append(c.definition()).append(",\n");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append(")");
            if (useful(engine)) {
                sb.append(" ENGINE='").append(engine).append("'");
            }
            if (useful(charset)) {
                sb.append(" CHARSET='").append(charset).append("'");
            }
            if (useful(collate)) {
                sb.append(" COLLATE='").append(collate).append("'");
            }
            if (useful(comment)) {
                sb.append(" COMMENT='").append(comment).append("'");
            }
            sb.append(";");
            String sql = sb.toString();
            try {
                stmt.executeUpdate(sql);
                log.info("create table {}: successful", name);
                log.info("sql:\n{}", sql);
            } catch (SQLException e) {
                e.printStackTrace();
                log.error("create table {}: failed", name);
                log.error("sql:\n{}", sql);
            }
            status = "create";
        }
    }


    public void index(Statement stmt) {
        switch (status) {
            case "nothing": break;
            case "recreate":
            case "create":
                for (Key key : keys) {
                    String sql = "ALTER TABLE `" + name + "` ADD "
                            + key.getDefinition();
                    try {
                        log.info("add key, sql --> {}", sql);
                        stmt.execute(sql);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("add key error: {}", sql);
                    }
                }
                break;
            case "update":
                Table exist = null;
                try {
                    exist = new Table(stmt, name);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (!exist(exist) || !exist.isValid()) {
                    log.error("analysis existed table {}: failed, " +
                            "skip this checking", name);
                    return;
                }
                List<Key> es = exist.keys;
                String sql = null;
                for (Key key : keys) {
                    try {
                        Key k = getSameKey(es, key);
                        if (!exist(k)) {
                            // check name
                            if (useful(key.name)) {
                                Key sameName = getSameName(es, key);
                                if (exist(sameName)) {
                                    Key necessary = getSameKey(keys, sameName);
                                    if (exist(necessary)) {
                                        log.error("table {}: index checking error:");
                                        log.error("    wanna: -> {}", key);
                                        log.error("same name: -> {}", sameName);
                                        log.error("wanna too: -> {}", necessary);
                                        continue;
                                    } else {
                                        dropIndex(stmt, sameName, name);
                                        es.remove(sameName);
                                    }
                                }
                            }
                            sql = "ALTER TABLE `" + name + "` ADD "
                                    + key.getDefinition();
                            log.info("table {}: add index: {}", name, sql);
                            stmt.execute(sql);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        log.error("table {}: add index failed: {}", name, sql);
                    }
                }
                for (Key i : es) {
                    dropIndex(stmt, i, name);
                }
                break;
                default:
        }
    }

    public void initial(Statement stmt) {
        switch (status) {
            case "nothing": break;
            case "recreate":
            case "create":
                if (exist(initial) && 0 < initial.size()) {
                    for (Object i : initial) {
                        initial(stmt, i);
                    }
                }
                break;
            case "update":  break;
            default:
        }
    }

    private void initial(Statement stmt, Object initial) {
        if (!exist(initial)) {
            return;
        }
        String sql = "INSERT INTO `" + name + "`";
        if (initial instanceof String) {
            sql += initial;
        } else {
            Class<?> s = initial.getClass();
            if (!s.isAssignableFrom(table)) {
                log.error("can not init object, {} is not a {}",
                        initial.toString(), table.getName());
                return;
            }
            Map<Column, String> map = new HashMap<>();
            for (Column c : columns) {
                try {
                    Field f = s.getDeclaredField(c.field.getName());
                    f.setAccessible(true);
                    Object value = f.get(initial);
                    if (exist(value)) {
                        String v;
                        if (value instanceof String) {
                            v = (String) value;
                        } else {
                            v = c.value(value);
                        }
                        if (exist(v)) {
                            map.put(c, v);
                        } else {
                            log.error("can not transform {}", value);
                        }
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    log.error("can not find the value of {}", c.field.getName());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (map.isEmpty()) {
                log.error("can not find any init value");
                return;
            }
            StringBuilder names = new StringBuilder("(");
            StringBuilder values = new StringBuilder("(");
            map.forEach((c, v) -> {
                names.append("`").append(c.name).append("`,");
                values.append(v).append(",");
            });
            names.replace(names.length() - 1, names.length(), ")");
            values.replace(values.length() - 1, names.length(), ")");
            sql += names.toString() + " VALUES " + values.toString();
        }
        try {
            log.info("insert: {}", sql);
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("insert error: {}", sql);
        }
    }


    // =============== tools ===============

    private static void dropIndex(Statement stmt, Key key, String name) {
        String sql = "ALTER TABLE `" + name + "` DROP KEY " + key.name;
        try {
            log.info("table {}: drop index: {}", name, sql);
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("table {}: drop index failed: {}", name, sql);
        }
    }

    private static Key getSameName(List<Key> keys, Key key) {
        for (int i = keys.size() - 1; 0 <= i; i--) {
            if (keys.get(i).name.equals(key.name)) {
                return keys.get(i);
            }
        }
        return null;
    }

    private static Key getSameKey(List<Key> keys, Key key) {
        Key same = null;
        for (int i = keys.size() - 1; 0 <= i; i--) {
            if (keys.get(i).equals(key)) {
                keys.remove(i);
                same = key;
            }
        }
        return same;
    }

    private static void checkTableInfo(Statement stmt, String now,
                                       String want, String name, String define) {
        if (useful(now)) {
            if (now.equalsIgnoreCase(want)) {
                return;
            }
        } else {
            if (!useful(want)) {
                return;
            }
        }
        String sql = null;
        try {
            if (useful(want)) {
                sql = "ALTER TABLE `" + name + "` " + define + "='" +  want + "'";
                log.error("table {}: change setting {}: {}", name, define, sql);
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("table {}: change setting {} failed: ",
                    name, define, sql);
        }
    }

    private static void changeColumn(Statement stmt, Column column,
                                     String name) {
        String sql = "ALTER TABLE `" + name + "` CHANGE `" + column.getName()
                + "` " + column.definition()
                .replace("PRIMARY KEY", "") + " "
                + column.getPlace();
        try {
            log.info("table {}: change column {}: {}",
                    name, column.name, sql);
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("table {}: change column {} failed: {}",
                    name, column.name, sql);
        }
    }

    private static void addColumn(Statement stmt, Column column,
                                  String name)  {
        String sql = "ALTER TABLE `" + name + "` ADD "
                + column.definition() + " " + column.getPlace();
        try {
            log.info("table {}: add column: {}", name, sql);
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("table {}: add column failed: {}", name, sql);
        }
    }

    private static void dropPrimaryKey(Statement stmt, String name) {
        String sql = "ALTER TABLE `" + name + "` DROP PRIMARY KEY";
        try {
            log.info("table {}: drop primary key: {}", name, sql);
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("table {}: drop primary key failed: {}", name, sql);
        }
    }

    private static Column getColumnByName(String name, List<Column> columns) {
        for (Column c : columns) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    private static void renameColumn(Statement stmt, String from, String name,
                                     String tableName)  {
        String sql = null;
        try {
            if (!existTable(stmt, tableName)) {
                log.error("table {}: is not exist.", tableName);
                return;
            }
            if (!existColumn(stmt, from, tableName)) {
                log.error("table {}: does not have column {}",
                        tableName, from);
                return;
            }
            if (existColumn(stmt, name, tableName)) {
                log.error("table {}: already have column {}",
                        tableName, name);
                return;
            }
            String tableDefinition = getTableDefinition(stmt, tableName);
            String definition = pattern(tableDefinition,
                    "\\n\\s*`" + from + "` ", ",\n[\\s]*");
            sql = "ALTER TABLE `" + tableName + "` CHANGE `"
                    + from + "` `" + name + "` " + definition + ";";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("table {}: change name {} -> {} failed: {}",
                    tableName, from, name, sql);
        }
    }

    private static boolean existColumn(Statement stmt, String name,
                                       String tableName) {
        String sql = "DESCRIBE `" + tableName + "`;";
        try (ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                if (rs.getString(1).equals(name)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("table {}: check column {} error: ", tableName,
                    name, sql);
        }
        return false;
    }

    private static String getTableDefinition(Statement stmt, String name) {
        String sql = "SHOW CREATE TABLE `" + name + "`;";
        try (ResultSet rs = stmt.executeQuery(sql)){
            rs.next();
            return rs.getString(2);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("get table definition failed --> " + name);
        }
        return "";
    }

    private static void renameTable(Statement stmt, String from, String name) {
        String sql = null;
        try {
            if (existTable(stmt, from) && !existTable(stmt, name)) {
                sql = "ALTER TABLE " + from + " RENAME " + name + ";";
                log.error("rename table `{}` to `{}`: {}", from, name, sql);
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("rename table {}: to {} error: {}", from, name, sql);
        }
    }

    private static void dropTable(Statement stmt, String name) {
        String sql = "DROP TABLE IF EXISTS `" + name + "`";
        if (useful(name)){
            log.error("drop table `{}`: {}", name, sql);
            try {
                stmt.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
                log.error("drop table {}: error: {}", name, sql);
            }
        }
    }

    private static boolean existTable(Statement stmt, String name) {
        if (!useful(name)) {
            return false;
        }
        String sql = "SHOW TABLES LIKE '" + name + "'";
        try (ResultSet resultSet = stmt.executeQuery(sql)) {
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("check table {}: error: {}", name, sql);
        }
        return false;
    }
}
