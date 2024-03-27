package dao.impl;

import common.Constants;
import common.config.Configuration;
import dao.DaoMedicalRecords;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MedicalRecord;
import model.error.AppError;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class DaoMedicalRecordsImpl implements DaoMedicalRecords {

    private final Configuration config;

    @Inject
    public DaoMedicalRecordsImpl(Configuration config) {
        this.config = config;
    }

    //get all medical records by patient id
    @Override
    public Either<AppError, List<MedicalRecord>> getAll(MedicalRecord record) {
        Either<AppError, List<MedicalRecord>> result;
        int patientId = record.getPatientId();

        Path file = Paths.get(config.getPathMedicalRecords());
        List<MedicalRecord> recordList = new ArrayList<>();


        if (!file.toFile().exists()) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
        } else {
            try (BufferedReader br = Files.newBufferedReader(file)) {
                String st;

                if (Files.size(file) == 0) {
                    result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NO_DATA));
                } else {
                    while ((st = br.readLine()) != null) {
                        if (!st.trim().isEmpty()) {
                            MedicalRecord medicalRecord = new MedicalRecord(st);
                            if (medicalRecord.getPatientId() == patientId) {
                                recordList.add(medicalRecord);
                            }
                        }
                    }

                    if (recordList.isEmpty()) {
                        result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NO_DATA));
                    } else {
                        result = Either.right(recordList);
                    }
                }

            } catch (Exception e) {
                result = Either.left(new AppError(Constants.INTERNAL_ERROR));
            }
        }
        return result;
    }

    @Override
    public Either<AppError, List<MedicalRecord>> getAll() {
        Either<AppError, List<MedicalRecord>> result;

        Path file = Paths.get(config.getPathMedicalRecords());
        List<MedicalRecord> recordList = new ArrayList<>();


        if (!file.toFile().exists()) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
        } else {
            try (BufferedReader br = Files.newBufferedReader(file)) {
                String st;

                if (Files.size(file) == 0) {
                    result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NO_DATA));
                } else {
                    while ((st = br.readLine()) != null) {
                        if (!st.trim().isEmpty()) {
                            MedicalRecord medicalRecord = new MedicalRecord(st);
                            recordList.add(medicalRecord);
                        }
                    }
                    if (recordList.isEmpty()) {
                        result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NO_DATA));
                    } else {
                        result = Either.right(recordList);
                    }
                }

            } catch (Exception e) {
                result = Either.left(new AppError(Constants.INTERNAL_ERROR));
            }
        }
        return result;
    }

    @Override
    public Either<AppError, Integer> save(MedicalRecord record) {
        Either<AppError, Integer> result;

        Path file = Path.of(config.getPathMedicalRecords());
        if (!file.toFile().exists()) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
        } else {
            try (BufferedWriter bw = Files.newBufferedWriter(file, APPEND)) {

                bw.write(record.toStringTextFile());
                bw.write(System.lineSeparator());
                result = Either.right(1);

            } catch (IOException e) {
                result = Either.left(new AppError(Constants.INTERNAL_ERROR));
            }
        }
        return result;
    }

    //delete all medical records by patientId
    @Override
    public Either<AppError, Integer> delete(MedicalRecord record) {
        Either<AppError, Integer> result;
        int patientId = record.getPatientId();

        Path file = Paths.get(config.getPathMedicalRecords());
        Either<AppError, List<MedicalRecord>> either = this.getAll();
        String emptyFile = "";

        if (either.isRight()) {

            List<MedicalRecord> listAllRecords = either.get();

            if (!file.toFile().exists()) {
                result = Either.left(new AppError(Constants.FILE_DOES_NOT_EXIST_ERROR));
            } else {

                try (BufferedWriter writer = Files.newBufferedWriter(file, TRUNCATE_EXISTING)) {
                    writer.write(emptyFile);
                } catch (IOException e) {
                    result = Either.left(new AppError(Constants.INTERNAL_ERROR));
                }
                try (BufferedWriter writer = Files.newBufferedWriter(file, APPEND)) {
                    for (MedicalRecord r : listAllRecords) {
                        if (r.getPatientId() != patientId) {
                            writer.write(r.toStringTextFile());
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
