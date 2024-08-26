package com.trifork.assignment.Loader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.trifork.assignment.Model.OrgDTO;

import java.nio.file.Path;

public class CSVLoader implements SORLoader {
    private OrgDTO[] Organizations;

    public CSVLoader(Path filePath, char delimiter, Charset charset) throws IOException {
        // Error if file does not exist
        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("File does not exist");
        }

        List<OrgDTO> organizationsList = new ArrayList<>();
        try (Reader reader = new InputStreamReader(Files.newInputStream(filePath), charset);
                @SuppressWarnings("deprecation") // withDelimeter is marked as deprecated, but its replacement is not
                                                 // present in this version of commons-csv
                CSVParser csvParser = new CSVParser(reader,
                        CSVFormat.DEFAULT.withDelimiter(delimiter).withFirstRecordAsHeader())) {
            for (CSVRecord csvRecord : csvParser) {
                organizationsList.add(OrgDTO.fromStrings(
                        csvRecord.get("Enhedsnavn"),
                        csvRecord.get("Enhedstype"),
                        csvRecord.get("P_Region"),
                        csvRecord.get("Hovedspeciale"),
                        csvRecord.get("SOR-kode"),
                        csvRecord.get("Parent-SOR-kode"),
                        csvRecord.get("CVR")));
            }

            this.Organizations = organizationsList.toArray(new OrgDTO[0]);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public OrgDTO[] getAll() {
        return this.Organizations;
    }
}
