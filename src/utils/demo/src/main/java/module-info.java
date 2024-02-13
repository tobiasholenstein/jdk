module com.sun.hotspot.igv.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.kordamp.ikonli.fontawesome;
    requires org.fxmisc.richtext;
    requires reactfx;
    requires com.github.javaparser.core;
    requires java.desktop;


    opens com.sun.hotspot.igv.demo to javafx.fxml;
    exports com.sun.hotspot.igv.demo;
}
