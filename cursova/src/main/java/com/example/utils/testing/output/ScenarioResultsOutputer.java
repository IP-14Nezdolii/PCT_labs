package com.example.utils.testing.output;

import java.io.Serializable;

import com.example.utils.testing.TestScenario;

public interface ScenarioResultsOutputer {
    public void output(TestScenario<? extends Serializable> scenario);
}
