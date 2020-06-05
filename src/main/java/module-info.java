module com.github.kaginawa.sdk {
    exports com.github.kaginawa.sdk;
    requires java.net.http;
    requires java.json.bind;
    opens com.github.kaginawa.sdk to org.eclipse.yasson;
}
