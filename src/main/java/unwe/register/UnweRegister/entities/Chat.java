package unwe.register.UnweRegister.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "chats", uniqueConstraints=
@UniqueConstraint(columnNames={"employer_uid", "coordinator_uid"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String uid;

    @ManyToOne
    @NonNull
    private User coordinator;

    @ManyToOne
    @NonNull
    private User employer;

}
