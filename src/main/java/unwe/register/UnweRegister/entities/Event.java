package unwe.register.UnweRegister.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Table(name = "events")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String uid;

    @ManyToOne
    @JoinColumn(name = "activityPlan")
    private ActivityPlan activityPlan;

    @Column(nullable = false)
    private String description;
}
