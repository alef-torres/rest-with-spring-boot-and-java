package com.github.alef_torres.file.importer.factory;

import com.github.alef_torres.exception.BadRequestException;
import com.github.alef_torres.file.importer.contract.FileImporter;
import com.github.alef_torres.file.importer.impl.CsvImporter;
import com.github.alef_torres.file.importer.impl.XlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileImporterFactory {

    private Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

    @Autowired
    private ApplicationContext applicationContext;

    public FileImporter getImporter(String fileName) throws Exception {
        if (fileName.endsWith(".xlsx")) {
            logger.info("Loading XLSX file");
            return applicationContext.getBean(XlsxImporter.class);
            //return new XlsxImporter();
        }

        if (fileName.endsWith(".csv")) {
            logger.info("Loading CSV file");
            return applicationContext.getBean(CsvImporter.class);
            //return new CsvImporter();
        }

        throw new BadRequestException("Invalid file format");
    }
}
