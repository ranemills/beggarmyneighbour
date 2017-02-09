package com.mills.beggarmyneighbour;

import com.mills.beggarmyneighbour.game.GamePlay;
import com.mills.beggarmyneighbour.game.GameStats;
import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Player;
import com.mills.beggarmyneighbour.repositories.GameStatsRepository;
import com.mills.beggarmyneighbour.utils.CardOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class BeggarMyNeighbourApplication {

    private static final Logger logger = LoggerFactory.getLogger(BeggarMyNeighbourApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BeggarMyNeighbourApplication.class);


    }


}
