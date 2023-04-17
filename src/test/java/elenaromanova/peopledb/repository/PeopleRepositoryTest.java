package elenaromanova.peopledb.repository;

import elenaromanova.peopledb.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class PeopleRepositoryTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:/Users/alisia/Projects/people_test");
    }

    @Test
    public void canSaveOnePerson() {
        PeopleRepository repo = new PeopleRepository(connection);
        Person john = new Person(
            "John",
            "Smith",
            ZonedDateTime.of(1987, 3, 15,14, 32, 0, 0, ZoneId.of("+2")));
        Person savedPerson = repo.save(john);

        assertThat(savedPerson.getId()).isGreaterThan(0);
    }

    @Test
    public void canSaveTwoPeople() {
        PeopleRepository repo = new PeopleRepository(connection);
        Person john = new Person(
                "John",
                "Smith",
                ZonedDateTime.of(1987, 3, 15,14, 32, 0, 0, ZoneId.of("+2")));
        Person jane = new Person(
                "Lane",
                "Doe",
                ZonedDateTime.of(1994, 5, 9,22, 12, 0, 0, ZoneId.of("+1")));
        Person savedJohn = repo.save(john);
        Person savedJane = repo.save(jane);

        assertThat(savedJohn.getId()).isNotEqualTo(savedJane.getId());
    }
}
