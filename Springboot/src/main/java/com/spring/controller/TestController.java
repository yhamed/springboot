package com.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
  // , produces = {MediaType.}
    @RequestMapping(method = RequestMethod.GET)
 //   @PreAuthorize("hasAuthority('ADMIN_USER')")
    public ResponseEntity<String> get() {
        return new ResponseEntity<String>("hello world", HttpStatus.OK);
    }
}
