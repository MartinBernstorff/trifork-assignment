package com.trifork.assignment;

import java.nio.charset.Charset;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.trifork.assignment.Loader.CSVLoader;
import com.trifork.assignment.Model.Org;
import com.trifork.assignment.Model.OrgDTO;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class OrgService {
    private final OrgRepository orgRepository;

    public OrgService(OrgRepository orgRepository, Environment env) {
        this.orgRepository = orgRepository;

        if (env == null) {
            // In non SpringBoot test
            return;
        }

        try {
            CSVLoader organizations = new CSVLoader(Path.of("src/main/resources/sor.csv"),
                    ';', Charset.forName("windows-1252"));
            this.populateOrgs(organizations.getAll());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load sor codes", e);
        }

    }

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
