package random.name.photsapp.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(of ={"id"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, updatable = false, length = 35)
    private String chatIdentity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @CreationTimestamp
    private LocalDateTime created;

    @Column(length = 16)
    private String sender;

    @Column(length = 16)
    private String receiver;

    @Column(length = 300)
    private String payload;
}