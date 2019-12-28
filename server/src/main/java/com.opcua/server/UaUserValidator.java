package com.opcua.server;

import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.server.ServerUserIdentity;
import com.prosysopc.ua.server.Session;
import com.prosysopc.ua.server.UserValidator;
import org.opcfoundation.ua.core.UserIdentityToken;
import org.opcfoundation.ua.core.UserTokenType;
import org.springframework.stereotype.Component;

/**
 * Created by mj on 2017/12/12.
 *
 * @author li wen ya
 */
public class UaUserValidator implements UserValidator {

/*
 * (non-Javadoc)
 *
 * @see
 * com.prosysopc.ua.server.UserValidator#onValidate(com.prosysopc.ua.server
 * .Session, com.prosysopc.ua.server.SessionManager.ServerUserIdentity)
 */
    @Override
    public boolean onValidate(Session session, ServerUserIdentity userIdentity) throws StatusException {
        // Return true, if the user is allowed access to the server
        // Note that the UserIdentity can be of different actual types,
        // depending on the selected authentication mode (by the client).
        OpcUaServer.println("onValidate: userIdentity=" + userIdentity);
        if (userIdentity.getType().equals(UserTokenType.UserName))
            if (userIdentity.getName().equals("opcua") && userIdentity.getPassword().equals("opcua"))
                return true;
            else if (userIdentity.getName().equals("opcua2") && userIdentity.getPassword().equals("opcua2"))
                return true;
            else
                return false;
        if (userIdentity.getType().equals(UserTokenType.Certificate))
            // Implement your strategy here, for example using the PkiFileBasedCertificateValidator
            return true;
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.prosysopc.ua.server.UserValidator#onValidationError(com.prosysopc
     * .ua.server.Session, org.opcfoundation.ua.core.UserIdentityToken,
     * java.lang.Exception)
     */
    @Override
    public void onValidationError(Session session, UserIdentityToken userToken, Exception exception) {
        OpcUaServer.println("onValidationError: User validation failed: userToken=" + userToken + " error=" + exception);
    }



}
