package com.example.domain.question.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "question")
@FieldNameConstants
public class Question {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    private String groupId;

    private String title;

    private String description;

    private Status status;

    private String createdBy;

    private Instant createdAt;

    private Instant updatedAt;

    @OneToMany
    @JoinColumn(name = "question_id")
    private List<Answer> answers;

    public enum Status {
        OPENED, CLOSED
    }
}
