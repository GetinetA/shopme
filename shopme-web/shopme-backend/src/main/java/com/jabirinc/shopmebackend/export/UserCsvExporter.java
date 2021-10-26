package com.jabirinc.shopmebackend.export;

import com.jabirinc.shopmecommon.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * Created by Getinet on 9/22/21
 */
public class UserCsvExporter extends AbstractExporter<User> {

    @Override
    public void export(List<User> listUsers, HttpServletResponse response) {

        String fileName = null;
        try(CsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);) {

            fileName = super.createFileName(AbstractExporter.CSV_FILE_EXT, AbstractExporter.EXPORT_USERS_PREFIX);
            super.setResponseHeader(response, AbstractExporter.CONTENT_TYPE_CSV, fileName);

            csvWriter.writeHeader(AbstractExporter.USERS_EXPORT_LABELS);
            for (User user : listUsers) {
                csvWriter.write(user, AbstractExporter.USERS_EXPORT_FIELDS);
            }

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to generate report: " + fileName, ex);
        }
    }
}
