package me.rndstad.drugsrpg.database.enums;

public enum DatabaseQuery {

    DRUGSRPG_DRUGS("CREATE TABLE IF NOT EXISTS `drugsrpg_drugs` " + "(" +
            "drug VARCHAR(255) NOT NULL," +
            "displayName VARCHAR(255) NOT NULL," +
            "message VARCHAR(255) NOT NULL," +
            "lore VARCHAR(255) NOT NULL," +
            "product VARCHAR(255) NOT NULL," +
            "rows VARCHAR(255) NOT NULL," +
            "ingredients VARCHAR(255) NOT NULL," +
            "effects VARCHAR(255) NOT NULL" +
            ")"),


    SELECT_DRUG("SELECT * FROM drugsrpg_drugs;"),

    INSERT_DRUG("INSERT INTO drugsrpg_drugs (drug, displayName, message, lore, product, rows, ingredients, effects) VALUES (?, ?, ?, ?, ?, ?, ?, ?);"),

    UPDATE_DRUG_DISPLAYNAME("UPDATE drugsrpg_drugs SET displayName=? WHERE drug=?"),

    UPDATE_DRUG_MESSAGE("UPDATE drugsrpg_drugs SET message=? WHERE drug=?"),

    UPDATE_DRUG_LORE("UPDATE drugsrpg_drugs SET lore=? WHERE drug=?"),

    UPDATE_DRUG_ROWS("UPDATE drugsrpg_drugs SET rows=? WHERE drug=?"),

    UPDATE_DRUG_INGREDIENTS("UPDATE drugsrpg_drugs SET ingredients=? WHERE drug=?"),

    UPDATE_DRUG_EFFECTS("UPDATE drugsrpg_drugs SET effects=? WHERE drug=?"),

    DELETE_DRUG("DELETE drugsrpg_drugs WHERE drug=?;");

    private String query;

    DatabaseQuery(String query) {
        this.query = query;
    }

    public String toString() {
        return query;
    }
}
