package com.trifork.assignment;

import org.springframework.data.repository.CrudRepository;

import com.trifork.assignment.Model.OrgDTO;

import java.util.HashSet;
import java.util.Optional;

public interface OrgRepository extends CrudRepository<OrgDTO, Long> {
    public HashSet<OrgDTO> findAllByParentID(Optional<Long> parentID);
}
