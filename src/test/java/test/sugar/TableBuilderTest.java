package test.sugar;

import dive.sugar.Sugar;
import dive.sugar.model.type.string.VarChar;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import test.sugar.model.Sex;
import test.sugar.model.Unique;
import test.sugar.model.Unique2;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TableBuilderTest {

    @Value("${mime.auto.table.driver}")
    private String driver;
    @Value("${mime.auto.table.url}")
    private String url;
    @Value("${mime.auto.table.username}")
    private String username;
    @Value("${mime.auto.table.password}")
    private String password;

    @Test
    public void test() {
        Set<String> hobbies = new HashSet<>();
        hobbies.add("234");
        hobbies.add("345");
        Unique unique = new Unique("123", hobbies);
        Sugar builder =
                new Sugar()
                        .info(System.out::println)
                        .error(System.err::println)
                        .connect(driver, url, username, password)
//                        .auto("recreate")
                        .auto("update")
                        .omit("VARCHAR",
                                new VarChar.Builder()
                                        .length(255)
                                        .build())
                        .omit(Sex.class)
                        .extend(Unique2.class)
                        .prepareAll(Unique.class)
                        .initial(Unique.class, "(`id`) VALUES (1)")
                        .initial(Unique.class, "(`name`) VALUES ('123')")
                        .initial(Unique.class, "(`name`) VALUES ('1234')")
                        .initial(Unique.class, "(`sex`) VALUES ('男')")
                        .initial(Unique.class, unique)
                        .initial(Unique.class, unique)
                        .initial(Unique.class, unique)
                        .initial(Unique.class, unique)


                        .check();
    }

}
