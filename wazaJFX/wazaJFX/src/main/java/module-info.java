module com.thales.wazajfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;


    requires lombok;

    requires com.gluonhq.connect;
    requires com.fasterxml.jackson.databind;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;

    requires com.fasterxml.jackson.datatype.jsr310;

    opens com.thales.wazajfx to javafx.fxml;
    exports com.thales.wazajfx;


    opens com.thales.wazajfx.controller to javafx.fxml;
    exports com.thales.wazajfx.controller;

    opens com.thales.wazajfx.model to javafx.fxml;
    exports com.thales.wazajfx.model;
    exports com.thales.wazajfx.utils;
    opens com.thales.wazajfx.utils to javafx.fxml;

}