package com.example.BotForAdults.storage;

import com.example.BotForAdults.model.Pose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PoseRepository extends JpaRepository<Pose , Integer> {

}
