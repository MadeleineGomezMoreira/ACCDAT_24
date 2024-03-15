package ui;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.error.AppError;
import services.PatientsService;

import java.util.Scanner;

public class DeletePatient {

    private final PatientsService service;

    @Inject
    public DeletePatient(PatientsService service) {
        this.service = service;
    }

    public void deletePatient() {
        boolean confirmed = false;
        Either<AppError,Integer> either = service.delete(1, confirmed);

        Scanner sc = new Scanner(System.in);

        if (either.isRight()) {
            System.out.println("Patient deleted successfully!");
        } else if (either.isLeft()) {
            System.out.println(either.getLeft().getMessage());
            System.out.println("Please confirm the deletion of the patient (Y/N.");
            String input = sc.nextLine();

            if (input.equals("Y")) {
                confirmed = true;
                either = service.delete(1, confirmed);
                if (either.isRight()) {
                    System.out.println("Patient deleted successfully!");
                } else {
                    System.out.println(either.getLeft().getMessage());
                }
            } else {
                System.out.println("Goodbye!");
            }

        }
    }
}
