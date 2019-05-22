package io.github.jhipster.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Execution.
 */
@Entity
@Table(name = "execution")
public class Execution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "planned", nullable = false)
    private LocalDate planned;

    @Column(name = "done")
    private LocalDate done;

    @Column(name = "done_comment")
    private String doneComment;

    @ManyToOne
    @JsonIgnoreProperties("executions")
    private Diagnostic diagnostic;

    @ManyToOne
    @JsonIgnoreProperties("executions")
    private Person person;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPlanned() {
        return planned;
    }

    public Execution planned(LocalDate planned) {
        this.planned = planned;
        return this;
    }

    public void setPlanned(LocalDate planned) {
        this.planned = planned;
    }

    public LocalDate getDone() {
        return done;
    }

    public Execution done(LocalDate done) {
        this.done = done;
        return this;
    }

    public void setDone(LocalDate done) {
        this.done = done;
    }

    public String getDoneComment() {
        return doneComment;
    }

    public Execution doneComment(String doneComment) {
        this.doneComment = doneComment;
        return this;
    }

    public void setDoneComment(String doneComment) {
        this.doneComment = doneComment;
    }

    public Diagnostic getDiagnostic() {
        return diagnostic;
    }

    public Execution diagnostic(Diagnostic diagnostic) {
        this.diagnostic = diagnostic;
        return this;
    }

    public void setDiagnostic(Diagnostic diagnostic) {
        this.diagnostic = diagnostic;
    }

    public Person getPerson() {
        return person;
    }

    public Execution person(Person person) {
        this.person = person;
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Execution)) {
            return false;
        }
        return id != null && id.equals(((Execution) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Execution{" +
            "id=" + getId() +
            ", planned='" + getPlanned() + "'" +
            ", done='" + getDone() + "'" +
            ", doneComment='" + getDoneComment() + "'" +
            "}";
    }
}
