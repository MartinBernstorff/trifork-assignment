package com.trifork.assignment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.trifork.assignment.Model.Org;
import com.trifork.assignment.Model.OrgDTO;

@ExtendWith(SpringExtension.class)
class OrgServiceTest {

    @Mock
    private OrgRepository orgRepository;

    @InjectMocks
    private OrgService orgService;

    @Test
    void testGetOrg() {
        OrgDTO parent = new OrgDTO(1L, Optional.empty(), "Org 1", "Type 1", "Region 1", "Specialty 1", "CVR 1");
        OrgDTO child = new OrgDTO(2L, Optional.of(1L), "Org 2", "Type 2", "Region 2", "Specialty 2", "CVR 2");

        when(orgRepository.findAll()).thenReturn(Arrays.asList(parent, child));
        when(orgRepository.findById(1L)).thenReturn(Optional.of(parent.withChildren(Set.of(2L))));
        when(orgRepository.findAllByParentID(Optional.of(1L))).thenReturn(new HashSet<>(Arrays.asList(child)));

        Optional<Org> result = orgService.getOrganizationWithChildren(1L);
        assertTrue(result.isPresent());
        Org org = result.get();

        assertEquals("Org 1", org.getName());
        assertEquals(1, org.getChildren().size());
        assertTrue(org.getChildren().contains(2L));
    }

    @Test
    void testGetTopmostParent() {
        OrgDTO parent = new OrgDTO(1L, Optional.empty(), "Org 1", "Type 1", "Region 1", "Specialty 1", "CVR 1");
        OrgDTO mid = new OrgDTO(2L, Optional.of(1L), "Org 2", "Type 2", "Region 2", "Specialty 2", "CVR 2");
        OrgDTO child = new OrgDTO(3L, Optional.of(2L), "Org 3", "Type 3", "Region 3", "Specialty 3", "CVR 3");

        when(orgRepository.findById(1L)).thenReturn(Optional.of(parent.withChildren(new HashSet<>())));
        when(orgRepository.findById(2L)).thenReturn(Optional.of(mid.withChildren(new HashSet<>())));
        when(orgRepository.findById(3L)).thenReturn(Optional.of(child.withChildren(new HashSet<>())));

        Org org = orgService.getTopLevelAncestor(3L).get();
        assertEquals("Org 1", org.getName());
    }

    @Test
    void testEmptyOrgHierarchy() {
        when(orgRepository.findAllByParentID(Optional.of(1L))).thenReturn(new HashSet<>());

        Optional<Org> result = orgService.getOrganizationWithChildren(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void testGetTopmostParentNoParent() {
        OrgDTO org = new OrgDTO(1L, Optional.empty(), "Org 1", "Type 1", "Region 1", "Specialty 1", "CVR 1");

        Optional<OrgDTO> expected = Optional.of(org.withChildren(new HashSet<>()));
        when(orgRepository.findById(1L)).thenReturn(expected);

        Optional<Org> result = orgService.getTopLevelAncestor(1L);
        Org topmostParent = result.get();
        assertEquals("Org 1", topmostParent.getName());
    }
}
