package com.opcua.server;

import com.prosysopc.ua.CertificateValidationListener;
import com.prosysopc.ua.PkiFileBasedCertificateValidator;
import com.prosysopc.ua.PkiFileBasedCertificateValidator.CertificateCheck;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.utils.CertificateUtils;
import org.springframework.stereotype.Component;

import java.security.cert.CertificateParsingException;
import java.util.EnumSet;


/**
 * Created by mj on 2017/12/12.
 *
 * @author li wen ya
 */
public class UaCertificateValidationListener implements CertificateValidationListener {

    @Override
    public PkiFileBasedCertificateValidator.ValidationResult onValidate(
            Cert certificate, ApplicationDescription applicationDescription, EnumSet<CertificateCheck> passedChecks) {
        try {
            OpcUaServer.println(applicationDescription + ", "
                    + CertificateUtils.getApplicationUriOfCertificate(certificate));
        } catch (CertificateParsingException e1) {
            throw new RuntimeException(e1);
        }

        // Do not mind about URI...
        if (passedChecks.containsAll(EnumSet.of(CertificateCheck.Trusted, CertificateCheck.Validity,
                CertificateCheck.Signature))) {
            if (!passedChecks.contains(CertificateCheck.Uri)) try {
                OpcUaServer
                        .println("Client's ApplicationURI (" + applicationDescription.getApplicationUri()
                                + ") does not match the one in certificate: "
                                + CertificateUtils.getApplicationUriOfCertificate(certificate));
            } catch (CertificateParsingException e) {
                throw new RuntimeException(e);
            }
            return PkiFileBasedCertificateValidator.ValidationResult.AcceptPermanently;
        }
        return PkiFileBasedCertificateValidator.ValidationResult.Reject;
    }
}
