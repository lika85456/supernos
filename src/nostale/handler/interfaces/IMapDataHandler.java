package nostale.handler.interfaces;

import nostale.data.MapCharacterInstance;
import nostale.data.MapInstance;

public interface IMapDataHandler {
	public void onMapChange(MapInstance map);
	public void onPlayerIn(MapCharacterInstance player);
	public void mapout();
	public void dead();
}
