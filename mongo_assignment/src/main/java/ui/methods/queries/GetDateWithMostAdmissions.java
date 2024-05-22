package ui.methods.queries;

import jakarta.inject.Inject;
import services.QueryService;

public class GetDateWithMostAdmissions {

    private final QueryService queryService;

    @Inject
    public GetDateWithMostAdmissions(QueryService queryService) {
        this.queryService = queryService;
    }

    public void getDateWithMostAdmissions() {
        queryService.getDateWhenMostPatientsWereAdmitted()
                .peek(result -> System.out.println("The date where most patients were admitted is: " + result))
                .peekLeft(e -> System.out.println("ERROR: " + e.getMessage()));
    }
}
