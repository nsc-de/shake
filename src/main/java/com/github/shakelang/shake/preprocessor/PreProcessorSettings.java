package com.github.shakelang.shake.preprocessor;

public class PreProcessorSettings {


    private boolean shortenCalculations = true;

    public boolean shortenCalculations() {
        return shortenCalculations;
    }

    public PreProcessorSettings setShortenCalculations(boolean shortenCalculations) {
        this.shortenCalculations = shortenCalculations;
        return this;
    }
}
