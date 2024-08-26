package com.trifork.assignment.Loader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.trifork.assignment.Model.OrgDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.*;

class CSVLoaderTest {

    @Test
    void testGetAll(@TempDir Path tempDir) throws IOException {
        Path testFile = tempDir.resolve("csvOrgRepo.csv");
        String csvContent = "Enhedsnavn;Enhedstype;P_Region;Hovedspeciale;SOR-kode;unused-header;Parent-SOR-kode;CVR\n"
                +
                // Test parsing
                "test-enhedsnavn;test-enhedstype;test-region;test-specialty;1;unused-value;2;test-cvr\n"
                +
                // Test nullable parent SOR code
                "test-enhedsnavn;test-enhedstype;test-region;test-specialty;1;unused-value;;test-cvr";
        Files.writeString(testFile, csvContent);

        CSVLoader csvRepository = new CSVLoader(testFile, ';', Charset.defaultCharset());
        OrgDTO[] organizations = csvRepository.getAll();

        assertEquals(2, organizations.length);
        OrgDTO org = organizations[0];
        OrgDTO nullParentSorOrg = organizations[1];
        assertAll(
                () -> assertEquals("test-enhedsnavn", org.getName()),
                () -> assertEquals("test-enhedstype", org.getType()),
                () -> assertEquals("test-region", org.getRegion()),
                () -> assertEquals("test-specialty", org.getSpecialty()),
                () -> assertEquals(1L, org.getId()),
                () -> assertEquals(2L, org.getParentID().get()),
                () -> assertEquals("test-cvr", org.getCvr()),
                () -> assertTrue(nullParentSorOrg.getParentID().isEmpty()));
    }

    @Test
    void testLoadSor() throws IOException {
        Path testFile = Path.of("src/main/resources/sor.csv");
        CSVLoader csvRepository = new CSVLoader(testFile, ';', Charset.defaultCharset());
        OrgDTO[] organizations = csvRepository.getAll();
        assertTrue(organizations.length > 0);
    }
}
