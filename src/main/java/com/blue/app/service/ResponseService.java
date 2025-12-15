package com.blue.app.service;

import com.blue.app.model.Response;
import com.blue.app.repository.ResponseRepository;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {

    private final ResponseRepository responseRepository;

    public ResponseService(ResponseRepository responseRepository) {
        this.responseRepository = responseRepository;
    }

    public void save(Response response){
        responseRepository.save(response);
    }

}
