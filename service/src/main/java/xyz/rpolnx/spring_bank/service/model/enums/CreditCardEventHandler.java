package xyz.rpolnx.spring_bank.service.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;
import xyz.rpolnx.spring_bank.common.model.enums.EventType;
import xyz.rpolnx.spring_bank.service.service.CreditCardService;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.util.stream.Collectors.toMap;

@AllArgsConstructor
@Getter
public enum CreditCardEventHandler {
    CREATION(EventType.CREATION, CreditCardService::createCreditCard),
    UPDATE(EventType.UPDATE, CreditCardService::updateCreditCard),
    DELETE(EventType.DELETE, CreditCardService::deleteCreditCard);

    public final EventType type;
    public final BiConsumer<CreditCardService, AccountEvent> callable;

    private static final Map<EventType, CreditCardEventHandler> map;

    static {
        map = Arrays.stream(CreditCardEventHandler.values())
                .collect(toMap(CreditCardEventHandler::getType, x -> x));
    }

    public static CreditCardEventHandler fromEventType(EventType type) {
        return map.get(type);
    }
}
