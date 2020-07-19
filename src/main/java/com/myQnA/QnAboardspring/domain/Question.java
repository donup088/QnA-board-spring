package com.myQnA.QnAboardspring.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Entity
public class Question {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name="fk_question_writer"))
    private User writer;

    private String title;

    @Lob
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question")
    @OrderBy("id ASC")
    private List<Answer> answers;

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

    public boolean isSameWriter(User loginUser) {
        return this.writer.equals(loginUser);
    }

}
