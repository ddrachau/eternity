package com.prodyna.pac.eternity.server.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name="project")
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
  /*      if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Abteilung abteilung = (Abteilung) o;

        if (id != null ? !id.equals(abteilung.id) : abteilung.id != null) return false;
        if (name != null ? !name.equals(abteilung.name) : abteilung.name != null) return false;
*/
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Abteilung{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
