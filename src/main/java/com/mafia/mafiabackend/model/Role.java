package com.mafia.mafiabackend.model;

public enum Role {
    RED,
    BLACK,
    DON,
    SHERIFF,
    WHORE,
    DOCTOR,
    MANIAC;

    public static Boolean isBlack(Role role) {
        switch (role) {
            case BLACK:
            case DON:
            case WHORE:
                return true;
            default:
                return false;
        }
    }
}
