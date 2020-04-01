package unwe.register.UnweRegister.entities;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Table(name = "activity_plans")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class ActivityPlan {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String uid;

    @ManyToOne
    @NonNull
    private Agreement agreement;

    @Column(nullable = false)
    @NonNull
    private String description;

    @OneToMany(mappedBy = "activityPlan")
    private List<Event> events;
}
