package org.staticmap.map;

import org.staticmap.map.model.MapCommand;

public interface MapCommandMapper<U> {

    MapCommand toMapCommand(U u);
}
