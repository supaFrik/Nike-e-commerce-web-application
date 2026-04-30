package vn.demo.nike.shared.domain;

import java.time.LocalDateTime;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
