package test.sugar.model;


import dive.sugar.annotate.index.INDEX;
import dive.sugar.annotate.index.UNIQUE;
import dive.sugar.annotate.prop.*;
import dive.sugar.annotate.type.SET;

import java.util.Date;
import java.util.Set;

@CHARSET("utf8mb4")
@UNIQUE(names = {"age", "sex"}, unique = false)
public class Unique {

    public Unique(String password, Set<String> hobbies) {
        this.password = password;
        this.hobbies = hobbies;
    }

    @AUTO_INCREMENT
    @UNSIGNED
    private Long id;

    private Long id2;

    private String password;

    @UNIQUE(name = "u2", comment = "123", unique = false)
    private String name;

    @INDEX(name = "u1")
    private String nick;

    private Sex sex;

    @UNSIGNED
    private Integer age;

    private Integer age2;

    @UNIQUE
    private String email;

    @SET({"123", "345", "234", "247", "24"}) @DEFAULT("123,345")
    private Set<String> hobbies;

    @DEFAULT("CURRENT_TIMESTAMP")
    private Date created;

    @ON_UPDATE("CURRENT_TIMESTAMP")
    private Date updated;


}
