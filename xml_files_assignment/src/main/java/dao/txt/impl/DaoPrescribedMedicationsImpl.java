package dao.txt.impl;

import common.Constants;
import common.config.Configuration;
import dao.txt.DaoPrescribedMedications;
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

public class DaoPrescribedMedicationsImpl implements DaoPrescribedMedications {

    private final Configuration config;

    @Inject
    public DaoPrescribedMedicationsImpl(Configuration config) {
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
    public Either<AppError, List<PrescribedMedication>> getAll() {
        Either<AppError, List<PrescribedMedication>> result;

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
                            medicationList.add(prescribedMedication);
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
        int medicalRecordId = medication.getMedicalRecordId();
        Path file = Path.of(config.getPathPrescribedMedication());
        Either<AppError, List<PrescribedMedication>> eitherAll = this.getAll();

        if (eitherAll.isLeft()) {
            return Either.left(eitherAll.getLeft());
        }

        List<PrescribedMedication> listAllMedications = eitherAll.get();
        List<PrescribedMedication> listAllMedicationsToKeep = filterMedicationsToRemove(listAllMedications, medicalRecordId);

        return writeToPrescribedMedicationsFile(file, listAllMedicationsToKeep);
    }

    private List<PrescribedMedication> filterMedicationsToRemove(List<PrescribedMedication> medications, int medicalRecordId) {
        List<PrescribedMedication> medicationsToKeep = new ArrayList<>();
        for (PrescribedMedication m : medications) {
            if (m.getMedicalRecordId() != medicalRecordId) {
                medicationsToKeep.add(m);
            }
        }
        return medicationsToKeep;
    }

    private Either<AppError, Integer> writeToPrescribedMedicationsFile(Path file, List<PrescribedMedication> medications) {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (PrescribedMedication m : medications) {
                writer.write(m.toStringTextFile());
                writer.write(System.lineSeparator());
            }
            return Either.right(1);
        } catch (IOException e) {
            return Either.left(new AppError(Constants.INTERNAL_ERROR));
        }
    }

}
