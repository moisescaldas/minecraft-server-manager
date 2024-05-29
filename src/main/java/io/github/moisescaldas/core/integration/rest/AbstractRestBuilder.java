package io.github.moisescaldas.core.integration.rest;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.UriBuilder;

import io.github.moisescaldas.core.exceptions.ApplicationServerException;

public class AbstractRestBuilder {
    protected URL baseUrl;

    protected Invocation.Builder createRequestBuilder(String resource, Map<String, String> queryParameters, Map<String, Object> headers) {
        try {
            var client = ClientBuilder.newClient();
            var uriBuilder = UriBuilder.fromUri(baseUrl.toURI() + (Objects.nonNull(resource) ? resource : ""));

            if (Objects.nonNull(queryParameters)) {
                queryParameters.entrySet().stream().forEach(entry -> uriBuilder.queryParam(entry.getKey(), entry.getValue()));
            }

            var webTarget = client.target(uriBuilder.build());
            var requestBuilder = webTarget.request();

            if (Objects.nonNull(headers)) {
                headers.entrySet().forEach(entry -> requestBuilder.header(entry.getKey(), entry.getValue()));
            }

            return requestBuilder;
        } catch (URISyntaxException ex) {
            throw new ApplicationServerException(ex);
        }
    }
}
