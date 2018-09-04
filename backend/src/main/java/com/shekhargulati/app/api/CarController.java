package com.shekhargulati.app.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;

@RestController
class CarController {

    @GetMapping("/cars")
    @CrossOrigin(origins = "http://localhost:4200")
    public Collection<Car> cars() {
        return Arrays.asList(new Car(new Long(1), "Bmw"), new Car(new Long(2), "Mersedes"));
    }

}