package ui.methods;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.PrescribedMedication;
import model.dtos.MedicalRecordDTO;
import model.error.AppError;
import services.MedicalRecordsService;

import java.time.LocalDate;
import java.util.List;

public class SaveRecordWithMedication {

    private final MedicalRecordsService service;

    @Inject
    public SaveRecordWithMedication(MedicalRecordsService service) {
        this.service = service;
    }

    public void saveRecordWithMedication() {

        List<PrescribedMedication> medicationList = List.of(
                new PrescribedMedication(1, "Ibuprofen", "500mg", 9),
                new PrescribedMedication(2, "Acetaminophen", "500gm", 9)
        );

        MedicalRecordDTO medicalRecordDTO =
                new MedicalRecordDTO(
                        9,
                        LocalDate.now(),
                        "broken leg",
                        2,
                        3,
                        medicationList
                );

        Either<AppError,Integer> either = service.save(medicalRecordDTO);

        if (either.isRight()) {
            System.out.println("Medical record added successfully!");
            System.out.println(medicalRecordDTO);

        } else if (either.isLeft()) {
            System.out.println(either.getLeft().getMessage());
        }


    }
}
