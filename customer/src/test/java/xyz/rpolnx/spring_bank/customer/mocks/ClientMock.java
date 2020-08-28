package xyz.rpolnx.spring_bank.customer.mocks;

import xyz.rpolnx.spring_bank.customer.model.enums.CustomPersonType;
import xyz.rpolnx.spring_bank.customer.model.entity.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientMock {
    public static List<Client> generateClients(int size, int max) {
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            clients.add(generateClient(i, max));
        }
        return clients;
    }

    private static Client generateClient(int position, int max) {
        int typePositionRandom = (int) (Math.random() * CustomPersonType.values().length);
        CustomPersonType type = CustomPersonType.values()[typePositionRandom];

        int randomScore = (int) (Math.random() * max);

        return new Client(String.valueOf(position), "Name #" + position, type, randomScore);
    }
}
