package com.ecommerce.user.controllers;


import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;
//    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    private ResponseEntity<List<UserResponse>> getAllUsers ()
    {
        return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<UserResponse> getUser (@PathVariable String id)
    {
        log.info("Request received for user : {}", id);

        log.trace("This is TRACE level - Very detailed logs");
        log.debug("This is DEBUG level - Used for development debugging");
        log.info("This is INFO level - General system information");
        log.warn("This is WARN level - Something might be wrong");
        log.error("This is ERROR level - Something failed");




        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createUser (@RequestBody UserRequest userRequest)
    {
        userService.addUser(userRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully!!");
    }

    @PutMapping("/{id}")
   private ResponseEntity<String> updateUser (@PathVariable String id, @RequestBody UserRequest updatedUserRequest)
   {
       boolean updated = userService.upateUser(id, updatedUserRequest);

       if (updated)
           return ResponseEntity.ok("User updated successfully");
       return ResponseEntity.notFound().build();
   }

}
