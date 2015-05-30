package com.prodyna.pac.eternity.test.common;

import com.prodyna.pac.eternity.common.helper.PasswordHashBuilder;
import com.prodyna.pac.eternity.common.helper.impl.PasswordHashBuilderImpl;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Tests for password generation and validation.
 */
public class PasswordHashTest {

    private PasswordHashBuilder passwordHashBuilder = new PasswordHashBuilderImpl();

    @Test
    public void testCreatePasswordHash() {

        String[] passwords = {"demo", "secret", "s3cure"};

        for (String password : passwords) {

            String hash = passwordHashBuilder.createHash(password);
            String secondHash = passwordHashBuilder.createHash(password);

            Assert.assertNotNull(hash);
            Assert.assertNotNull(secondHash);
            Assert.assertFalse(hash.equals(secondHash));
        }

    }

    @Test
    public void testValidatePassword() {

        String[] passwords = {"demo", "secret", "s3cure"};

        for (String password : passwords) {

            String hash = passwordHashBuilder.createHash(password);

            Assert.assertTrue(passwordHashBuilder.validatePassword(password, hash));
            Assert.assertFalse(passwordHashBuilder.validatePassword(password + System.currentTimeMillis(), hash));

        }

    }

}
