package be.mauricecantaert.ewdjproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EwdjProjectApplicationTests {

    @Test
    void contextLoads() {
    }

    // TODO isbn13 test:
    /*
    *     public static void main(String[] args) {
        List<String> validISBNs = List.of(
                "978-1-60309-   520-4",
                "978-1-60309508-2",
                "978-1-a60309-038-4",
                "978-1-891830c-02-0",
                "9781603093255"
        );

        validISBNs.forEach(t -> System.out.println(validateChecksum(t)));
    }
    * */

}
