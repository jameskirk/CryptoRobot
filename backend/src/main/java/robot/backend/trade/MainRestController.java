package robot.backend.trade;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;

@RestController
public class MainRestController {

    @RequestMapping("/")
    @ResponseBody
    public String welcome() {
        return "Welcome to Spring Boot + REST + JWT Example.";
    }

    @RequestMapping(value = "/cars")
    @ResponseBody
    public Collection<Car> cars() {
        return Arrays.asList(new Car(new Long(1), "Bmw"), new Car(new Long(2), "Mersedes"));
    }

    public static class Car {
        private Long id;
        private String name;

        public Car(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
