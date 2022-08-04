package com.saving.metadata.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.saving.metadata.exception.ParamException;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * @author Administrator
 */
public class BeanValidator {
    private static final ValidatorFactory VALIDATORFACTORY = Validation.buildDefaultValidatorFactory();

    private BeanValidator() {
    }

    public static <T> Map<String, String> validate(T t, Class... groups) {
        Validator validator = VALIDATORFACTORY.getValidator();
        Set validateResult = validator.validate(t, groups);
        if (validateResult.isEmpty()) {
            return Collections.emptyMap();
        } else {
            LinkedHashMap errors = Maps.newLinkedHashMap();
            Iterator iterator = validateResult.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation violation = (ConstraintViolation) iterator.next();
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return errors;
        }
    }

    public static Map<String, String> validateList(Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();
        Map errors;

        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object);
        } while (errors.isEmpty());

        return errors;
    }

    public static Map<String, String> validateObject(Object first, Object... objects) {
        if (objects != null && objects.length > 0) {
            return validateList(Lists.asList(first, objects));
        } else {
            return validate(first);
        }
    }

    public static void check(Object param) {
        Map<String, String> map = BeanValidator.validateObject(param);
        if (MapUtils.isNotEmpty(map)) {
            StringBuilder msg = new StringBuilder();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                msg.append(map.get(entry.getKey())).append("</br>");
            }
            throw new ParamException(msg.toString());
        }
    }

    public static <T> void getCheckListMsg(Map<String, String> stringStringMap2) {
        Map<String, String> stringStringMap = stringStringMap2;
        if (MapUtils.isNotEmpty(stringStringMap)) {
            StringBuilder msg = new StringBuilder();
            for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {
                msg.append(stringStringMap.get(entry.getKey())).append("</br>");
            }
            throw new ParamException(msg.toString());
        }
    }

}
