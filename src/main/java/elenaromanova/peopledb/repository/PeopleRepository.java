package elenaromanova.peopledb.repository;

import elenaromanova.peopledb.exception.UnableToSaveException;
import elenaromanova.peopledb.model.Person;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

public class PeopleRepository {
    public static final String SAVE_PERSON_SQL = "INSERT INTO people (first_name, last_name, dob) VALUES(?, ?, ?)";
    public static final String SELECT_PERSON_BY_ID = "SELECT id, first_name, last_name, dob FROM people WHERE id=?";
    private final Connection connection;
    public PeopleRepository(Connection connection) {
        this.connection = connection;
    }

    public Person save(Person person) throws UnableToSaveException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    SAVE_PERSON_SQL,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(
                    person.getDob().withZoneSameInstant(ZoneId.of("+0")).toLocalDateTime())
            );
            int recordsAffected = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                long id = resultSet.getLong(1);
                person.setId(id);
                System.out.println(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnableToSaveException("Tried to save person: " + person);
        }
        return person;
    }

    public Optional<Person> findById(Long id) {
        Person person = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    SELECT_PERSON_BY_ID
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                long foundId = resultSet.getLong("id");
                String foundName = resultSet.getString("first_name");
                String foundLastName = resultSet.getString("last_name");
                ZonedDateTime dob = ZonedDateTime.of(
                        resultSet.getTimestamp("dob").toLocalDateTime(),
                        ZoneId.of("+0")
                );

                person = new Person(foundName, foundLastName, dob);
                person.setId(foundId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(person);
    }
}
