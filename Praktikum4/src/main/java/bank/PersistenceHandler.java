package bank;

import bank.exceptions.TransactionAttributeException;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Klasse die die Custom-Funktionen zur (De)Serialisierung enthält
 */
public class PersistenceHandler implements JsonSerializer<Transaction> , JsonDeserializer<Transaction>{


    /**
     * Custom-Funktion für das Serialisieren von Transaktionen in eine JSON
     * @param transaction Die zu serialisierende Transaktion
     * @param type dunno
     * @param jsonSerializationContext dunno
     * @return Ein JsonElement, dass die Transaktion in Form einer JSON repräsentiert
     */
    @Override
    public JsonElement serialize(Transaction transaction, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject output = new JsonObject();
        JsonObject instance = new JsonObject();

        output.addProperty("CLASSNAME",transaction.getClass().getSimpleName());

        if(transaction instanceof Payment){
            instance.addProperty("incomingInterest", ((Payment) transaction).get_incomingInterest());
            instance.addProperty("outgoingInterest",((Payment) transaction).get_outgoingInterest());
        }
        else if(transaction instanceof Transfer){
            instance.addProperty("sender",((Transfer) transaction).get_sender());
            instance.addProperty("recipient",((Transfer) transaction).get_recipient());
        }

        instance.addProperty("date", transaction.get_date());
        instance.addProperty("amount", transaction.get_amount());
        instance.addProperty("description", transaction.get_description());

        output.add("INSTANCE",instance);
        return output;
    }

    /**
     * Custom-Funktion zum deserialisieren einer Transaktion aus einer JSON-File
     * @param jsonElement Das zu deserialisierende Element
     * @param type dunno
     * @param jsonDeserializationContext duno
     * @return Transaktion
     * @throws JsonParseException Es ist ein Fehler beim Parsen aufgetreten
     */
    @Override
    public Transaction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject input = jsonElement.getAsJsonObject();
        JsonObject val = input.get("INSTANCE").getAsJsonObject();

        String classname = input.get("CLASSNAME").getAsString();

        switch (classname) {
            case "Payment":
                try {
                    return new Payment(
                            val.get("date").getAsString(),
                            val.get("amount").getAsDouble(),
                            val.get("description").getAsString(),
                            val.get("incomingInterest").getAsDouble(),
                            val.get("outgoingInterest").getAsDouble()
                            );
                } catch (TransactionAttributeException e) {
                    e.printStackTrace();
                }

            case "IncomingTransfer":
                return new IncomingTransfer(
                        val.get("date").getAsString(),
                        val.get("amount").getAsDouble(),
                        val.get("description").getAsString(),
                        val.get("sender").getAsString(),
                        val.get("recipient").getAsString()
                        );

            case "OutgoingTransfer":
                return new OutgoingTransfer(
                        val.get("date").getAsString(),
                        val.get("amount").getAsDouble(),
                        val.get("description").getAsString(),
                        val.get("sender").getAsString(),
                        val.get("recipient").getAsString()
                );
        }

        return null;
    }
}
