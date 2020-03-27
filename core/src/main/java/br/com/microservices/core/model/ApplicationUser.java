package br.com.microservices.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationUser implements AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull(message = "Titulo é obrigatorio")
    @Column(nullable = false)
    private String username;

    @NotNull(message = "Password é obrigatorio")
    @Column(nullable = false)
    @ToString.Exclude
    private String password;

    @NotNull(message = "Role é obrigatorio")
    @Column(nullable = false)
    private String role = "USER";

    @Override
    public Long getId() {
        return null;
    }

    public ApplicationUser(@NotNull ApplicationUser applicationUser) {
        this.id = applicationUser.getId();
        this.username = applicationUser.getUsername();
        this.password = applicationUser.getPassword();
        this.role = applicationUser.getRole();
    }
}
