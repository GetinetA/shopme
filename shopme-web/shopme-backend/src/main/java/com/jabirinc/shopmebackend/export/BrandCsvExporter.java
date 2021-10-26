package com.jabirinc.shopmebackend.export;

import com.jabirinc.shopmecommon.entity.Brand;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * Created by Getinet on 9/22/21
 */
public class BrandCsvExporter extends AbstractExporter<Brand> {

    @Override
    public void export(List<Brand> listBrands, HttpServletResponse response) {

        String fileName = null;
        try(CsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);) {

            fileName = super.createFileName(AbstractExporter.CSV_FILE_EXT, AbstractExporter.EXPORT_BRAND_PREFIX);
            super.setResponseHeader(response, AbstractExporter.CONTENT_TYPE_CSV, fileName);

            csvWriter.writeHeader(AbstractExporter.BRANDS_EXPORT_LABELS);
            for (Brand brand : listBrands) {
                csvWriter.write(brand, AbstractExporter.BRANDS_EXPORT_FIELDS);
            }

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to generate report: " + fileName, ex);
        }
    }
}
