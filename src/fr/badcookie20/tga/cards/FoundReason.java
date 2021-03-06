package fr.badcookie20.tga.cards;

public enum FoundReason {

    CHEST(32, "Il y avait une carte dans ce coffre !"),
    MOB(20, "Il y avait une carte dans ce mob !"),
    PLANT(100, "Il y avait une carte dans ces herbes !"),
    XP_GAIN(10, "Il y avait de la mana dans cette orbe d'expérience !");

    private int odds;
    private String message;

    FoundReason(int odds, String message) {
        this.odds = odds;
        this.message = message;
    }

    public int getOdds() {
        return odds;
    }

    public String getMessage() {
        return this.message;
    }

}
