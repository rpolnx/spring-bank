package xyz.rpolnx.spring_bank.service.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;
import xyz.rpolnx.spring_bank.common.model.enums.EventType;
import xyz.rpolnx.spring_bank.service.service.OverdraftService;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.util.stream.Collectors.toMap;

@AllArgsConstructor
@Getter
public enum OverdraftEventHandler {
    CREATION(EventType.CREATION, OverdraftService::createOverdraft),
    UPDATE(EventType.UPDATE, OverdraftService::updateOverdraft),
    DELETE(EventType.DELETE, OverdraftService::deleteOverdraft);

    public final EventType type;
    public final BiConsumer<OverdraftService, AccountEvent> callable;

    private static final Map<EventType, OverdraftEventHandler> map;

    static {
        map = Arrays.stream(OverdraftEventHandler.values())
                .collect(toMap(OverdraftEventHandler::getType, x -> x));
    }

    public static OverdraftEventHandler fromEventType(EventType type) {
        return map.get(type);
    }
}
