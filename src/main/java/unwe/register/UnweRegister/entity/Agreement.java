package unwe.register.UnweRegister.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

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

    @OneToOne
    private Employer employer;

    @OneToOne
    private Coordinator coordinator;

    @Column(nullable = false)
    private Long date;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long number;

}
