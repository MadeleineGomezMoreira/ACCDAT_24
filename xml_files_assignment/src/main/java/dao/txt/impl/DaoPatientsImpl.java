package dao.txt.impl;

import common.Constants;
import common.config.Configuration;
import dao.txt.DaoPatients;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Patient;
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

public class DaoPatientsImpl implements DaoPatients {

    private final Configuration config;

    @Inject
    public DaoPatientsImpl(Configuration config) {
        this.config = config;
    }

    @Override
    public Either<AppError, List<Patient>> getAll() {
        Either<AppError, List<Patient>> result;

        Path file = Paths.get(config.getPathPatients());
        List<Patient> patientList = new ArrayList<>();

        if (!file.toFile().exists()) {
            result = Either.left(new AppError(Constants.FILE_DOES_NOT_EXIST_ERROR));
        } else {

            try (BufferedReader br = Files.newBufferedReader(file)) {
                String st;

                //check if the file is empty first
                if (Files.size(file) == 0) {
                    result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NO_DATA));
                } else {
                    //read the file line by line and create a new Patient object for each line
                    while ((st = br.readLine()) != null) {
                        if (!st.trim().isEmpty()) { //skip empty lines
                            Patient patient = new Patient(st);
                            patientList.add(patient);
                        }
                    }
                    result = Either.right(patientList);
                }
            } catch (Exception e) {
                result = Either.left(new AppError(Constants.INTERNAL_ERROR));
            }
        }
        return result;
    }

    //get patient by id
    @Override
    public Either<AppError, Patient> get(Patient patient) {
        Either<AppError, Patient> result;

        Path file = Path.of(config.getPathPatients());
        Patient patientToReturn = null;

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
                            Patient newPatient = new Patient(st);
                            if (newPatient.getId() == patient.getId()) {
                                patientToReturn = newPatient;
                            }
                        }
                    }
                    if(patientToReturn == null) {
                        result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
                    } else {
                        result = Either.right(patientToReturn);
                    }
                }
            } catch (Exception e) {
                result = Either.left(new AppError(Constants.INTERNAL_ERROR));
            }
        }
        return result;
    }

    //delete patient

    @Override
    public Either<AppError, Integer> delete(Patient patient) {
        Either<AppError, Integer> result;
        int patientId = patient.getId();

        Path file = Paths.get(config.getPathPatients());
        Either<AppError, List<Patient>> either = this.getAll();
        String emptyFile = "";

        if (either.isRight()) {

            List<Patient> patientList = either.get();

            if (!file.toFile().exists()) {
                result = Either.left(new AppError(Constants.FILE_DOES_NOT_EXIST_ERROR));
            } else {
                try (BufferedWriter bw = Files.newBufferedWriter(file, TRUNCATE_EXISTING)) {
                    bw.write(emptyFile);
                } catch (IOException e) {
                    result = Either.left(new AppError(Constants.INTERNAL_ERROR));
                }
                try (BufferedWriter bw = Files.newBufferedWriter(file, APPEND)) {
                    for (Patient p : patientList) {
                        if (p.getId() != patientId) {
                            bw.write(p.toStringTextFile());
                            bw.write(System.lineSeparator());
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
