package com.prodyna.pac.eternity.common.helper.impl;

import com.prodyna.pac.eternity.common.helper.RememberMeAccessor;

/**
 * Default implementataion for a RememberMeAccessor
 */
public class RememberMeAccessorImpl implements RememberMeAccessor {


    @Override
    public String getRememberMeId(final String rememberMeToken) {

        return rememberMeToken.split(TOKEN_DELIMITER)[0];

    }

    @Override
    public String getRememberMePassword(final String rememberMeToken) {

        return rememberMeToken.split(TOKEN_DELIMITER)[1];

    }

    @Override
    public boolean isValidToken(final String token) {

        return token != null && token.length() > 10 && token.contains(TOKEN_DELIMITER);

    }

}
