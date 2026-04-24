package com.pedroheing.shoppingcart.common.serializer;

import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.exc.InvalidFormatException;

import java.math.BigDecimal;

public class BigDecimalStringDeserializer extends ValueDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctx) {
        if (p.currentToken() != JsonToken.VALUE_STRING) {
            throw new InvalidFormatException(
                    p,
                    "Invalid format. The value must be sent as a String.",
                    p.getString(),
                    BigDecimal.class
            );
        }

        String value = p.getString().trim();
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new InvalidFormatException(
                    p,
                    "The provided string does not represent a valid number.",
                    value,
                    BigDecimal.class
            );
        }
    }
}
