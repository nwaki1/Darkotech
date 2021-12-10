package com.darkotech.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Permission.
 */
@Entity
@Table(name = "permission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "permission_name")
    private String permissionName;

    @ManyToMany(mappedBy = "permissions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "permissions" }, allowSetters = true)
    private Set<Auhority> auhorities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Permission id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermissionName() {
        return this.permissionName;
    }

    public Permission permissionName(String permissionName) {
        this.setPermissionName(permissionName);
        return this;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public Set<Auhority> getAuhorities() {
        return this.auhorities;
    }

    public void setAuhorities(Set<Auhority> auhorities) {
        if (this.auhorities != null) {
            this.auhorities.forEach(i -> i.removePermission(this));
        }
        if (auhorities != null) {
            auhorities.forEach(i -> i.addPermission(this));
        }
        this.auhorities = auhorities;
    }

    public Permission auhorities(Set<Auhority> auhorities) {
        this.setAuhorities(auhorities);
        return this;
    }

    public Permission addAuhority(Auhority auhority) {
        this.auhorities.add(auhority);
        auhority.getPermissions().add(this);
        return this;
    }

    public Permission removeAuhority(Auhority auhority) {
        this.auhorities.remove(auhority);
        auhority.getPermissions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Permission)) {
            return false;
        }
        return id != null && id.equals(((Permission) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Permission{" +
            "id=" + getId() +
            ", permissionName='" + getPermissionName() + "'" +
            "}";
    }
}
