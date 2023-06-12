package com.groep5.Node.Agents;

import lombok.Data;

@Data
public class FailureAgentGetDTO {
    private int failingNodeHash;
    private boolean isNewAgent;

    public FailureAgentGetDTO(int failingNodeHash, boolean isNewAgent) {
        this.failingNodeHash = failingNodeHash;
        this.isNewAgent = isNewAgent;
    }
}
