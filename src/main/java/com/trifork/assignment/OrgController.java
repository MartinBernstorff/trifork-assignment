package com.trifork.assignment;

import java.nio.charset.Charset;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trifork.assignment.Loader.CSVLoader;
import com.trifork.assignment.Loader.SORLoader;
import com.trifork.assignment.Model.Org;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

@RestController
@RequestMapping("/v1/")
public class OrgController {
	private OrgService orgService;

	public OrgController(OrgService orgService) throws RuntimeException {
		this.orgService = orgService;
	}

	@PostConstruct
	public void init() {
		try {
			SORLoader organizations = new CSVLoader(Path.of("src/main/resources/sor.csv"),
					';', Charset.forName("windows-1252"));
			this.orgService.populateOrgs(organizations.getAll());
		} catch (IOException e) {
			throw new RuntimeException("Failed to load sor codes", e);
		}
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
	public ResponseEntity<Org> getTopmostParent(@PathVariable Long id) {
		// Imperative approach. Use whichever is aligned with the codebase.
		Optional<Org> maybeTopmostParent = orgService.getTopmostParent(id);
		if (maybeTopmostParent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(maybeTopmostParent.get());
	}
}
