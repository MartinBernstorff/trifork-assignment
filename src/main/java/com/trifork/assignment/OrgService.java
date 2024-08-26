package com.trifork.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trifork.assignment.Model.Org;
import com.trifork.assignment.Model.OrgDTO;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrgService {
    @Autowired
    private OrgRepository orgRepository;

    public void populateOrgs(OrgDTO[] orgs) {
        for (OrgDTO org : orgs) {
            orgRepository.save(org);
        }
    }

    private Optional<OrgDTO> getOrg(Long sorCode) {
        return orgRepository.findById(sorCode);
    }

    public Optional<Org> getOrganizationWithChildren(Long sorCode) {
        return getOrg(sorCode)
                .map(org -> {
                    Set<Long> childIDs = orgRepository.findAllByParentID(Optional.of(sorCode))
                            .stream()
                            .map(OrgDTO::getId)
                            .collect(Collectors.toSet());
                    return org.withChildren(childIDs);
                });
    }

    private Optional<OrgDTO> getParent(Long sorCode) {
        return getOrg(sorCode)
                .flatMap(OrgDTO::getParentID)
                .flatMap(this::getOrg);
    }

    public Optional<Org> getTopLevelAncestor(Long sorCode) {
        Optional<OrgDTO> maybeOrg = getOrg(sorCode);
        if (!maybeOrg.isPresent()) {
            return Optional.empty();
        }
        OrgDTO org = maybeOrg.get();

        Optional<OrgDTO> maybeParent = getParent(sorCode);
        while (maybeParent.isPresent()) {
            org = maybeParent.get();
            long orgId = org.getId();
            maybeParent = getParent(orgId);
        }

        long orgId = org.getId();
        return getOrganizationWithChildren(orgId);
    }
}
