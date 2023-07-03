package org.example.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "years")
    private Integer years;

    @Column(name = "city")
    private String city;

    @Column(name = "times")
    private Integer times;
    @Column(name = "gender")
    private String gender;
    @Column(name = "date_of_birth")
    private Date date_of_birth;


    // Геттеры и сеттеры

}