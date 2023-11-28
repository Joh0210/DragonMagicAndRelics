package de.joh.dmnr.exception;

public class CapabilityException extends RuntimeException {

    public static RuntimeException mna_magic(){
        return new RuntimeException("DM&R: Could not find M&A Magic Capability");
    }
}
