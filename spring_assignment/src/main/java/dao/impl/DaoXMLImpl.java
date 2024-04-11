package dao.impl;

import common.Constants;
import common.config.Config;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import lombok.extern.log4j.Log4j2;
import model.error.AppError;
import model.xml.MedicalRecordsXML;

import java.io.File;

@Log4j2
public class DaoXMLImpl implements dao.DaoXML {

    private final Config config;
    @Inject
    public DaoXMLImpl(Config config) {
        this.config = config;
    }

    @Override
    public Either<AppError,Integer> save(MedicalRecordsXML medicalRecordsXML) {
        return writeXML(medicalRecordsXML);
    }

    private Either<AppError, Integer> writeXML(MedicalRecordsXML medicalRecordsXML) {
        Either<AppError, Integer> result;

        File file = new File(config.getProperty(Constants.PATH_XML));

        if (!file.exists()) {
            result = Either.left(new AppError(Constants.FILE_DOES_NOT_EXIST_ERROR));
        } else {

            try {
                JAXBContext context = JAXBContext.newInstance(MedicalRecordsXML.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                marshaller.marshal(medicalRecordsXML, file);

                return Either.right(1);
            } catch (Exception e) {
                log.error(new AppError(Constants.ERROR_WRITING_XML + Constants.BLANK_SPACE + e.getMessage()));
                return Either.left(new AppError(Constants.INTERNAL_ERROR));
            }
        }
        return result;
    }

}
