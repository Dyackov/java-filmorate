package ru.yandex.practicum.filmorate.dal.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserRepositoryTest {
    private final UserRepository userRepository;

    @Test
    @DirtiesContext
    public void testCreateUser() {
        User user = new User();
        user.setEmail("Colton.Berge@yahoo.com");
        user.setLogin("HyXTwwChNw");
        user.setName("Kristy Welch");
        user.setBirthday(LocalDate.of(1980, 8, 13));
        userRepository.createUser(user);

        assertThat(userRepository.getUserById(user.getId()).getEmail()).isEqualTo("Colton.Berge@yahoo.com");
        assertThat(userRepository.getUserById(user.getId()).getLogin()).isEqualTo("HyXTwwChNw");
        assertThat(userRepository.getUserById(user.getId()).getName()).isEqualTo("Kristy Welch");
    }

    @Test
    @DirtiesContext
    public void testFindUserById() {
        User user = new User();
        user.setEmail("Colton.Berge@yahoo.com");
        user.setLogin("HyXTwwChNw");
        user.setName("Kristy Welch");
        user.setBirthday(LocalDate.of(1980, 8, 13));

        userRepository.createUser(user);

        Optional<User> userOptional = Optional.of(userRepository.getUserById(1L));
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
        userRepository.createUser(user);

        User newUser = new User();
        newUser.setId(1L);
        newUser.setEmail("Pansy_Runolfsson0@gmail.com");
        newUser.setLogin("lRm7KiJnCe");
        newUser.setName("Edwin Rice");
        newUser.setBirthday(LocalDate.of(1995, 12, 30));
        userRepository.updateUser(newUser);

        assertThat(userRepository.getUserById(newUser.getId()).getEmail()).isEqualTo("Pansy_Runolfsson0@gmail.com");
        assertThat(userRepository.getUserById(newUser.getId()).getLogin()).isEqualTo("lRm7KiJnCe");
        assertThat(userRepository.getUserById(newUser.getId()).getName()).isEqualTo("Edwin Rice");
    }

    @Test
    @DirtiesContext
    public void testDeleteByIdUser() {
        User user = new User();
        user.setEmail("Colton.Berge@yahoo.com");
        user.setLogin("HyXTwwChNw");
        user.setName("Kristy Welch");
        user.setBirthday(LocalDate.of(1980, 8, 13));
        userRepository.createUser(user);

        boolean delete = userRepository.removeFriendById(user.getId());
        assertThat(delete).isTrue();
    }
}