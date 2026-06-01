package com.ceniuch.common.config;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class ValidatorLoggingRuntimeHints implements RuntimeHintsRegistrar {

    private static final String[] GENERATED_TYPES = {
            "org.hibernate.validator.internal.util.logging.Log_$logger",
            "org.hibernate.validator.internal.util.logging.Messages_$bundle"
    };

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        for (String type : GENERATED_TYPES) {
            hints.reflection().registerTypeIfPresent(classLoader, type,
                    MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                    MemberCategory.INVOKE_DECLARED_METHODS);
        }
    }
}