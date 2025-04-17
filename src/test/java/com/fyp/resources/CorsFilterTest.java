package com.fyp.resources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CorsFilterTest {

    private CorsFilter corsFilter;
    private ContainerRequestContext requestContext;
    private ContainerResponseContext responseContext;
    private MultivaluedMap<String, Object> headers;

    @BeforeEach
    public void setUp() {
        corsFilter = new CorsFilter();
        requestContext = mock(ContainerRequestContext.class);
        responseContext = mock(ContainerResponseContext.class);
        headers = new DummyMultivaluedMap<>();
        when(responseContext.getHeaders()).thenReturn(headers);
    }

    @Test
    void filter_ShouldAddCORSHeaders_WhenOriginIsAllowed() throws IOException {
        when(requestContext.getHeaderString("Origin")).thenReturn("http://localhost:3000");
        when(requestContext.getMethod()).thenReturn("GET");

        corsFilter.filter(requestContext, responseContext);

        assertEquals("http://localhost:3000", headers.getFirst("Access-Control-Allow-Origin"));
        assertEquals("GET, POST, PUT, DELETE, OPTIONS", headers.getFirst("Access-Control-Allow-Methods"));
        assertEquals("Content-Type, Authorization", headers.getFirst("Access-Control-Allow-Headers"));
        assertEquals("true", headers.getFirst("Access-Control-Allow-Credentials"));
    }

    @Test
    void filter_ShouldNotAddHeaders_WhenOriginIsNotAllowed() throws IOException {
        when(requestContext.getHeaderString("Origin")).thenReturn("http://not-allowed.com");
        when(requestContext.getMethod()).thenReturn("GET");

        corsFilter.filter(requestContext, responseContext);

        assertEquals(0, headers.size()); // No headers should be added
    }

    @Test
    void filter_ShouldSetStatusTo200_ForOptionsPreflightRequest() throws IOException {
        when(requestContext.getHeaderString("Origin")).thenReturn("https://fyp.quran-mem-tool.pro");
        when(requestContext.getMethod()).thenReturn("OPTIONS");

        corsFilter.filter(requestContext, responseContext);

        verify(responseContext).setStatus(Response.Status.OK.getStatusCode());
        assertEquals("https://fyp.quran-mem-tool.pro", headers.getFirst("Access-Control-Allow-Origin"));
    }

    // Helper class to simulate a MultivaluedMap
    private static class DummyMultivaluedMap<K, V> extends HashMap<K, java.util.List<V>> implements MultivaluedMap<K, V> {
        @Override
        public void putSingle(K key, V value) {
            this.put(key, new java.util.ArrayList<>(java.util.Collections.singletonList(value)));
        }

        @Override
        public void add(K key, V value) {
            this.computeIfAbsent(key, k -> new java.util.ArrayList<>()).add(value);
        }

        @Override
        public V getFirst(K key) {
            java.util.List<V> list = this.get(key);
            return (list != null && !list.isEmpty()) ? list.get(0) : null;
        }

        @Override
        public void addAll(K key, V... newValues) {
            this.computeIfAbsent(key, k -> new java.util.ArrayList<>()).addAll(java.util.Arrays.asList(newValues));
        }

        @Override
        public void addAll(K key, java.util.List<V> valueList) {
            this.computeIfAbsent(key, k -> new java.util.ArrayList<>()).addAll(valueList);
        }

        @Override
        public void addFirst(K key, V value) {
            this.computeIfAbsent(key, k -> new java.util.ArrayList<>()).add(0, value);
        }

        @Override
        public boolean equalsIgnoreValueOrder(MultivaluedMap<K, V> otherMap) {
            return this.equals(otherMap);
        }
    }
}
