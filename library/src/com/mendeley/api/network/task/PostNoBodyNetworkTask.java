package com.mendeley.api.network.task;

import com.mendeley.api.exceptions.HttpResponseException;
import com.mendeley.api.exceptions.JsonParsingException;
import com.mendeley.api.exceptions.MendeleyException;
import com.mendeley.api.network.NetworkUtils;
import com.mendeley.api.network.task.NetworkTask;

import java.io.IOException;

import static com.mendeley.api.network.NetworkUtils.getConnection;
import static com.mendeley.api.network.NetworkUtils.getErrorMessage;

public abstract class PostNoBodyNetworkTask extends NetworkTask {
    @Override
    protected int getExpectedResponse() {
        return 204;
    }

    @Override
    protected MendeleyException doInBackground(String... params) {
        String url = params[0];
        try {
            con = getConnection(url, "POST", getAccessTokenProvider());
            con.connect();

            getResponseHeaders(null);

            final int responseCode = con.getResponseCode();
            if (responseCode != getExpectedResponse()) {
                return new HttpResponseException(responseCode, NetworkUtils.getErrorMessage(con));
            } else {
                return null;
            }
        }	catch (IOException e) {
            return new JsonParsingException(e.getMessage());
        } finally {
            closeConnection();
        }
    }
}
