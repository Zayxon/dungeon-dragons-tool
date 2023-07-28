module cz.cvut.fel.pjv.dd_simple_tool {
    requires javafx.controls;
    requires javafx.fxml;

    opens cz.cvut.fel.pjv.dd_simple_tool to javafx.fxml;
    exports cz.cvut.fel.pjv.dd_simple_tool;
    requires org.json;
    requires junit;
    requires java.logging;
}
