
package com.github.alef_torres.file.exporter.factory;

import com.github.alef_torres.exception.BadRequestException;
import com.github.alef_torres.file.exporter.contract.FileExporter;
import com.github.alef_torres.file.exporter.impl.CsvExporter;
import com.github.alef_torres.file.exporter.impl.XlsxExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileExporterFactory {

    private Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

    @Autowired
    private ApplicationContext applicationContext;

    public FileExporter getImporter(String acceptHeader) throws Exception {
        if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)) {
            logger.info("Loading XLSX file");
            return applicationContext.getBean(XlsxExporter.class);
            //return new XlsxImporter();
        }

        if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
            logger.info("Loading CSV file");
            return applicationContext.getBean(CsvExporter.class);
            //return new CsvImporter();
        }

        throw new BadRequestException("Invalid file format");
    }
}
