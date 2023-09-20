package random.name.photsapp.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;
import random.name.photsapp.config.json.Views;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(of ={"id"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonView(Views.ChatView.class)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, updatable = false, length = 35)
    @NotBlank
    private String chatIdentity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @CreationTimestamp
    private LocalDateTime created;

    @Column(length = 16)
    private String sender;

    @Column(length = 16)
    @NotBlank
    private String receiver;

    @Column(length = 300)
    @NotBlank
    @Length(min = 3, max = 300)
    private String payload;
}