package xyz.rpolnx.spring_bank.account.model.enums;

import lombok.NoArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.EnumType;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public enum AccountStatus {
    ACTIVE,
    CREATING,
    INACTIVE;

    public static AccountStatus[] usableStatus() {
        return new AccountStatus[]{ACTIVE, CREATING};
    }

    @NoArgsConstructor
    public static class PostgresType extends EnumType<AccountStatus> {

        @Override
        public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
                throws HibernateException, SQLException {
            if (value == null) {
                st.setNull(index, Types.OTHER);
            } else {
                st.setObject(index, value.toString(), Types.OTHER);
            }
        }
    }
}
