package com.myQnA.QnAboardspring.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
public class Answer {
    @Id
    @GeneratedValue
    @JsonProperty
    private Long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name="fk_answer_writer"))
    @JsonProperty
    private User writer;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name="fk_answer_to_question"))
    @JsonProperty
    private Question question;

    @Lob
    @JsonProperty
    private String content;

    private LocalDateTime createDate;

    public Answer(){}

    public Answer(User writer,Question question, String content) {
        this.writer = writer;
        this.content = content;
        this.question=question;
        this.createDate=LocalDateTime.now();
    }

    public String getFormattedCreateDate(){
        if(createDate==null){
            return "" ;
        }
        return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HH:mm:ss"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(id, answer.id) &&
                Objects.equals(writer, answer.writer) &&
                Objects.equals(content, answer.content) &&
                Objects.equals(createDate, answer.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer, content, createDate);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writer=" + writer +
                ", content='" + content + '\'' +
                ", createDate=" + createDate +
                '}';
    }

    public boolean isSameWriter(User loginUser) {
        return loginUser.equals(this.writer);
    }
}
