package com.tookancustomer.retrofit2;


import com.tookancustomer.BuildConfig;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.TlsVersion;

public class TLSSocketFactory extends SSLSocketFactory {
    private SSLSocketFactory delegate;

    TLSSocketFactory() throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, null, null);
        delegate = context.getSocketFactory();
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return delegate.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return delegate.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        return enableTLSOnSocket(delegate.createSocket(s, host, port, autoClose));
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        return enableTLSOnSocket(delegate.createSocket(host, port));
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        return enableTLSOnSocket(delegate.createSocket(host, port, localHost, localPort));
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return enableTLSOnSocket(delegate.createSocket(host, port));
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return enableTLSOnSocket(delegate.createSocket(address, port, localAddress, localPort));
    }

    private Socket enableTLSOnSocket(Socket socket) {
        if ((socket instanceof SSLSocket)) {
            try {
                SSLParameters sslParameters = SSLContext.getDefault().getDefaultSSLParameters();
                boolean isTLS3 = false;
                ArrayList<String> protocolList = null;
                if (sslParameters != null && sslParameters.getProtocols() != null) {
                    protocolList = new ArrayList<>(Arrays.asList(sslParameters.getProtocols()));
                }

                if (protocolList != null) {
                    for (String protocol : protocolList) {
                        if (protocol.equalsIgnoreCase(TlsVersion.TLS_1_3.javaName())) {
                            isTLS3 = true;
                            break;
                        }
                    }
                }

                if (protocolList != null && protocolList.size() > 0 && isTLS3) {
                    ((SSLSocket) socket).setEnabledProtocols(new String[]{TlsVersion.TLS_1_2.javaName()
                            , TlsVersion.TLS_1_3.javaName(), TlsVersion.TLS_1_1.javaName()
                            , TlsVersion.TLS_1_0.javaName()});
                    Log.e(TLSSocketFactory.class.getName(), "TLS 1.3");
                } else {
                    ((SSLSocket) socket).setEnabledProtocols(new String[]{TlsVersion.TLS_1_2.javaName()
                            , TlsVersion.TLS_1_1.javaName(), TlsVersion.TLS_1_0.javaName()});
                }

            } catch (Exception e) {

                               Utils.printStackTrace(e);
                try {
                    ((SSLSocket) socket).setEnabledProtocols(new String[]{TlsVersion.TLS_1_2.javaName(), TlsVersion.TLS_1_1.javaName(), TlsVersion.TLS_1_0.javaName()});
                } catch (Exception e1) {
//                    e1.printStackTrace();
                    try {
                        ((SSLSocket) socket).setEnabledProtocols(new String[]{TlsVersion.TLS_1_2.javaName(), TlsVersion.TLS_1_1.javaName()});
                    } catch (Exception e2) {
//                        e2.printStackTrace();
                        try {
                            ((SSLSocket) socket).setEnabledProtocols(new String[]{TlsVersion.TLS_1_1.javaName(), TlsVersion.TLS_1_0.javaName()});
                        } catch (Exception e4) {
//                            e4.printStackTrace();
                            try {
                                ((SSLSocket) socket).setEnabledProtocols(new String[]{TlsVersion.TLS_1_0.javaName()});
                            } catch (Exception e5) {
//                                e5.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return socket;
    }
}
