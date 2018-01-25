package nostale.domain;

public enum ItemType {
	Weapon(0), Armor(1), Fashion(2), Jewelery(3), Specialist(4), Box(5), Shell(6),
	Main(10), Upgrade(11), Production(12), Map(13), Special(14), Potion(15), Event(16), 
	Quest1(18), Sell(20), Food(21), Snack(22), Magical(24), Part(25), Teacher(26), 
	Ammo(27), Quest2(28), House(30), Garden(31), Minigame(32), Terrace(33), MinilandTheme(34);

	private byte value;

	private ItemType(int v) {
		this.value = (byte) v;
	}
	
    public static ItemType fromInteger(int x) {
	        switch(x) {
	        case 0:  return Weapon;
	        case 1:  return Armor;
	        case 2:  return Fashion;
	        case 3:  return Jewelery;
	        case 4:  return Specialist;
	        case 5:  return Box; 
	        case 6:  return Shell; 
	        case 10: return Main;
	        case 11: return Upgrade;
	        case 12: return Production;
	        case 13: return Map;
	        case 14: return Special;
	        case 15: return Potion;
	        case 16: return Event;
	        case 18: return Quest1;
	        case 20: return Sell;
	        case 21: return Food;
	        case 22: return Snack;
	        case 24: return Magical;
	        case 25: return Part;
	        case 26: return Teacher;
	        case 27: return Ammo;
	        case 28: return Quest2;
	        case 30: return House;
	        case 31: return Garden;
	        case 32: return Minigame;
	        case 33: return Terrace;
	        case 34: return MinilandTheme;
	        }
	        return null;
	    }

	public int getValue() {
		return value;
	}
}
