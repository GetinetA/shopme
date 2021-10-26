package com.jabirinc.shopmebackend.export;

import com.jabirinc.shopmecommon.entity.Category;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * Created by Getinet on 9/22/21
 */
public class CategoryCsvExporter extends AbstractExporter<Category> {

    @Override
    public void export(List<Category> listCategories, HttpServletResponse response) {

        String fileName = null;
        try(CsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);) {

            fileName = super.createFileName(AbstractExporter.CSV_FILE_EXT, AbstractExporter.EXPORT_CATEGORY_PREFIX);
            super.setResponseHeader(response, AbstractExporter.CONTENT_TYPE_CSV, fileName);

            csvWriter.writeHeader(AbstractExporter.CATEGORIES_EXPORT_LABELS);
            for (Category category : listCategories) {
                category.setName(category.getName().replace("--", "  "));
                csvWriter.write(category, AbstractExporter.CATEGORIES_EXPORT_FIELDS);
            }

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to generate report: " + fileName, ex);
        }
    }
}
