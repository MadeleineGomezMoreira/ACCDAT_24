package dao.impl;

import common.Constants;
import common.config.Configuration;
import dao.DaoDoctors;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Doctor;
import model.error.AppError;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DaoDoctorsImpl implements DaoDoctors { //TODO: CHECK IF THIS IS CORRECT

    private final Configuration config;

    @Inject
    public DaoDoctorsImpl(Configuration config) {
        this.config = config;
    }

    //get doctor by id
    @Override
    public Either<AppError, Doctor> get(Doctor doctor) {
        Either<AppError, Doctor> result;

        Path file = Path.of(config.getPathDoctors());
        Doctor doctorToReturn = null;

        if (!file.toFile().exists()) {
            result = Either.left(new AppError(Constants.FILE_DOES_NOT_EXIST_ERROR));
        } else {
            try {
                if (Files.size(file) == 0) {
                    result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NO_DATA));
                } else {
                    List<String> lines = Files.readAllLines(file);
                    for (String st : lines) {
                        if (!st.trim().isEmpty()) {
                            Doctor newDoctor = new Doctor(st);
                            if (newDoctor.getId() == doctor.getId()) {
                                doctorToReturn = newDoctor;
                            }
                        }
                    }
                    if(doctorToReturn == null) {
                        result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
                    } else {
                        result = Either.right(doctorToReturn);
                    }
                }
            } catch (Exception e) {
                result = Either.left(new AppError(Constants.INTERNAL_ERROR));
            }
        }
        return result;
    }
}
