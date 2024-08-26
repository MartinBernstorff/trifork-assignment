package com.trifork.assignment.Model;

import java.util.Optional;
import java.util.Set;

/**
 * The Org class represents an organization with children.
 * It extends from ChildlessOrg and includes additional information about its
 * children.
 */
public class Org extends OrgDTO {
    Set<Long> children;

    public Org(Long id, Optional<Long> parentID, String name, String type, String region, String specialty,
            String cvr, Set<Long> children) {
        super(id, parentID, name, type, region, specialty, cvr);
        this.children = children;
    }

    public Set<Long> getChildren() {
        return children;
    }
}
