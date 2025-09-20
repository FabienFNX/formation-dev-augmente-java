package com.example.loansimulator.dto;

import com.example.loansimulator.model.LoanRequest;
import com.example.loansimulator.model.LoanResponse;

public class SaveSimulationRequest {
    private LoanRequest loanRequest;
    private LoanResponse loanResponse;

    public SaveSimulationRequest() {}

    public SaveSimulationRequest(LoanRequest loanRequest, LoanResponse loanResponse) {
        this.loanRequest = loanRequest;
        this.loanResponse = loanResponse;
    }

    public LoanRequest getLoanRequest() {
        return loanRequest;
    }

    public void setLoanRequest(LoanRequest loanRequest) {
        this.loanRequest = loanRequest;
    }

    public LoanResponse getLoanResponse() {
        return loanResponse;
    }

    public void setLoanResponse(LoanResponse loanResponse) {
        this.loanResponse = loanResponse;
    }
}