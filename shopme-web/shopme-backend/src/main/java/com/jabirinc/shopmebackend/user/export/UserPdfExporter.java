package com.jabirinc.shopmebackend.user.export;

import com.jabirinc.shopmecommon.entity.User;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Created by Getinet on 9/22/21
 */
public class UserPdfExporter extends AbstractExporter<User> {

    @Override
    public void export(List<User> listUser, HttpServletResponse response) {

        String fileName = null;

        try (Document document = new Document(PageSize.A4)) {

            fileName = super.createFileName(AbstractExporter.PDF_FILE_EXT, AbstractExporter.EXPORT_USERS_PREFIX);
            super.setResponseHeader(response, MediaType.APPLICATION_PDF_VALUE, fileName);

            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setSize(18);
            font.setColor(Color.BLUE);

            Paragraph paragraph = new Paragraph("List of User", font);
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(paragraph);
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100f);
            table.setSpacingBefore(10);
            table.setWidths(new float[] {1.2f, 3.5f, 3.0f, 3.0f, 3.0f, 1.7f});

            writeTableHeader(table);
            writeTableData(table, listUser);

            document.add(table);

        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to generate report: " + fileName, ex);
        }
    }

    private void writeTableHeader(PdfPTable table) {

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        for (String s : AbstractExporter.USERS_EXPORT_LABELS) {
            cell.setPhrase(new Phrase(s, font));
            table.addCell(cell);
        }
    }

    private void writeTableData(PdfPTable table, List<User> listUser) {
        for (User user : listUser) {
            table.addCell(String.valueOf(user.getId()));
            table.addCell(user.getEmail());
            table.addCell(user.getFirstName());
            table.addCell(user.getLastName());
            table.addCell(user.getRoles().toString());
            table.addCell(String.valueOf(user.isEnabled()));
        }
    }

}
