package random.name.photsapp.entities;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import random.name.photsapp.config.json.Views;

@Entity
@Data
@JsonView(Views.PostView.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;

    @Column(nullable = false)
    private String author;
}