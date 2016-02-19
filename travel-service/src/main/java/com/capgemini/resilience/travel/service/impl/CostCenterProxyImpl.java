package com.capgemini.resilience.travel.service.impl;

import java.util.List;
import javax.inject.Named;

import com.capgemini.resilience.travel.rest.CostCenterTO;
import com.capgemini.resilience.travel.service.CostCenterProxy;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by kso on 18.02.16.
 */
@Named
public class CostCenterProxyImpl implements CostCenterProxy {

    @Value("${costcenter.service.url}")
    private String url;

    @Override
    @HystrixCommand(fallbackMethod = "getFallbackCostCenter")
    public CostCenterTO get(int number) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<List<CostCenterTO>> exchange = restTemplate.exchange(
                url + "/costcenter?number=" + number,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CostCenterTO>>() {
                });

        if (!exchange.getBody().isEmpty())
            return exchange.getBody().iterator().next();
        else
            return null;
    }


    public CostCenterTO getFallbackCostCenter(int number) {
        return new CostCenterTO(number, "fallback");
    }
}
