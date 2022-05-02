package ru.alxstn.stackexchangeclient.service;

import ru.alxstn.stackexchangeclient.model.source.ResponseWrapper;

import java.util.Optional;

public interface DataProviderService {
    Optional<ResponseWrapper> searchByTitle(String searchReq, Optional<Integer> pageNumber);
}
