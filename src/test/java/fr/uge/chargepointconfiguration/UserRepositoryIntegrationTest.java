package fr.uge.chargepointconfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryIntegrationTest {
    
    @Autowired
    private UserRepository userRepository;
   
    @Test
    public void whenCalledSave_thenCorrectNumberOfUsers() {
        userRepository.save(new Users("Bob", "bob@domain.com"));
        List<Users> users = (List<Users>) userRepository.findAll();
        
        assertEquals(users.size(), 1);
    }    
}