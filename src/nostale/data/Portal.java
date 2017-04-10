package nostale.data;
import nostale.util.Pos;

/**
 * Write a description of class Portal here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Portal
{
    //gp {portal.SourceX} {portal.SourceY} {ServerManager.GetMapInstance(portal.DestinationMapInstanceId)?.Map.MapId} {portal.Type} {i} {(portal.IsDisabled ? 1 : 0)}
    public Pos pos;
    public int MapID;
    public int Type;
    public Boolean isEnabled;
    public Portal()
    {
    }

}
