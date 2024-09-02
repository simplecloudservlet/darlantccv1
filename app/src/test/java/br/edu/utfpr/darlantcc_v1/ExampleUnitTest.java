package br.edu.utfpr.darlantcc_v1;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void teste1_inserirCadastro() {
        assertEquals(1, ActivityCadastrarPessoa.teste_inserirCadastro());
    }

        @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void teste2_senhaCorreta(){

        String password = "123";
        String candidate = "123";

        // Hash a password for the first time
        //String hashed = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());

        // gensalt's log_rounds parameter determines the complexity
// the work factor is 2**log_rounds, and the default is 10
        String hashed = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt(12));

// Check that an unencrypted password matches one that has
// previously been hashed
        /*if (org.mindrot.jbcrypt.BCrypt.checkpw(candidate, hashed))
            System.out.println("It matches");
        else
            System.out.println("It does not match");
        */
        assertEquals(true, org.mindrot.jbcrypt.BCrypt.checkpw(candidate, hashed));
    }

    @Test
    public void teste3_senhaInvalida(){

        String password = "123";
        String candidate = "joao";

        // Hash a password for the first time
        //String hashed = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());

        // gensalt's log_rounds parameter determines the complexity
        // the work factor is 2**log_rounds, and the default is 10
        String hashed = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt(12));

        System.out.println(hashed);
        // Check that an unencrypted password matches one that has
        // previously been hashed
        assertEquals(false, org.mindrot.jbcrypt.BCrypt.checkpw(candidate, hashed));

    }

}