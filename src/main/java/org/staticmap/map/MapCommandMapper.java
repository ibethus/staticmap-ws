package org.staticmap.map;

import org.staticmap.model.MapCommand;

public interface MapCommandMapper<U> {

    MapCommand toMapCommand(U u);
}
