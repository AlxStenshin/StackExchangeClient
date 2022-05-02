package ru.alxstn.stackexchangeclient.service;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import ru.alxstn.stackexchangeclient.exception.DataProviderException;
import ru.alxstn.stackexchangeclient.model.source.ResponseWrapper;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;


@SpringBootTest
@WireMockTest(httpPort = 9090) // WireMock Declarative Operation Mode
public class StackExchangeIntegrationTest {

    static class FileLoader {
        static String read(String filePath) {
            try {
                var file = ResourceUtils.getFile(filePath);
                return new String(Files.readAllBytes(file.toPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Autowired
    private DataProviderService client;

    @BeforeEach
    public void init() {

        // Stubs for different OK responses with values
        List<String> inputs = List.of("title", "java", "python", "exception", "test");
        for (String input : inputs) {
            stubFor(get(urlPathEqualTo("/2.3/search"))
                    .withQueryParam("order", equalTo("desc"))
                    .withQueryParam("sort", equalTo("activity"))
                    .withQueryParam("site", equalTo("stackoverflow"))
                    .withQueryParam("intitle", equalTo(input))
                    .willReturn(aResponse()
                            .withBody(FileLoader.read("classpath:stackExchangeResponse_" + input + ".json"))
                            .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .withStatus(HttpStatus.OK.value())));
        }

        // Empty OK response Stub
        stubFor(get(urlPathEqualTo("/2.3/search"))
                .withQueryParam("order", equalTo("desc"))
                .withQueryParam("sort", equalTo("activity"))
                .withQueryParam("site", equalTo("stackoverflow"))
                .withQueryParam("intitle", equalTo("some value with no results on stackoverflow"))
                .willReturn(aResponse()
                        .withBody(FileLoader.read("classpath:stackExchangeEmptyResponse.json"))
                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"title", "java", "python", "exception", "test"})
    public void shouldContainTargetStringInEachTitle(String input) {
        ResponseWrapper result = client.searchByTitle(input, Optional.empty()).get();
        int recordsCount = result.getItems().size();
        int resultCount = (int) result
                .getItems()
                .stream()
                .filter(i -> i.getTitle().toLowerCase(Locale.ROOT).contains(input))
                .count();
        assertEquals(resultCount, recordsCount);
    }

    @Test
    public void shouldReturnBadRequest_WithEmptyQuery() {
        stubFor(get(urlPathEqualTo("/2.3/search"))
                .withQueryParam("order", equalTo("desc"))
                .withQueryParam("sort", equalTo("activity"))
                .withQueryParam("site", equalTo("stackoverflow"))
                .withQueryParam("intitle", equalTo(""))
                .willReturn(aResponse().withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.BAD_REQUEST.value())));
        try {
            client.searchByTitle("", Optional.empty()).get();
        } catch (DataProviderException e) {
            assertEquals(HttpStatus.BAD_REQUEST.value(), e.getStatus().value());
        }
    }

    @Test
    public void shouldNotHaveAnyItemsInEmptyResult() {
        assertEquals(0, client.searchByTitle("some value with no results on stackoverflow",
                Optional.empty()).get().getItems().size());
    }

    @Test
    public void shouldNotHaveMoreItemsInEmptyResult() {
        assertEquals(false, client.searchByTitle("some value with no results on stackoverflow",
                Optional.empty()).get().getHasMore());
    }

    @ParameterizedTest
    @ValueSource(strings = {"title", "java", "python", "exception", "test"})
    public void shouldHaveMoreItemsInNonEmptyResult(String input) {
        assertEquals(true, client.searchByTitle(input, Optional.empty()).get().getHasMore());
    }

    @ParameterizedTest
    @ValueSource(strings = {"title", "java", "python", "exception", "test"})
    public void shouldContainThirtyItemsOnMultiPagedResult(String input) {
        assertEquals(30, client.searchByTitle(input, Optional.empty()).get().getItems().size());
    }

    @Test
    public void shouldDecreaseQuotaValueOnEachCall() {
        var javaResult = client.searchByTitle("java", Optional.empty()).get();
        var testResult = client.searchByTitle("test", Optional.empty()).get();

        assertTrue(javaResult.getQuotaRemaining() > testResult.getQuotaRemaining());
    }
}
