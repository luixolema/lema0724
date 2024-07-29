package com.lema.test.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@Entity
public class Tool {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String toolCode;

    @Column
    private String brand;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @ToString.Exclude
    private ToolType toolType;
}
