package edu.uph.ii.springbootprj.domain;

import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="opinie")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Review {

    @Min(0) @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Drug drug;

    @Min(0) @Max(5)
    private byte rating;

    @NotBlank @NotEmpty @Size(min = 2, max = 200)
    private String content;

    @CreationTimestamp
    @DateTimeFormat(pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime createdAt;

}
