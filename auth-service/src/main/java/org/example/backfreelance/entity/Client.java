package org.example.backfreelance.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.backfreelance.enums.ClientType;

@Entity
@Table(name = "clients")
@DiscriminatorValue("CLIENT")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Client extends User{

    private String companyName;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ClientType clientType = ClientType.INDIVIDUAL;

    private String website;
    private String sector;

    @Builder.Default
    private Integer totalProjectsPosted = 0;

    @Builder.Default
    private Integer totalProjectsCompleted = 0;

    @Builder.Default
    private Double averageRating = 0.0;

    @Builder.Default
    private Boolean publicProfile = true;
}
