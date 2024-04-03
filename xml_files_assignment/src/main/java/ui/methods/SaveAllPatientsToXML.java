package ui.methods;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.error.AppError;
import services.PatientsService;

public class SaveAllPatientsToXML { //TODO: birthDate is not saving correctly

    private final PatientsService patientsService;

    @Inject
    public SaveAllPatientsToXML(PatientsService patientsService) {
        this.patientsService = patientsService;
    }

    public void savePatientsToXML() {

        Either<AppError, Integer> either = patientsService.writeAllPatientsInXML();

        if (either.isRight()) {
            System.out.println("Patients saved successfully into the XML file!");
        } else if (either.isLeft()) {
            System.out.println(either.getLeft().getMessage());
        }

    }

}
