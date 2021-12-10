package com.darkotech.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Auhority.
 */
@Entity
@Table(name = "auhority")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Auhority implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "auhority_name")
    private String auhorityName;

    @ManyToMany
    @JoinTable(
        name = "rel_auhority__permission",
        joinColumns = @JoinColumn(name = "auhority_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "auhorities" }, allowSetters = true)
    private Set<Permission> permissions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Auhority id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuhorityName() {
        return this.auhorityName;
    }

    public Auhority auhorityName(String auhorityName) {
        this.setAuhorityName(auhorityName);
        return this;
    }

    public void setAuhorityName(String auhorityName) {
        this.auhorityName = auhorityName;
    }

    public Set<Permission> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Auhority permissions(Set<Permission> permissions) {
        this.setPermissions(permissions);
        return this;
    }

    public Auhority addPermission(Permission permission) {
        this.permissions.add(permission);
        permission.getAuhorities().add(this);
        return this;
    }

    public Auhority removePermission(Permission permission) {
        this.permissions.remove(permission);
        permission.getAuhorities().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Auhority)) {
            return false;
        }
        return id != null && id.equals(((Auhority) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Auhority{" +
            "id=" + getId() +
            ", auhorityName='" + getAuhorityName() + "'" +
            "}";
    }
}
