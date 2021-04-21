module io.github.kaginawa.sdk {
    exports io.github.kaginawa.sdk;
    requires java.net.http;
    requires java.json.bind;
    opens io.github.kaginawa.sdk to org.eclipse.yasson;
}
