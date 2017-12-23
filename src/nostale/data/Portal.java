package nostale.data;

import nostale.util.Pos;

public class Portal {
    //gp {portal.SourceX} {portal.SourceY} {ServerManager.GetMapInstance(portal.DestinationMapInstanceId)?.Map.MapId} {portal.Type} {i} {(portal.IsDisabled ? 1 : 0)}
    public Pos pos;
    public int MapID;
    public int Type;
    public Boolean isEnabled;
    public Portal()
    {
}
}
