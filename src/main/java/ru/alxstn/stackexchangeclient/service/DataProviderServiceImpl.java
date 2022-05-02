package ru.alxstn.stackexchangeclient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import ru.alxstn.stackexchangeclient.exception.DataProviderException;
import ru.alxstn.stackexchangeclient.model.source.ResponseWrapper;

import java.util.Optional;

@Service
public class DataProviderServiceImpl implements DataProviderService {
    private final String stackExchangeUrl;
    private final RestTemplate template;

    @Autowired
    public DataProviderServiceImpl(@Value("${stackexchange.url}") final String stackExchangeUrl) {
        this.stackExchangeUrl = stackExchangeUrl;
        // Apache Http Client decodes ZIP-compressed responses on fly.
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());
        template = new RestTemplate(clientHttpRequestFactory);
    }

    private String buildUrl(String searchReq) {
        if ((searchReq == null) || (searchReq.isEmpty()))
            throw new DataProviderException(HttpStatus.BAD_REQUEST, "Please set title to search");
        return  stackExchangeUrl +
                "/2.3/search?" +
                "order=desc&" +
                "sort=activity&" +
                "site=stackoverflow&" +
                "intitle=" + searchReq;
    }

    private String buildPagedUrl(String searchReq, int pageNumber) {
        return buildUrl(searchReq) + "&page=" + pageNumber;
    }

    private ResponseWrapper search(String url) {
        ResponseWrapper resp = new ResponseWrapper();
        try {
            resp = template.getForObject(url, ResponseWrapper.class);
        } catch (HttpClientErrorException httpException) {
            throw new DataProviderException(httpException.getStatusCode(), httpException.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Search Exception: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public Optional<ResponseWrapper> searchByTitle(String searchReq, Optional<Integer> pageNumber) {
        return pageNumber.map(integer -> search(buildPagedUrl(searchReq, integer))).or(() -> Optional.of(search(buildUrl(searchReq))));
    }
}
