package com.mills.beggarmyneighbour;

import com.mills.beggarmyneighbour.run.GameRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class BeggarMyNeighbourApplication {

    public static void main(String[] args) {
        new GameRunner().run();
    }
}
