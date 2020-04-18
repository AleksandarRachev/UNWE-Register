package unwe.register.UnweRegister.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Table(name = "events")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Event {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String uid;

    @ManyToOne
    @JoinColumn(name = "activityPlan")
    @NonNull
    private ActivityPlan activityPlan;

    @Column(nullable = false)
    @NonNull
    private String title;

    @Column(nullable = false, length = 2000)
    @NonNull
    private String description;

    @Lob
    private byte[] image;

    private Long madeOn;

    @PrePersist
    private void onCreate() {
        this.madeOn = System.currentTimeMillis();
    }
}
