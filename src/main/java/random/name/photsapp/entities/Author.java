package random.name.photsapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import random.name.photsapp.config.json.AuthorSetSerializer;
import random.name.photsapp.config.json.Views;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(of = { "username" })
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.BaseAuthor.class)
    private long id;

    @Column(unique = true)
    @JsonView(Views.BaseAuthor.class)
    private String username;

    @JsonIgnore
    private String password;

    @JsonView(Views.BaseAuthor.class)
    private String avatarUrl;


    @JsonView(Views.FullAuthor.class)
    @JsonSerialize(using = AuthorSetSerializer.class)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "author_subscriptions",
            joinColumns = @JoinColumn(name = "current_author"),
            inverseJoinColumns = @JoinColumn(name ="author"))

    private Set<Author> subscriptions = new HashSet<>();


    @JsonView(Views.FullAuthor.class)
    @JsonSerialize(using = AuthorSetSerializer.class)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "author_subscriptions",
            joinColumns = @JoinColumn(name = "author"),
            inverseJoinColumns = @JoinColumn(name ="current_author"))

    private Set<Author> subscribers = new HashSet<>();
}