package be.eafcuccle.tshirtshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the T-shirt Shop application.
 * This class bootstraps the Spring Boot application.
 */
@SpringBootApplication
public class TshirtShopApplication {

    /**
     * The main method which starts the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(TshirtShopApplication.class, args);
    }
}
