package dao.impl;

import common.Constants;
import common.config.Configuration;
import dao.DaoPrescribedMedication;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.PrescribedMedication;
import model.error.AppError;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.APPEND;

public class DaoPrescribedMedicationImpl implements DaoPrescribedMedication {

    private final Configuration config;

    @Inject
    public DaoPrescribedMedicationImpl(Configuration config) {
        this.config = config;
    }

    //get all prescribed medication by medicalRecordId

    @Override
    public Either<AppError, List<PrescribedMedication>> getAll(PrescribedMedication medication) {
        Either<AppError, List<PrescribedMedication>> result;
        int medicalRecordId = medication.getMedicalRecordId();

        Path file = Path.of(config.getPathPrescribedMedication());
        List<PrescribedMedication> medicationList = new ArrayList<>();

        if (!file.toFile().exists()) {
            result = Either.left(new AppError(Constants.FILE_DOES_NOT_EXIST_ERROR));
        } else {
            try (BufferedReader br = Files.newBufferedReader(file)) {
                String st;

                if (Files.size(file) == 0) {
                    result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NO_DATA));
                } else {
                    while ((st = br.readLine()) != null) {
                        if (!st.trim().isEmpty()) {
                            PrescribedMedication prescribedMedication = new PrescribedMedication(st);
                            if (prescribedMedication.getMedicalRecordId() == medicalRecordId) {
                                medicationList.add(prescribedMedication);
                            }
                        }
                    }
                    if (medicationList.isEmpty()) {
                        result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NO_DATA));
                    } else {
                        result = Either.right(medicationList);
                    }
                }
            } catch (IOException e) {
                result = Either.left(new AppError(Constants.INTERNAL_ERROR));
            }
        }
        return result;
    }

    @Override
    public Either<AppError, Integer> save(PrescribedMedication medication) {
        Either<AppError, Integer> result;

        Path file = Path.of(config.getPathPrescribedMedication());
        if (!file.toFile().exists()) {
            result = Either.left(new AppError(Constants.FILE_DOES_NOT_EXIST_ERROR));
        } else {
            try (BufferedWriter bw = Files.newBufferedWriter(file, APPEND)) {

                bw.write(medication.toStringTextFile());
                bw.write(System.lineSeparator());
                result = Either.right(1);

            } catch (IOException e) {
                result = Either.left(new AppError(Constants.INTERNAL_ERROR));
            }
        }
        return result;
    }

    //delete medication by medicalRecordId
    @Override
    public Either<AppError, Integer> delete(PrescribedMedication medication) {
        Either<AppError, Integer> result;
        int medicalRecordId = medication.getMedicalRecordId();

        Path file = Path.of(config.getPathPrescribedMedication());
        Either<AppError, List<PrescribedMedication>> either = this.getAll(medication);
        String emptyFile = "";

        if (either.isRight()) {

            List<PrescribedMedication> listAllMedications = either.get();

            if (!file.toFile().exists()) {
                result = Either.left(new AppError(Constants.FILE_DOES_NOT_EXIST_ERROR));
            } else {

                try (BufferedWriter writer = Files.newBufferedWriter(file, APPEND)) {
                    writer.write(emptyFile);
                } catch (IOException e) {
                    result = Either.left(new AppError(Constants.INTERNAL_ERROR));
                }
                try (BufferedWriter writer = Files.newBufferedWriter(file, APPEND)) {
                    for (PrescribedMedication m : listAllMedications) {
                        if (m.getMedicalRecordId() != medicalRecordId) {
                            writer.write(m.toStringTextFile());
                            writer.write(System.lineSeparator());
                        }
                    }
                    result = Either.right(1);
                } catch (IOException e) {
                    result = Either.left(new AppError(Constants.INTERNAL_ERROR));
                }
            }
        } else {
            result = Either.left(either.getLeft());
        }
        return result;
    }
}
