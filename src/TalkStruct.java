import java.util.Random;

import nostale.packet.Packet;

public class TalkStruct {
	private static Random random = new Random();
	public static String[] text = {"Nov� nostale jackpot! Pro v�ce informac� mi napi� /info","P�ipojte se do jackpotu! pro v�ce informac� mi napi� /info"};
	public static Packet randomTalk()
	{
		return new Packet("say "+text[random.nextInt(text.length)]);
	}
}
