package com.demo.consumer.feign;


import org.springframework.web.bind.annotation.GetMapping;

//@org.springframework.cloud.openfeign.FeignClient(name="provider-service", url="http://localhost:8081")
@org.springframework.cloud.openfeign.FeignClient(name="provider")
public interface ProviderFeignClient {

    @GetMapping("/instance-info")
    String getInstanceInfo ();
}
