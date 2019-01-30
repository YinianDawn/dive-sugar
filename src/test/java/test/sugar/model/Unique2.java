package test.sugar.model;

import java.util.Set;

public class Unique2 extends Unique {

    private String email;

    private String cheers;

    public Unique2(String password, Set<String> hobbies) {
        super(password, hobbies);
    }
}
