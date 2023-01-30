package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

import guru.qa.model.Color;

public class FilesParsingTest {
    ClassLoader cl = FilesParsingTest.class.getClassLoader();

    @Test
    void zipParseTest() throws Exception {
        try (
                InputStream resource = cl.getResourceAsStream("TestZIP.zip");
                ZipInputStream zis = new ZipInputStream(resource);
        ) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if ((entry.getName()).contains(".pdf")) {
                    PDF pdfContent = new PDF(zis);
                    assertThat(pdfContent.text).contains("Dummy PDF file");
                } else if ((entry.getName()).contains(".csv")) {
                    CSVReader reader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> csvContent = reader.readAll();
                    assertThat(csvContent.get(2)[1]).contains("Manila");
                } else if ((entry.getName()).contains(".xlsx")) {
                    XLS xlsContent = new XLS(zis);
                    assertThat(xlsContent.excel.getSheetAt(0).getRow(0).getCell(1).getStringCellValue()).contains("First Name");
                }
            }
        }
    }

    @Test
    void jsonParseTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try (
                InputStream resource = cl.getResourceAsStream("color.json");
                InputStreamReader reader = new InputStreamReader(resource)
        ) {
            Color color = objectMapper.readValue(reader, Color.class);
            assertThat(color.name).isEqualTo("black");
            assertThat(color.isDefault).isFalse();
            assertThat(color.type).isEqualTo("primary");
            assertThat(color.code.rgba.get(0)).isEqualTo(255);
            assertThat(color.code.hex).isEqualTo("#000");

        }
    }

}
