package ru.practicum.shareit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.itemRequest.ItemRequestClient;
import ru.practicum.shareit.user.UserClient;

@Configuration

public class WebClientConfig {

    @Value("${shareit-server.url}")
    private String serviceUrl;

    private static final String API_PREFIX_BOOKINGS = "/bookings";
    private static final String API_PREFIX_ITEMS = "/items";
    private static final String API_PREFIX_USERS = "/users";
    private static final String API_PREFIX_IR = "/requests";

    @Bean
    public BookingClient bookingClient(RestTemplateBuilder builder) {
        var restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serviceUrl + API_PREFIX_BOOKINGS))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
        BookingClient client = new BookingClient(restTemplate);
        return client;
    }

    @Bean
    public ItemClient itemClient(RestTemplateBuilder builder) {
        var restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serviceUrl + API_PREFIX_ITEMS))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
        ItemClient client = new ItemClient(restTemplate);
        return client;
    }

    @Bean
    public UserClient UserClient(RestTemplateBuilder builder) {
        var restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serviceUrl + API_PREFIX_USERS))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
        UserClient client = new UserClient(restTemplate);
        return client;
    }

    @Bean
    public ItemRequestClient ItemRequestClient(RestTemplateBuilder builder) {
        var restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serviceUrl + API_PREFIX_IR))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
        ItemRequestClient client = new ItemRequestClient(restTemplate);
        return client;
    }
}
