package nostale.data;

public class NosbasarItem extends Item{
	public long BasarItemId;
	public byte Amount;
	public long DateStart;
	public short Duration;
	public Boolean IsPackage;
	public Boolean MedalUsed;
	public long BasarPrice;
	public long SellerId;
	public String Owner;
	public String Info;
	public byte Rare;
	public NosbasarItem(Item i)
	{
		super(i);
	}
}
