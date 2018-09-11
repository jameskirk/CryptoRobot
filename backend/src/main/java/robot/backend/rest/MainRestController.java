package robot.backend.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;

@RestController
public class MainRestController {

    @RequestMapping("/")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:4200")
    public String welcome() {
        return "Welcome to Spring Boot + REST + JWT Example.";
    }

    @RequestMapping(value = "/cars")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:4200")
    public Collection<Car> cars() {
        return Arrays.asList(new Car(new Long(1), "Bmw"), new Car(new Long(2), "Mersedes"));
    }
}
