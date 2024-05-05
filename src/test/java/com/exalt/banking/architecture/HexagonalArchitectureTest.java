package com.exalt.banking.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

class HexagonalArchitectureTest {

    private final JavaClasses importedClasses = new ClassFileImporter().importPackages("com.exalt.banking");

    @Test
    void domainLayer_should_not_depend_on_application_or_infrastructure() {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAnyPackage("..application..", "..infrastructure..")
                .check(importedClasses);
    }

    @Test
    void application_layer_should_only_depend_on_domain_and_not_on_infrastructure() {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat().resideInAnyPackage("..infrastructure..")
                .check(importedClasses);
    }

    @Test
    void infrastructure_layer_should_not_depend_on_application() {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..infrastructure..")
                .should().dependOnClassesThat().resideInAPackage("..application..")
                .check(importedClasses);
    }
}
