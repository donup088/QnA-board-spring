package com.myQnA.QnAboardspring.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Question {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name="fk_question_writer"))
    private User writer;

    private String title;

    private String content;

    private LocalDateTime createDate;

    public Question(){}

    public Question(User writer, String title, String content) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.createDate=LocalDateTime.now();
    }

    public String getFormattedCreateDate(){
        if(createDate==null){
            return "" ;
        }
        return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HH:mm:ss"));
    }

    public void update(String title, String content) {
        this.title=title;
        this.content=content;
    }
}
