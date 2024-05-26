package com.library.Library.controller;

import com.library.Library.dto.RegisterDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {
    @PostMapping("/allowUser")
    public ResponseEntity<String> allow(){
        return new ResponseEntity<>("Allow USER only", HttpStatus.OK);
    }
}
