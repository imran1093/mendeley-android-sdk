package com.mendeley.api.network;

import com.mendeley.api.auth.AccessTokenProvider;

import org.apache.http.HttpConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Utilities for the NetworkProviders.
 */
public class NetworkUtils {
    public static final String API_URL = "https://api.mendeley.com/";

    /**
     * Extends HttpEntityEnclosingRequestBase to provide PATCH request method.
     */
    public static class HttpPatch extends HttpEntityEnclosingRequestBase {
        public final static String METHOD_NAME = "PATCH";

        public HttpPatch() {
            super();
        }

        public HttpPatch(final URI uri) {
            super();
            setURI(uri);
        }

        public HttpPatch(final String uri) {
            super();
            setURI(URI.create(uri));
        }

        @Override
        public String getMethod() {
            return METHOD_NAME;
        }
    }

    /**
     * Creates an error message string from a given URLConnection object,
     * which includes the response code, response message and the error stream from the server
     *
     * @param con the URLConnection object
     * @return the error message string
     */
    public static String getErrorMessage(HttpsURLConnection con) {
        String message = "";
        InputStream is = null;
        try {
            message = con.getResponseCode() + " "  + con.getResponseMessage();
            is = con.getErrorStream();
            String responseString = "";
            if (is != null) {
                responseString = getJsonString(is);
            }
            message += "\n" + responseString;
        } catch (IOException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                }
            }
        }
        return message;
    }

    /**
     * Creates an error message string from a given HttpResponse object,
     * which includes the response code, response message and the error stream from the server
     *
     * @return the error message string
     */
    public static String getErrorMessage(HttpResponse response) {
        String message = "";
        InputStream is = null;
        try {
            message = response.getStatusLine().getStatusCode() + " "  + response.getStatusLine().getReasonPhrase();
            is = response.getEntity().getContent();
            String responseString = "";
            if (is != null) {
                responseString = getJsonString(is);
            }
            message += "\n" + responseString;
        } catch (IOException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                }
            }
        }
        return message;
    }

    /**
     * @param date if-Unmodified-Since date, or null
     */
    public static HttpPatch getHttpPatch(String url, String date, String contentType, AccessTokenProvider accessTokenProvider) {
        HttpPatch httpPatch = new HttpPatch(url);
        httpPatch.setHeader("Authorization", "Bearer " + accessTokenProvider.getAccessToken());
        httpPatch.setHeader("Content-type", contentType);
        if (date != null) {
            httpPatch.setHeader("If-Unmodified-Since", date);
        }

        return httpPatch;
    }

    /**
     * Creating HttpsURLConnection object with the given url and request method.
     * Also adding the access token and other required request properties.
     *
     * @param url the call url
     * @param method the required request method
     * @return the HttpsURLConnection object
     * @throws IOException
     */
    public static HttpsURLConnection getConnection(String url, String method, AccessTokenProvider accessTokenProvider) throws IOException {
        final HttpsURLConnection con = getDownloadConnection(url, method);
        con.addRequestProperty("Authorization", "Bearer " + accessTokenProvider.getAccessToken());

        return con;
    }

    /**
     * Creating HttpsURLConnection object with the given url and request method
     * without the authorization header. This is used for downloading a file from the server.
     *
     * @param url the call url
     * @param method the required request method
     * @return the HttpsURLConnection object
     * @throws IOException
     */
    public static HttpsURLConnection getDownloadConnection(String url, String method) throws IOException {
        final URL callUrl = new URL(url);
        final HttpsURLConnection con = (HttpsURLConnection) callUrl.openConnection();
        con.setReadTimeout(3000);
        con.setConnectTimeout(3000);
        con.setRequestMethod(method);

        con.setSSLSocketFactory(new TSLOnlySocketFactory(con.getSSLSocketFactory()));
        return con;
    }

    public static HttpURLConnection getHttpDownloadConnection(String url, String method) throws IOException {
        final URL callUrl = new URL(url);
        final HttpURLConnection con = (HttpURLConnection) callUrl.openConnection();
        con.setReadTimeout(3000);
        con.setConnectTimeout(3000);
        con.setRequestMethod(method);

        return con;
    }

    /**
     * Extracting json String from the given InputStream object.
     *
     * @param stream the InputStream holding the json string
     * @return the json string
     * @throws IOException
     */
    public static String getJsonString(InputStream stream) throws IOException {
        StringBuffer data = new StringBuffer();
        InputStreamReader isReader = null;
        BufferedReader br = null;

        try {
            isReader = new InputStreamReader(stream);
            br = new BufferedReader(isReader);
            String brl = "";
            while ((brl = br.readLine()) != null) {
                data.append(brl);
            }

        } finally {
            stream.close();
            isReader.close();
            br.close();
        }

        return data.toString();
    }

    public static HttpGet getApacheDownloadConnection(String url, String contentType, AccessTokenProvider accessTokenProvider) throws IOException {
        HttpGet httpGet = new HttpGet(url);

        if (accessTokenProvider != null) {
            httpGet.setHeader("Authorization", "Bearer " + accessTokenProvider.getAccessToken());
        }

        if (contentType != null) {
            httpGet.setHeader("Content-type", contentType);
        }

        return httpGet;
    }
}
