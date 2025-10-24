package org.staticmap.map.resources.identity;

import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.Tool;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class McpIdentityResource {

    private final SecurityIdentity identity;

    public McpIdentityResource(SecurityIdentity identity) {
        this.identity = identity;
    }

    @Tool(name = "user-name-provider", description = "Provides a name of the current user in the staticmap realm")
    public TextContent provideUserName() {
        return new TextContent(identity.getPrincipal().getName());
    }
}
