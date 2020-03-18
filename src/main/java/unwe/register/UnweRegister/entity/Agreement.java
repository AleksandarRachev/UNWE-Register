package unwe.register.UnweRegister.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Table(name = "agreements")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agreement {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String uid;

    @ManyToOne
    private Employer employer;

    @ManyToOne
    private Coordinator coordinator;

    @Column(nullable = false)
    private Long date;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long number;

    @OneToMany(mappedBy = "agreement")
    private List<ActivityPlan> activityPlans;

}
