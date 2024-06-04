package ui.methods;

import jakarta.inject.Inject;
import services.DataMigrationService;

public class ImportDataToMongo {

    private final DataMigrationService dataMigrationService;

    @Inject
    public ImportDataToMongo(DataMigrationService dataMigrationService) {
        this.dataMigrationService = dataMigrationService;
    }

    public void importDataToMongo() {

        dataMigrationService.importDataToMongo()
                .peek(a -> System.out.println("Data imported successfully!"))
                .peekLeft(appError -> System.out.println("ERROR: " + appError.getMessage()));

    }

}
