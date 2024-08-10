package com.lema.test.components;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;


class ComponentsArchTest {
    @Test
    void assertInternalPackagesAreNotAccessFromOutside() {
        var internalPackage = "..internal..";

        JavaClasses jc = new ClassFileImporter()
                .importPackages("com.lema.test.components");

        ArchRuleDefinition.noClasses()
                .that().resideOutsideOfPackage(internalPackage)
                .should().dependOnClassesThat().resideInAPackage(internalPackage)
                .check(jc);

    }
}
