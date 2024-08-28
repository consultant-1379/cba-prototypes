/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.mediation.transport.tls.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.bind.DatatypeConverter;

import com.ericsson.oss.mediation.adapter.tls.exception.TlsSecurityException;

//CODE-REVIEW: move all constants to a separate class
public class TlsSecurityUtil {

	private static SSLContext sslContext;

	public static SSLContext createSslContext() throws TlsSecurityException {

		String certString = "-----BEGIN CERTIFICATE-----"
				+ "MIIDWTCCAkGgAwIBAgIJAN6QE/DXYZRRMA0GCSqGSIb3DQEBBQUAMEIxCzAJBgNV"
				+ "BAYTAlNFMREwDwYDVQQKEwhFcmljc3NvbjEgMB4GA1UEAxMXQzEyMzQ1Njc4OS5l"
				+ "cmljc3Nvbi5jb20wHhcNMTQwODEyMDkwNDIzWhcNMTQwOTExMDkwNDIzWjBCMQsw"
				+ "CQYDVQQGEwJTRTERMA8GA1UEChMIRXJpY3Nzb24xIDAeBgNVBAMTF0MxMjM0NTY3"
				+ "ODkuZXJpY3Nzb24uY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA"
				+ "qRbPQT/29IPUWBX4HwyB+Se24fl5x6U0D9DcRluGf+zxR1XnCtodFsp94C6buf42"
				+ "zN/qe7JkXNCo1nI9rtH/Us+UuMXDfy5GlqKsFRk9fyZxigeqK8nDsONVF/iIPUrZ"
				+ "DIZ2YqrxE6yExlK9QppiwqEgK8rFjf5WfrPcQrG4k258wvqmfNb5cUNd7W3gJsN1"
				+ "nlbar4SuDqQsuREt3yrTW9bvwg2/rqT69T4ZeOs3x4O+mjx+PD6pvqGPDpLrL+YC"
				+ "oLo2jwGl/C/6N69PpjRm6FdAgDcnYLP49XMItvnPfAyT6r1u6tSIXICEXCy/upZd"
				+ "+6oQNNO+K555JFXAbfPTUwIDAQABo1IwUDAdBgNVHQ4EFgQU0Nc4xCMNaSCgWPSV"
				+ "Pe2QRkJFJVQwHwYDVR0jBBgwFoAU0Nc4xCMNaSCgWPSVPe2QRkJFJVQwDgYDVR0P"
				+ "AQH/BAQDAgWgMA0GCSqGSIb3DQEBBQUAA4IBAQBCWd0JvOo1v6uvvsTd/rZPq7zA"
				+ "XM2Bz6dHXIO+r0xk0DoKKlLs4JxOcjDZGf2Yu39axJ2zxpnhrevQQyBKEZdzK2KP"
				+ "Qro6973zLP4Oq+vjOm2Mds6donprlSsqT1vY2XRJesR9uqXzb4k/CulgButFnpIN"
				+ "O1W35BnSXdir3RLNtJsYcJOvWgvDUkDsOJVUOD1iyIREZNZbSdpyY7Uw5fGa/WAp"
				+ "aakMkuISlwUhn8Oe0DanlFdI2u4XYDPiPdQDfTjuCRV0KfonL2WReVAPb5B432yJ"
				+ "NGjwK6WSoH/+BihAaE8jOL3UTUwBAXAJzhJddkYgrGzED+TG06pCet64MnaF"
				+ "-----END CERTIFICATE-----";

		String keyString = "-----BEGIN PRIVATE KEY-----"
				+ "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCpFs9BP/b0g9RY"
				+ "FfgfDIH5J7bh+XnHpTQP0NxGW4Z/7PFHVecK2h0Wyn3gLpu5/jbM3+p7smRc0KjW"
				+ "cj2u0f9Sz5S4xcN/LkaWoqwVGT1/JnGKB6orycOw41UX+Ig9StkMhnZiqvETrITG"
				+ "Ur1CmmLCoSArysWN/lZ+s9xCsbiTbnzC+qZ81vlxQ13tbeAmw3WeVtqvhK4OpCy5"
				+ "ES3fKtNb1u/CDb+upPr1Phl46zfHg76aPH48Pqm+oY8Okusv5gKgujaPAaX8L/o3"
				+ "r0+mNGboV0CANydgs/j1cwi2+c98DJPqvW7q1IhcgIRcLL+6ll37qhA0074rnnkk"
				+ "VcBt89NTAgMBAAECggEAB6W0cesGrEBaFjHIKv9R0YbuSoIR8+mQ1TZJABWbm6WH"
				+ "tAY6kM8UibdWwhDZkivhJprp6/ZBe8EkIa7BA2sT9RbWTxUCpnndT2rEBaBCm8GN"
				+ "WQTHuXtCknl1m8PtxXfhISPjrsq0ZINk7eNIE/8PgGXxIonlLWrXVpuj1pBbHK/L"
				+ "rwFfvFXq3NtILBfQz3G7X4X5s7okpyFxXrtbTgjO7ZgSxxO39qIcCqBWFXb2Wu0j"
				+ "DhE2xwwou/PE7xkzOFiayMnjy5/HUzDyRjr98pA95+7+JhiElLWRGsY1wCF0TD85"
				+ "1+6ULq+ac0TL62Bq5rijgD1hUknYtlWIDQLvFJ9ywQKBgQDfOWb5SAfe8hal8S0N"
				+ "m/WrUT1SJCiXpXIYKuhhHtrp7YeGYE/tG6FH6oAQBlp/sw9IblTSm+H0h7CyZCPL"
				+ "Spu6/2TnR8xzphc/QLiDTcXTgnPxNpPRJdDYSO8mXFXj+OdTnnazo7gb2SclIO5+"
				+ "3QN9g2aav7rNHsLfyS9voxmkGQKBgQDB6pCtDwJNu8s0aWz3OhZxZ5XMzVwOAmhG"
				+ "+uwqQYL1qSC9aBcJ/keHGkGaZKfhuEGBS6pymYtdzH1lOlCFra7jqppa5Mh2I6Ia"
				+ "u8cbU2f4m7R9fOJUDRk2EGr24XIiS+j1DmYpB+gojRZrTugISAViS0Zu7AObs4fJ"
				+ "gOUDGQ7ASwKBgQC5hbezjs4b2RBBsoRDP0+aptZUIoEqJ/L/awnfTgmbSjLM19cV"
				+ "tVUBtD/jEaS9ZVBaXnhY4f2VggC1As1M9CEh9YkjP2UI6Q2BIIU5e7Xi6BT8cOPB"
				+ "e7BrUZKRS0SHSCLaOGVnP7aQTpZeIVSWU5HHCyKHkrnqKJrF6ytlmSSCYQKBgQCj"
				+ "PdVg+huUWJytmHp0fN/bw7mlWNM/r+McrasVv2SBbUj0aaXLcdXA8+d6E3YIXFY4"
				+ "g/wO0RczdSuyha63egiZ+0pJbgMbANA79QueA06pSFpesTzOSOCKVLPW9N5h5MlN"
				+ "UjvKGq/jQzI2T4yhPA/cOUYnpmM7NZz/kpQ3UWbLlQKBgD/DQY9NiC5p18R6ufik"
				+ "+C+UC8xWjhIoBC2e2u26JfqtASzFWtwtsFz6sWx8X3F4/EonmxLYb3prZ8VW0zbq"
				+ "3qGi4NxE0dkoOB0w0crQA3rUX+SatTN2AFz3w3R2Qv5/D0821dXwHtgQWirFENRu"
				+ "XOrYn55nV+93nWPVp+A2kCTA" + "-----END PRIVATE KEY-----";

		byte[] certBytes = parseDERFromPEM(certString.getBytes(),
				"-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----");
		byte[] keyBytes = parseDERFromPEM(keyString.getBytes(),
				"-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----");

		X509Certificate cert;
		try {
			cert = generateCertificateFromDER(certBytes);
			RSAPrivateKey key = generatePrivateKeyFromDER(keyBytes);

			KeyStore keystore = updateKeyStore(cert, key);

			sslContext = SSLContext.getInstance("TLS");
			TrustManagerFactory tmf = TrustManagerFactory
					.getInstance("SunX509");
			tmf.init(keystore);

			TrustManager[] tm = tmf.getTrustManagers();

			// TODO look into KeyManager and "new SecureRandom" see OSS-RC NM
			// code
			sslContext.init(null, tm, null);
		} catch (CertificateException e) {
			throw new TlsSecurityException(e);
		} catch (KeyManagementException e) {
			throw new TlsSecurityException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new TlsSecurityException(e);
		} catch (KeyStoreException e) {
			throw new TlsSecurityException(e);
		} catch (IOException e) {
			throw new TlsSecurityException(e);
		} catch (InvalidKeySpecException e) {
			throw new TlsSecurityException(e);
		}
		return sslContext;
	}

	/**
	 * @param cert
	 * @param key
	 * @return
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 */
	private static KeyStore updateKeyStore(X509Certificate cert,
			RSAPrivateKey key) throws KeyStoreException, IOException,
			NoSuchAlgorithmException, CertificateException {
		KeyStore keystore = KeyStore.getInstance("JKS");
		keystore.load(null);
		keystore.setCertificateEntry("cert-alias", cert);
		keystore.setKeyEntry("key-alias", key, "".toCharArray(),
				new Certificate[] { cert });
		return keystore;

	}

	private static byte[] parseDERFromPEM(byte[] pem, String beginDelimiter,
			String endDelimiter) {
		String data = new String(pem);
		String[] tokens = data.split(beginDelimiter);
		tokens = tokens[1].split(endDelimiter);
		return DatatypeConverter.parseBase64Binary(tokens[0]);
	}

	private static RSAPrivateKey generatePrivateKeyFromDER(byte[] keyBytes)
			throws InvalidKeySpecException, NoSuchAlgorithmException {
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);

		KeyFactory factory = KeyFactory.getInstance("RSA");

		return (RSAPrivateKey) factory.generatePrivate(spec);
	}

	private static X509Certificate generateCertificateFromDER(byte[] certBytes)
			throws CertificateException {
		CertificateFactory factory = CertificateFactory.getInstance("X.509");

		return (X509Certificate) factory
				.generateCertificate(new ByteArrayInputStream(certBytes));
	}

}
