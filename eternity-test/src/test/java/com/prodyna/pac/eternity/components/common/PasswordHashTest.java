package com.prodyna.pac.eternity.components.common;

import junit.framework.Assert;
import org.junit.Test;

import static com.prodyna.pac.eternity.components.common.PasswordHash.*;

/**
 * Tests for password generation and validation.
 */
public class PasswordHashTest {

    @Test
    public void testCreatePasswordHash() {

        String[] passwords = {"demo", "secret", "s3cure"};

        for (String password : passwords) {

            String hash = createHash(password);
            String secondHash = createHash(password);

            Assert.assertNotNull(hash);
            Assert.assertNotNull(secondHash);
            Assert.assertFalse(hash.equals(secondHash));
        }

    }

    @Test
    public void testValidatePassword() {

        String[] passwords = {"demo", "secret", "s3cure"};

        for (String password : passwords) {

            String hash = createHash(password);

            Assert.assertTrue(validatePassword(password, hash));
            Assert.assertFalse(validatePassword(password + System.currentTimeMillis(), hash));

        }

    }

}
