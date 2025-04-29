package org.health.se7a.common;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.hibernate.type.descriptor.java.spi.JavaTypeBasicAdaptor;
import org.hibernate.type.descriptor.jdbc.NumericJdbcType;
import org.hibernate.type.internal.NamedBasicTypeImpl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class IDGenerator extends SequenceStyleGenerator {

    public static final String VALUE_PREFIX_PARAMETER = "valuePrefix";
    public static final String VALUE_PREFIX_DEFAULT = "";

    private String valuePrefix;

    public static final String NUMBER_FORMAT_PARAMETER = "numberFormat";
    public static final String INCREMENT_PARAM = "increment_size";
    public static final int DEFAULT_INCREMENT_SIZE = 1;
    public static final String NUMBER_FORMAT_DEFAULT = "";

    private String numberFormat;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM");
        String datePrefix = dateFormat.format(new Date());
        String sequenceNumber = String.format("%04d", super.generate(session, object));

         String seq = valuePrefix + datePrefix + sequenceNumber;
         return Long.parseLong(seq);
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        params.put(INCREMENT_PARAM, DEFAULT_INCREMENT_SIZE);
        super.configure(new NamedBasicTypeImpl<>(new JavaTypeBasicAdaptor<>(Long.class),
                NumericJdbcType.INSTANCE, "long"), params, serviceRegistry);
        valuePrefix = ConfigurationHelper.getString(VALUE_PREFIX_PARAMETER, params, VALUE_PREFIX_DEFAULT);
        numberFormat = ConfigurationHelper.getString(NUMBER_FORMAT_PARAMETER, params, NUMBER_FORMAT_DEFAULT);
    }
}
