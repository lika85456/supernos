import java.util.Random;

import nostale.packet.Packet;

public class TalkStruct {
	private static Random random = new Random();
	public static String[] text = {"Nový nostale jackpot! Pro více informací mi napiš /info","Pøipojte se do jackpotu! pro více informací mi napiš /info"};
	public static Packet randomTalk()
	{
		return new Packet("say "+text[random.nextInt(text.length)]);
	}
}
