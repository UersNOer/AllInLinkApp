package net.vpnsdk.vpn;

import android.util.Log;

import net.vpnsdk.vpn.Common.VpnError;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;

class SSLAuthServerCert {

    static final int SOCKET_TIMEOUT = 60000;
    private static final HostnameVerifier HOSTNAME_VERIFIER = HttpsURLConnection
            .getDefaultHostnameVerifier();
    private static String Tag = "SSLAuthCert";
    SSLSocket mSslSock = null;
    private SSLSocketFactory mSslSocketFactory;
    private X509TrustManager mDefTrustManager;

    /**
     * Authenticate the certificate from server side.
     */
    public SSLAuthServerCert() {
        createDefaultTrustManager();
        initSslSocketFactory();
    }

    private static X509TrustManager findX509TrustManager(TrustManager[] tms) {
        for (TrustManager tm : tms) {
            if (tm instanceof X509TrustManager) {
                return (X509TrustManager) tm;
            }
        }
        return null;
    }

    private static void printCertificate(X509Certificate cert) {
        X500Principal subject = cert.getSubjectX500Principal();
        X500Principal issuer = cert.getIssuerX500Principal();
        Log.d(Tag, "subject " + subject.getName());
        Log.d(Tag, "issuer " + issuer.getName());
    }

    private void initSslSocketFactory() {
        // here, trust managers is a single trust-all manager
        TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs,
                                           String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs,
                                           String authType) {
            }
        }};

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, null);
            mSslSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            Log.e(Tag, "" + e.getMessage());
            e.printStackTrace();
        } catch (KeyManagementException e) {
            Log.e(Tag, "" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createDefaultTrustManager() {
        try {
            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory
                    .getInstance(algorithm);
            tmf.init((KeyStore) null);
            TrustManager[] tms = tmf.getTrustManagers();
            mDefTrustManager = findX509TrustManager(tms);
        } catch (NoSuchAlgorithmException e) {
            Log.e(Tag, "" + e.getMessage());
            e.printStackTrace();
        } catch (KeyStoreException e) {
            Log.e(Tag, "" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void closeSocketThrowException(SSLSocket socket, String errorMessage)
            throws IOException {
        Log.i(Tag, "validation error: " + errorMessage);

        if (socket != null) {
            SSLSession session = socket.getSession();
            if (session != null) {
                session.invalidate();
            }

            socket.close();
        }

        throw new SSLHandshakeException(errorMessage);
    }

    /**
     * Performs the handshake and server certificates validation Notice a new
     * chain will be rebuilt by tracing the issuer and subject before calling
     * checkServerTrusted(). And if the last traced certificate is self issued
     * and it is expired, it will be dropped.
     *
     * @param sslSocket The secure connection socket
     * @param domain    The website domain
     * @return An SSL error object if there is an error and null otherwise
     */
    public int doHandshakeAndValidateServerCertificates(
            SSLSocket sslSocket, String domain) throws IOException {
        // get a valid SSLSession, close the socket if we fail
        Log.i(Tag, "get sslSession " + sslSocket.getInetAddress().getHostAddress());
        sslSocket.startHandshake();
        SSLSession sslSession = sslSocket.getSession();
        if (!sslSession.isValid()) {
            closeSocketThrowException(sslSocket,
                    "failed to perform SSL handshake");
        }
        Log.i(Tag, "sslSession PeerPrincipal: "
                + sslSession.getPeerPrincipal().getName());

        // retrieve the chain of the server peer certificates
        Certificate[] peerCertificates = sslSocket.getSession()
                .getPeerCertificates();

        if (peerCertificates == null || peerCertificates.length == 0) {
            Log.e(Tag, "failed to retrieve peer certificates");
            if (sslSocket != null) {
                SSLSession session = sslSocket.getSession();
                if (session != null) {
                    session.invalidate();
                }

                sslSocket.close();
            }
            return VpnError.ERR_CERT_SERVER_NO;
        }

        return verifyServerDomainAndCertificates(
                (X509Certificate[]) peerCertificates, domain, "RSA", sslSession);
    }

    /**
     * Common code of doHandshakeAndValidateServerCertificates and
     * verifyServerCertificates. Calls DomainNamevalidator to verify the domain,
     * and TrustManager to verify the certs.
     *
     * @param chain    the cert chain in X509 cert format.
     * @param domain   The full website hostname and domain
     * @param authType The authentication type for the cert chain
     * @return An SSL error object if there is an error and null otherwise
     */
    private int verifyServerDomainAndCertificates(X509Certificate[] chain,
                                                  String domain, String authType, SSLSession sess) throws IOException {
        // check if the first certificate in the chain is for this site
        X509Certificate currCertificate = chain[0];
        if (currCertificate == null) {
            throw new IllegalArgumentException(
                    "certificate for this site is null");
        }

        printCertificate(currCertificate);

        if (!HOSTNAME_VERIFIER.verify(domain, sess)) {
            Log.e(Tag, "certificate not for this host: " + domain);
            return VpnError.ERR_CERT_SERVER_UNTRUSTED;
        }
        Log.d(Tag, "Domain trusted");
        try {
            mDefTrustManager.checkServerTrusted(chain, authType);
            Log.d(Tag, "Server Trusted");
            return VpnError.ERR_SUCCESS;
        } catch (CertificateException e) {
            Log.e(Tag, "" + e.getClass().getName());
            Log.e(Tag,
                    "failed to validate the certificate chain, error: "
                            + e.getMessage());
            if (e instanceof CertificateExpiredException) {
                return VpnError.ERR_CERT_EXPIRED;
            }

            return VpnError.ERR_CERT_SERVER_UNTRUSTED;
        }
    }

    public synchronized int verifyServerCertificate(String host, int port) {
        int result = VpnError.ERR_SUCCESS;
        try {
            mSslSock = (SSLSocket) mSslSocketFactory.createSocket();
            mSslSock.connect(new InetSocketAddress(host, port));
            mSslSock.setSoTimeout(SOCKET_TIMEOUT);
            mSslSock.setUseClientMode(true);
            mSslSock.setEnabledCipherSuites(mSslSock.getEnabledCipherSuites());
            mSslSock.setEnabledProtocols(mSslSock.getSupportedProtocols());

            result = doHandshakeAndValidateServerCertificates(mSslSock, host);

            mSslSock.close();
        } catch (IOException e) {
            String errmsg = e.getMessage();
            Log.e(Tag, "verifyServerCertificate: " + e.getMessage());
            if (mSslSock != null) {
                try {
                    mSslSock.close();
                } catch (IOException e1) {
                    Log.e(Tag, "verifyServerCertificate" + e1.getMessage());
                }
            }

            if (errmsg == null || errmsg.contains("Network is unreachable")) {
                result = VpnError.ERR_SOCK_CONN;
            } else {
                result = VpnError.ERR_SSL_CONN;
            }
        } catch (Exception e) {
            Log.e(Tag, "verifyServerCertificate Exception: " + e.getMessage());
            result = VpnError.ERR_SSL_CONN;
        }
        return result;
    }

    public void asyncVerifyServerCertificate(String host, int port) {
        final String fhost = host;
        final int fport = port;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                verifyServerCertificate(fhost, fport);
            }
        });
        t.start();
    }

    public void stopVerify() {
        if (null != mSslSock) {
            try {
                Log.d(Tag, "stopVerify");
                mSslSock.close();
            } catch (IOException e) {
                Log.e(Tag, "stopVerify " + e.getMessage());
            }
        }
    }
}
