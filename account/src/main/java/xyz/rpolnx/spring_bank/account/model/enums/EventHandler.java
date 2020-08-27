package xyz.rpolnx.spring_bank.account.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.rpolnx.spring_bank.account.model.dto.CustomerEvent;
import xyz.rpolnx.spring_bank.account.service.AccountService;
import xyz.rpolnx.spring_bank.common.model.enums.EventType;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.util.stream.Collectors.toMap;

@AllArgsConstructor
@Getter
public enum EventHandler {
    CREATION(EventType.CREATION, AccountService::createAccount),
    UPDATE(EventType.UPDATE, AccountService::updateAccount),
    DELETE(EventType.DELETE, AccountService::deleteAccount);

    public final EventType type;
    public final BiConsumer<AccountService, CustomerEvent> callable;

    private static final Map<EventType, EventHandler> map;

    static {
        map = Arrays.stream(EventHandler.values())
                .collect(toMap(EventHandler::getType, x -> x));
    }

    public static EventHandler fromEventType(EventType type) {
        return map.get(type);
    }
}
