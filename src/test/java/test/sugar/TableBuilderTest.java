package test.sugar;

import dive.sugar.Auto;
import dive.sugar.Sugar;
import dive.sugar.model.type.number.Decimal;
import dive.sugar.model.type.string.VarChar;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import test.sugar.model.Sex;
import test.sugar.model.Unique;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TableBuilderTest {

    @Value("${dive.sugar.driver}")
    private String driver;
    @Value("${dive.sugar.url}")
    private String url;
    @Value("${dive.sugar.username}")
    private String username;
    @Value("${dive.sugar.password}")
    private String password;

    @Test
    public void test() {
        Set<String> hobbies = new HashSet<>();
        hobbies.add("234");
        hobbies.add("345");
        Unique unique = new Unique("123", hobbies);
        Sugar.build()
                .info(System.out::println)
                .error(System.err::println)

                .connect(driver, url, username, password)

                .auto(Auto.REREATE)
                .auto(Auto.UPDATE)

                .omit("VARCHAR",
                        new VarChar.Builder()
                                .length(255)
                                .build())

                .omit("DECIMAL",
                        new Decimal.Builder()
                                .length(9)
                                .decimals(1)
                                .build())

                .delete(Sex.class)
                .extend(true)
//                .noExtend(Unique2.class)
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
