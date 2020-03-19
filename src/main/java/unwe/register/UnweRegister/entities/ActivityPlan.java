package unwe.register.UnweRegister.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Table(name = "activity_plans")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPlan {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String uid;

    @ManyToOne
    private Agreement agreement;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "activityPlan")
    private List<Event> events;
}
