package ru.yandex.practicum.filmorate.dal.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.JdbcUserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcUserRepositoryTest {
    private final JdbcUserRepository jdbcUserRepository;

    @Test
    @DirtiesContext
    public void testCreateUser() {
        User user = new User();
        user.setEmail("Colton.Berge@yahoo.com");
        user.setLogin("HyXTwwChNw");
        user.setName("Kristy Welch");
        user.setBirthday(LocalDate.of(1980, 8, 13));
        jdbcUserRepository.createUser(user);

        assertThat(jdbcUserRepository.getUserById(user.getId()).getEmail()).isEqualTo("Colton.Berge@yahoo.com");
        assertThat(jdbcUserRepository.getUserById(user.getId()).getLogin()).isEqualTo("HyXTwwChNw");
        assertThat(jdbcUserRepository.getUserById(user.getId()).getName()).isEqualTo("Kristy Welch");
    }

    @Test
    @DirtiesContext
    public void testFindUserById() {
        User user = new User();
        user.setEmail("Colton.Berge@yahoo.com");
        user.setLogin("HyXTwwChNw");
        user.setName("Kristy Welch");
        user.setBirthday(LocalDate.of(1980, 8, 13));

        jdbcUserRepository.createUser(user);

        Optional<User> userOptional = Optional.of(jdbcUserRepository.getUserById(1L));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(userTest ->
                        assertThat(userTest).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    @DirtiesContext
    public void testUpdateUser() {
        User user = new User();
        user.setEmail("Colton.Berge@yahoo.com");
        user.setLogin("HyXTwwChNw");
        user.setName("Kristy Welch");
        user.setBirthday(LocalDate.of(1980, 8, 13));
        jdbcUserRepository.createUser(user);

        User newUser = new User();
        newUser.setId(1L);
        newUser.setEmail("Pansy_Runolfsson0@gmail.com");
        newUser.setLogin("lRm7KiJnCe");
        newUser.setName("Edwin Rice");
        newUser.setBirthday(LocalDate.of(1995, 12, 30));
        jdbcUserRepository.updateUser(newUser);

        assertThat(jdbcUserRepository.getUserById(newUser.getId()).getEmail()).isEqualTo("Pansy_Runolfsson0@gmail.com");
        assertThat(jdbcUserRepository.getUserById(newUser.getId()).getLogin()).isEqualTo("lRm7KiJnCe");
        assertThat(jdbcUserRepository.getUserById(newUser.getId()).getName()).isEqualTo("Edwin Rice");
    }

    @Test
    @DirtiesContext
    public void testDeleteByIdUser() {
        User user = new User();
        user.setEmail("Colton.Berge@yahoo.com");
        user.setLogin("HyXTwwChNw");
        user.setName("Kristy Welch");
        user.setBirthday(LocalDate.of(1980, 8, 13));
        jdbcUserRepository.createUser(user);
//
//        boolean delete = jdbcUserRepository.deleteUserById(user.getId());
//        assertThat(delete).isTrue();
    }
}