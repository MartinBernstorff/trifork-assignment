package com.trifork.assignment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trifork.assignment.Model.Org;

import java.util.Optional;

@RestController
@RequestMapping("/v1/")
public class OrgController {
	private OrgService orgService;

	public OrgController(OrgService orgService) throws RuntimeException {
		this.orgService = orgService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Org> getOrganization(@PathVariable Long id) {
		// Stream approach. Use whichever is aligned with the codebase.
		return orgService.getOrganizationWithChildren(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Returns the top-level parent organization for the given organization. If the
	 * organization has no parent, returns the organization itself.
	 */
	@GetMapping("/{id}/topmost-parent")
	public ResponseEntity<Org> getTopLevelAncestor(@PathVariable Long id) {
		// Imperative approach. Use whichever is aligned with the codebase.
		Optional<Org> maybeTopmostParent = orgService.getTopLevelAncestor(id);
		if (maybeTopmostParent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(maybeTopmostParent.get());
	}
}
