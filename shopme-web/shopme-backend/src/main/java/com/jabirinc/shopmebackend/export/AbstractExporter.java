package com.jabirinc.shopmebackend.export;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Getinet on 9/22/21
 */
public abstract class AbstractExporter<T> {

    public static final String[] USERS_EXPORT_LABELS =
            new String[]{"User Id", "Email", "First Name", "Last Name", "Roles", "Enabled"};
    public static final String[] USERS_EXPORT_FIELDS =
            new String[] {"id", "email", "firstName", "lastName", "roles", "enabled"};

    public static final String EXPORT_FILE_PREFIX = "users_";
    public static final String CSV_FILE_EXT = ".csv";
    public static final String EXCEL_FILE_EXT = ".xlsx";
    public static final String PDF_FILE_EXT = ".pdf";
    public static final String CONTENT_TYPE_CSV = "text/csv";
    public static final String FILE_NAME_DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";

    public void setResponseHeader(HttpServletResponse response, String contentType, String fileName) {

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;

        response.setContentType(contentType);
        response.setHeader(headerKey, headerValue);
    }

    public String createFileName(String fileExtension) {
        DateFormat dateFormat = new SimpleDateFormat(FILE_NAME_DATE_FORMAT);
        String timestamp = dateFormat.format(new Date());
        String fileName = EXPORT_FILE_PREFIX + timestamp + fileExtension;
        return fileName;
    }

    public abstract void export(List<T> data, HttpServletResponse response);


    /*public static void handleResourceShutdown( List<AutoCloseable> resources) {

        for (AutoCloseable resource: resources) {
            try {
                if (resource != null) {
                    resource.close();
                }
            } catch (Exception e) {
                //log.debug("Warning: error closing resources.");
            }
        }
    }*/
}