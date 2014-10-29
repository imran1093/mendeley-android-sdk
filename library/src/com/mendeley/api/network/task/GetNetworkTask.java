package com.mendeley.api.network.task;

import com.mendeley.api.exceptions.HttpResponseException;
import com.mendeley.api.exceptions.JsonParsingException;
import com.mendeley.api.exceptions.MendeleyException;
import com.mendeley.api.network.NetworkUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;

/**
 * A NetworkTask specialised for making HTTP GET requests.
 */
public abstract class GetNetworkTask extends NetworkTask {
    @Override
    protected int getExpectedResponse() {
        return 200;
    }

    @Override
    protected MendeleyException doInBackground(String... params) {
        String url = params[0];
        HttpGet httpGet = null;

        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpGet = NetworkUtils.getApacheDownloadConnection(url, getContentType(), getAccessTokenProvider());

            try {
                HttpResponse response = httpclient.execute(httpGet);
                getResponseHeaders(response);

                final int responseCode = response.getStatusLine().getStatusCode();
                if (responseCode != getExpectedResponse()) {
                    return new HttpResponseException(responseCode, NetworkUtils.getErrorMessage(response));
                } else {
                    HttpEntity entity = response.getEntity();
                    String responseString = EntityUtils.toString(entity, "UTF-8");
                    processJsonString(responseString);
                    return null;
                }
            } catch (IOException e) {
                return new MendeleyException("Error reading server response: " + e.toString(), e);
            } catch (JSONException e) {
                return new JsonParsingException("Error reading server response: " + e.toString(), e);
            }         finally {
                closeConnection();
            }

        } catch (Exception e) {
            return new MendeleyException("Error reading server response: " + e.toString(), e);
        }
    }

    protected abstract void processJsonString(String jsonString) throws JSONException;

    protected abstract String getContentType();
}
