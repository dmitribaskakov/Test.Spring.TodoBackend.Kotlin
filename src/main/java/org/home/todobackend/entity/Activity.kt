package org.home.todobackend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.util.Objects;

/*
Вся активность пользователя (активация аккаунта, другие действия по необходимости)
*/

@Entity
@Table(name = "activity", schema = "todolist")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)

public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activated", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean activated;  // 1 = true, 0 = false

    @Column(updatable = false)
    private String uuid;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", activated=" + activated +
                ", uuid='" + uuid + '\'' +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return id.equals(activity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}