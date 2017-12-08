package nostale.net;

import java.util.ArrayList;

public class Test {
	private ArrayList<Character> EncryptGamePacket(String buf, int session, Boolean is_session_packet)
	{
		int packet_length = buf.length();
		String packet_mask="";
		for (int i = 0; i < buf.length(); i++) {
			byte c = (byte) buf.charAt(i);
			if (c == '#')
				packet_mask += '0';
			else if (0 == (c -= 0x20) || (c += 0xF1) < 0 || (c -= 0xB) < 0 || 0 == (c -= 0xC5))
				packet_mask += '1';
			else
				packet_mask += '0';
		}

		ArrayList<Character> output = new ArrayList<Character>();
		int sequences = 0, sequence_counter = 0;
		int last_position = 0, current_position = 0, length = 0;
		char current_byte = 0;
		while(current_position <= packet_length)
		{
			last_position = current_position;
			while(current_position < packet_length && packet_mask.charAt(current_position)==('0'))
				++current_position;

			if(current_position!=0)
			{
				length = (current_position - last_position);
				sequences = (length / 0x7E);
				for(int i = 0; i < length; ++i, ++last_position)
				{
					if(i == (sequence_counter * 0x7E))
					{
						if(!(sequences!=0))
						{
							output.add(output.size(),(char)(length - i));
							
						} else
						{
							output.add(output.size(),(char)0x7E);
							--sequences;
							++sequence_counter;
						}
					}

					output.add(output.size(),(char)(buf.charAt(last_position) ^ 0xFF));
				}
			}

			if(current_position >= packet_length)
				break;

			last_position = current_position;
			while(current_position < packet_length && packet_mask.charAt(current_position) == '1')
				++current_position;

			if(current_position!=0)
			{
				length = (current_position - last_position);
				sequences = (length / 0x7E);
				for(int i = 0; i < length; ++i, ++last_position)
				{
					if(i == (sequence_counter * 0x7E))
					{
						if(!(sequences!=0))
						{
							output.add(output.size(),(char)((length - i) | 0x80));
						} else
						{
							output.add(output.size(),(char)(0x7E | 0x80));
							--sequences;
							++sequence_counter;
						}
					}

					current_byte = buf.charAt(last_position); 
					switch(current_byte)
					{
					case 0x20:
						current_byte = 1;
						break;
					case 0x2D:
						current_byte = 2;
						break;
					case 0x2E:
						current_byte = 3;
						break;
					case 0xFF:
						current_byte = 0xE;
						break;
					default:
						current_byte -= 0x2C;
						break;
					}

					if(current_byte != 0x00)
					{
						if(i % 2 == 0)
						{
							output.add(output.size(),(char)(current_byte << 4));
						} else 
						{
							char t = output.get(output.size()-1);
							t = (char)((int)t|current_byte); 
							output.set(output.size()-1, t);
						}
					}
				}
			}
		}

		output.add(output.size(),(char)0xFF);
		output = completeGamePacketEncrypt(output, session, is_session_packet);
		return output;
	}

	private ArrayList<Character> completeGamePacketEncrypt(ArrayList<Character> buf, int session, Boolean is_session_packet)
	{
		char session_number = (char)(((session >> 6) & 0xFF) & 0x80000003);
		
		if(session_number < 0)
			session_number = (char)(((session_number - 1) | 0xFFFFFFFC) + 1);

		char session_key = (char)(session & 0xFF);

		if(is_session_packet)
			session_number = (char)-1;

		switch (session_number)
		{
		case 0:
			for(int i = 0; i < buf.size(); ++i)
				buf.set(i, (char)(buf.get(i) + (session_key + 0x40)));
			break;

		case 1:
			for(int i = 0; i < buf.size(); ++i)
				buf.set(i, (char)((buf.get(i) - (session_key + 0x40))));
			break;

		case 2:
			for(int i = 0; i < buf.size(); ++i)
				buf.set(i, (char)((((buf.get(i) ^ 0xC3) + (session_key + 0x40)))));
			
			break;

		case 3:
			for(int i = 0; i < buf.size(); ++i)
				buf.set(i, (char)(((((buf.get(i)) ^ 0xC3) - (session_key + 0x40)))));
			break;

		default:
			for(int i = 0; i < buf.size(); ++i)
				buf.set(i, (char)(buf.get(i)+0x0F));
			break;
		}
		return buf;
	}
	
}
