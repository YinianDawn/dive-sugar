package dive.sugar.model;

import dive.sugar.Sugar;
import dive.sugar.annotate.index.INDEX;
import dive.sugar.annotate.index.UNIQUE;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static sun.management.Agent.error;

/**
 * 索引
 *
 * @author dawn
 */
public class Key extends Base {
    /**
     * 索引名称
     */
    String name;
    /**
     * 被索引的列
     */
    private String[] names;
    /**
     * 注释
     */
    private String comment;
    /**
     * 是否唯一索引
     */
    private Boolean unique;

    /**
     * 构造器
     *
     * @param name    索引名
     * @param names   被索引的列
     * @param comment 注释
     * @param unique  是否唯一索引
     * @param useful 是否启用
     */
    private Key(String name, String[] names,
                String comment, Boolean unique, Boolean useful) {
        this.name = name;
        this.names = names;
        this.comment = comment;
        this.unique = unique;
        super.useful = useful;
    }

    /**
     * 从定义解析索引
     *
     * @param definition sql定义
     */
    Key(String definition) {
        if (!useful(definition)) {
            return;
        }

        String d = definition.trim();
        if (0 == d.length()
                || d.startsWith("`")
                || d.startsWith("PRIMARY KEY")) {
            return;
        }
        if (d.startsWith("UNIQUE KEY")) {
            unique = true;
            d = substring(d, 11);
        }
        if (d.startsWith("KEY")) {
            d = substring(d, 4);
        }
        if (d.startsWith("FULLTEXT KEY")) {
            log.error("can not deal fulltext key, sorry: {}", definition);
            return;
        }
        if (d.startsWith("CONSTRAINT")) {
            log.error("can not deal foreign key, sorry: {}", definition);
            return;
        }

        if (d.startsWith("`")) {
            d = substring(d, 1);
        }
        name = d.substring(0, d.indexOf("`"));

        d = substring(d, name.length() + 2);
        if (d.startsWith("FOREIGN KEY")) {
            log.error("can not deal foreign key, sorry: {}", definition);
            return;
        }
        if (d.startsWith("(")) {
            d = substring(d, 1);
        }
        String names = d.substring(0, d.split(" ")[0].lastIndexOf(")"));
        this.names = names.replace("`", "").split(",");
        d = substring(d, d.indexOf(" ") + 1);

        if (d.startsWith("COMMENT")) {
            d = substring(d, 8);
            comment = d.substring(1, d.lastIndexOf("'"));
            if ("".equals(comment)) {
                comment = null;
            }
        }
        valid = true;
    }

    public String getDefinition() {
        StringBuilder sb = new StringBuilder();

        sb.append(unique ? "UNIQUE" : "").append(" KEY ");

        if (useful(name)) {
            sb.append("`").append(name).append("`");
        }

        sb.append(" (");

        for (String s : names) {
            if (s.contains("(")) {
                sb.append(" `").append(s.replace("(", "`("));
            } else {
                sb.append(" `").append(s).append("`");
            }
            sb.append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));

        sb.append(") ");

        if (useful(comment)) {
            sb.append("COMMENT '").append(comment).append("' ");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Key)) {
            return false;
        }
        Key key = (Key) o;
        return Arrays.equals(names, key.names) &&
                checkComment(comment, key.comment) &&
                Objects.equals(isTrue(unique), isTrue(key.unique)) &&
                super.useful == key.useful;
    }

    @Override
    public String toString() {
        return "Key{" +
                "name='" + this.name + '\'' +
                ", names=" + Arrays.toString(this.names) +
                ", comment='" + this.comment + '\'' +
                ", unique=" + this.unique +
                ", useful=" + super.useful +
                '}';
    }

    // =================== tools ======================

    private static Key build(String name, String[] names, String omit,
                             String comment, boolean unique, boolean useful) {
        if (0 == names.length) {
            if (!useful(omit)) {
                log.error("does not have any name.");
                return null;
            }
            names = new String[]{omit};
        }
        return new Key(name.trim(), names, comment.trim(), unique, useful);
    }

    private static Key build(INDEX index, String omit) {
        return build(index.name(), index.names(), omit, index.comment(), index.unique(), index.useful());
    }

    private static Key build(UNIQUE unique, String omit) {
        return build(unique.name(), unique.names(), omit, unique.comment(), unique.unique(), unique.useful());
    }

    private static List<Key> build(INDEX[] indices, String omit) {
        return Arrays.stream(indices)
                .map(index -> build(index, omit))
                .collect(Collectors.toList());
    }

    private static List<Key> build(UNIQUE[] uniques, String omit) {
        return Arrays.stream(uniques)
                .map(unique -> build(unique, omit))
                .collect(Collectors.toList());
    }

    /**
     * 获取一个表的所有索引信息
     *
     * @param table 表
     * @return 索引信息
     */
    static List<Key> build(Class<?> table) {
        INDEX[] indices = table.getAnnotationsByType(INDEX.class);
        List<Key> keys = new LinkedList<>(build(indices, null));
        UNIQUE[] uniques = table.getAnnotationsByType(UNIQUE.class);
        keys.addAll(build(uniques, null));
        return keys;
    }

}
