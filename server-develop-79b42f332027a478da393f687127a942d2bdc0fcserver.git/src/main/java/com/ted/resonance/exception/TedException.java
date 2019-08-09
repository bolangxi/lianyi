package com.ted.resonance.exception;

import com.ted.resonance.config.DServiceConstant;

public class TedException extends RuntimeException {
    private DServiceConstant dServiceConstant;

    public TedException(DServiceConstant dServiceConstant, Throwable cause) {
        super(cause);
        this.dServiceConstant = dServiceConstant;
    }

    public DServiceConstant getdServiceConstant() {
        return dServiceConstant;
    }
}
