package com.mills.beggarmyneighbour.repositories;

import com.mills.beggarmyneighbour.game.GameStats;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameStatsRepository extends MongoRepository<GameStats, String> {
}
