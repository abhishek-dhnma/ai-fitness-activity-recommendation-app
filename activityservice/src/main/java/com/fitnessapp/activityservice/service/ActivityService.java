package com.fitnessapp.activityservice.service;


import com.fitnessapp.activityservice.dto.ActivityRequest;
import com.fitnessapp.activityservice.dto.ActivityResponse;
import com.fitnessapp.activityservice.model.Activity;
import com.fitnessapp.activityservice.respository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidateService userValidateService;

    private final KafkaTemplate<String, Activity> kafkaTemplate;

    @Value("${kafka.topic.name}")
    public String topicName;

    public ActivityResponse trackActivity(ActivityRequest request) {
        // ✅ Validate before saving
        Boolean validatedUser = userValidateService.validateUser(request.getUserId());

        if (Boolean.FALSE.equals(validatedUser)){
            throw new RuntimeException("Invalid User:" + request.getUserId());
        }

        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepository.save(activity);

        try {

            kafkaTemplate.send(topicName, savedActivity.getUserId(), savedActivity);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity savedActivity) {




        ActivityResponse response = new ActivityResponse();
        response.setId(savedActivity.getId());
        response.setUserId(savedActivity.getUserId());
        response.setType(savedActivity.getType());
        response.setDuration(savedActivity.getDuration());
        response.setStartTime(savedActivity.getStartTime());
        response.setUpdatedAt(savedActivity.getUpdatedAt());
        response.setCreatedAt(savedActivity.getCreatedAt());
        response.setCaloriesBurned(savedActivity.getCaloriesBurned());
        response.setAdditionalMetrics(savedActivity.getAdditionalMetrics());

        return response;
    }
}
