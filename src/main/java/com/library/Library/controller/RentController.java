package com.library.Library.controller;

import com.library.Library.dto.RentDTO;
import com.library.Library.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rent")
public class RentController {
    private RentService rentService;

    @Autowired
    public RentController(RentService rentService){
        this.rentService = rentService;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<String> rentBook(@PathVariable Long bookId, @RequestBody RentDTO rentDTO){
        return null;
    }
}
