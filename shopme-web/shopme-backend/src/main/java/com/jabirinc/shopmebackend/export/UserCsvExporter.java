package com.jabirinc.shopmebackend.export;

import com.jabirinc.shopmecommon.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Getinet on 9/22/21
 */
public class UserCsvExporter {

    public static final String EXPORT_FILE_PREFIX = "users_";
    public static final String CSV_FILE_EXT = ".csv";
    public static final String MEDIA_TYPE_CSV = "text/csv";
    public static final String FILE_NAME_DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";

    public void export(List<User> listUsers, HttpServletResponse response) {

        String fileName = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(FILE_NAME_DATE_FORMAT);
            String timestamp = dateFormat.format(new Date());
            fileName = EXPORT_FILE_PREFIX + timestamp + CSV_FILE_EXT;

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=" + fileName;

            response.setContentType(MEDIA_TYPE_CSV);
            response.setHeader(headerKey, headerValue);
            CsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
            String[] csvHeader = {"User ID", "Email", "First Name", "Last Name", "Roles", "Enabled"};
            String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};

            csvWriter.writeHeader(csvHeader);
            for (User user : listUsers) {
                csvWriter.write(user, fieldMapping);
            }
            csvWriter.close();

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to generate report: " + fileName, ex);
        }
    }
}
