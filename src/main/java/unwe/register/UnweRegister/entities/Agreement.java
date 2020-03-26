package unwe.register.UnweRegister.entities;

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
    private User employer;

    @ManyToOne
    private User coordinator;

    @Column(nullable = false)
    private Long date;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private Long number;

    @OneToMany(mappedBy = "agreement")
    private List<ActivityPlan> activityPlans;

    @PrePersist
    public void onCreate() {
        this.number = System.currentTimeMillis();
    }

}
