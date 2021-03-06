package fr.badcookie20.tga.player;

public class PotentialBattle {

    private TGAPlayer enemy;

    private int lifePoints;
    private int maxTurns;
    private int damageMultiplier;
    private boolean statisticsIncrease;

    private boolean modified;

    public PotentialBattle(TGAPlayer enemy) {
        this.enemy = enemy;
        this.modified = false;

        this.lifePoints = TGAPlayer.DEFAULT_LIFE_POINTS;
        this.maxTurns = TGAPlayer.DEFAULT_MAX_TURNS;
        this.damageMultiplier = TGAPlayer.DEFAULT_DAMAGE_MULTIPLIER;
        this.statisticsIncrease = true;
    }

    public TGAPlayer getEnemy() {
        return this.enemy;
    }

    public boolean isModified() {
        return modified;
    }

    public Object get(PersonalizingAttribute o) {
        switch(o) {
            case LIFE_POINTS:
                return this.lifePoints;
            case STATISTICS_INCREASE:
                return this.statisticsIncrease;
            case DAMAGE_MULTIPLIER:
                return this.damageMultiplier;
            case MAX_TURNS:
                return this.maxTurns;
        }

        return null;
    }

    public void set(PersonalizingAttribute o, Object newValue) {
        switch(o) {
            case LIFE_POINTS:
                this.lifePoints = (Integer) newValue;
                break;
            case STATISTICS_INCREASE:
                this.statisticsIncrease = (Boolean) newValue;
                break;
            case DAMAGE_MULTIPLIER:
                this.damageMultiplier = (Integer) newValue;
                break;
            case MAX_TURNS:
                this.maxTurns = (Integer) newValue;
                break;
        }

        this.modified = true;
    }

}
