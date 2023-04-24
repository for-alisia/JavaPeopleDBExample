package elenaromanova.peopledb.repository;

import elenaromanova.peopledb.model.Person;
import elenaromanova.peopledb.model.PersonTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class PeopleRepositoryTest {

    private Connection connection;
    private PeopleRepository repo;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:/Users/alisia/Projects/people_test");
        connection.setAutoCommit(false);
        repo = new PeopleRepository(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void canSaveOnePerson() {
        Person person = PersonTest.createPerson();
        Person savedPerson = repo.save(person);

        assertThat(savedPerson.getId()).isGreaterThan(0);
    }

    @Test
    public void canSaveTwoPeople() {
        Person firstPerson = PersonTest.createPerson();
        Person secondPerson = PersonTest.createPerson();
        Person savedFirstPerson = repo.save(firstPerson);
        Person savedSecondPerson = repo.save(secondPerson);

        assertThat(savedFirstPerson.getId()).isNotEqualTo(savedSecondPerson.getId());
    }

    @Test
    public void canFindPersonById() {
        Person person = PersonTest.createPerson();
        Person savedPerson = repo.save(person);
        Optional<Person> foundPerson = repo.findById(savedPerson.getId());

        assertThat(foundPerson.get()).isEqualTo(savedPerson);
    }

    @Test
    public void testFindPersonByIdNotFound() {
        Optional<Person> person = repo.findById(-1L);

        assertThat(person).isEmpty();
    }

    @Test
    public void canCountPeople() {
        long startCount = repo.count();
        repo.save(PersonTest.createPerson());
        repo.save(PersonTest.createPerson());
        long endCount = repo.count();

        assertThat(endCount - startCount).isEqualTo(2);
    }

    @Test
    public void canDeletePersonById() {
        Person person = repo.save(PersonTest.createPerson());
        repo.delete(person.getId());
        Optional<Person> removedPerson = repo.findById(person.getId());

        assertThat(removedPerson).isEmpty();
    }

    @Test
    public void canDeleteMultiplePeopleByIds() {
        Person person1 = repo.save(PersonTest.createPerson());
        Person person2 = repo.save(PersonTest.createPerson());
        repo.delete(person1.getId(), person2.getId());

        Optional<Person> removedPerson1 = repo.findById(person1.getId());
        Optional<Person> removedPerson2 = repo.findById(person2.getId());

        assertThat(removedPerson1).isEmpty();
        assertThat(removedPerson2).isEmpty();
    }
}

