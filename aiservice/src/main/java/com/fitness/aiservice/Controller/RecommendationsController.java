package com.fitness.aiservice.Controller;


import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class RecommendationsController {

     private final RecommendationService recommendationService;

    // for any particular user all activity fetching from this endpoint
     @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>>getUserRecommendation(@PathVariable String userId){

         return ResponseEntity.ok(recommendationService.getUserRecommendation(userId));
     }

    // for any particular activity fetching from this endpoint
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recommendation> getAvtivityRecommendation(@PathVariable String activityId){

        return ResponseEntity.ok(recommendationService.getActivityRecommendation(activityId));


    }




}
