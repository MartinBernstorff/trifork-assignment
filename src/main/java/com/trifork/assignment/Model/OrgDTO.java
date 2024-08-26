package com.trifork.assignment.Model;

import java.util.Optional;
import java.util.Set;

import org.springframework.lang.Nullable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * OrgDTO is a Data Transfer Object (DTO) representing an organization. It
 * contains the core properties of an organization such as id, name, type,
 * region, specialty, and cvr.
 * This class does not yet contain information about child organizations. See
 * Org.
 */
@Entity
public class OrgDTO {
    @Id
    private Long id;

    @Nullable
    private Long parentID;

    public String name;
    public String type;
    public String region;
    public String specialty;
    public String cvr;

    public OrgDTO() {
    }

    public OrgDTO(Long id, Optional<Long> parentID, String name, String type, String region, String specialty,
            String cvr) {
        this.id = id;
        this.parentID = parentID.orElse(null);
        this.name = name;
        this.type = type;
        this.region = region;
        this.specialty = specialty;
        this.cvr = cvr;
    }

    public Org withChildren(Set<Long> children) {
        return new Org(this.id, Optional.ofNullable(this.parentID), this.name, this.type, this.region, this.specialty,
                this.cvr, children);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Optional<Long> getParentID() {
        return Optional.ofNullable(parentID);
    }

    private static Optional<Long> parseLongOrNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(Long.parseLong(value.trim()));
    }

    public String getType() {
        return type;
    }

    public String getRegion() {
        return region;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getCvr() {
        return cvr;
    }

    public static OrgDTO fromStrings(String name, String type, String region, String specialty,
            String sorCode, String parentSorCode,
            String cvr) {

        return new OrgDTO(Long.parseLong(sorCode),
                parseLongOrNull(parentSorCode), name, type, region, specialty, cvr);

    }
}
