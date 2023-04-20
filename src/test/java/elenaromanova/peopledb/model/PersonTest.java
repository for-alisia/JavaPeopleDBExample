package elenaromanova.peopledb.model;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {
    @Test
    public void testForEquality() {
        Person firstPerson = new Person("a", "b", ZonedDateTime.of(2000, 1, 1, 1, 1, 1, 1, ZoneId.of("-1")));
        Person secondPerson = new Person("a", "b", ZonedDateTime.of(2000, 1, 1, 1, 1, 1, 1, ZoneId.of("-1")));

        assertThat(firstPerson).isEqualTo(secondPerson);
    }

    @Test
    public void testForInequality() {
        Person firstPerson = new Person("d", "e", ZonedDateTime.of(2000, 1, 1, 1, 1, 1, 1, ZoneId.of("-1")));
        Person secondPerson = new Person("a", "b", ZonedDateTime.of(2000, 1, 1, 1, 1, 1, 1, ZoneId.of("-1")));

        assertThat(firstPerson).isNotEqualTo(secondPerson);
    }

    public static Person createPerson() {
        String suffix = String.valueOf(Math.random());

        return new Person(
                "testName".concat(suffix),
                "testLastName".concat(suffix),
                ZonedDateTime.now()
        );
    }
}