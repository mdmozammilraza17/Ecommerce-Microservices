package com.ecommerce.order.controller;


import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @GetMapping("/message")
    @RateLimiter(name = "reteBreaker", fallbackMethod = "getMessageFallback")
    public String getMessage ()
    {
        return "Order created!!";
    }

    public String getMessageFallback (Exception e)
    {
        return "Hello Fallback";
    }
}
