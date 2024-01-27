package com.example.BotForAdults.model;



import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Data
@Table(name = "pose")
public class Pose {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pose_id")
    private Long id;

    @Column(name = "pose_name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "pose_url", nullable = false)
    private String poseUrl;
}
