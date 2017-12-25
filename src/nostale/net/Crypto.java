package nostale.net;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import nostale.util.Log;

public class Crypto {


	public static String encrypt(String passwordToHash) {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] bytes = md.digest(passwordToHash.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	public static String md5(String input) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] md5sum = md.digest(input.getBytes());
		return String.format("%032X", new BigInteger(1, md5sum));

	}

	public static String DecryptLoginPacket(ArrayList<Integer> str) {
		String str_dec = "";
		for (int i = 0; i < str.size(); i++)
			str_dec += (char) (str.get(i) - 0xF);
		Crypto.addToLog("Login packet income", str_dec.substring(0, str_dec.length() - 1));
		return str_dec.substring(0, str_dec.length() - 1);
	}

	public static String EncryptLoginPacket(String str) {
		String str_enc = "";
		for (int i = 0; i < str.length(); i++)
			str_enc += (char) ((str.charAt(i) ^ 0xC3) + 0xF);
		Crypto.addToLog("Login packet sent", str);
		return str_enc += (char) 0xD8;
	}

	private static String ArrayListToString(ArrayList<Integer> output) {
		String returnValue = "";
		for (int i = 0; i < output.size(); i++) {
			returnValue += (char) (int) output.get(i);

		}
		return returnValue;
	}

	/*
	 * public static ArrayList<Integer> EncryptServerPacket(String str) {
	 * Crypto.addToLog("Game packet sent",str);
	 * 
	 * ArrayList<Integer> StrBytes = StringToInt(str); int BytesLength =
	 * StrBytes.size();
	 * 
	 * int[] encryptedData = new int[BytesLength + (int)Math.ceil(BytesLength /
	 * 0x7E) + 1];
	 * 
	 * int ii = 0; for (int i = 0; i < BytesLength; i++) { if (i % 0x7E == 0) {
	 * if(BytesLength-i>0x7E) encryptedData[i+ii]=0x7E; else
	 * encryptedData[i+ii]=(BytesLength-i) & 0xFF; ii++; } encryptedData[i + ii]
	 * = reverse_byte(StrBytes.get(i)); }
	 * 
	 * encryptedData[encryptedData.length - 1] = 0xFF;
	 * 
	 * ArrayList<Integer> list = new ArrayList<Integer>(encryptedData.length);
	 * for (int i : encryptedData) list.add(i); return list; }
	 * 
	 */

	public static int reverse_byte(int s) {
		return 0xFF - s;
	}

	public static ArrayList<String> DecryptGamePacket(ArrayList<Integer> buf) {
		int len = buf.size();
		ArrayList<String> output = new ArrayList<String>();
		ArrayList<Integer> current_packet = new ArrayList<Integer>();
		char keys[] = { ' ', '-', '.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'n' };
		int index = 0;
		int currentByte = 0, length = 0, first = 0, second = 0;

		while (index < len) {
			currentByte = buf.get(index);
			++index;
			if (currentByte == 0xFF) {

				try {// ISO-8859-1
					String toAdd = new String(ArrayListToString(current_packet).getBytes("ISO-8859-1"));
					output.add(toAdd);
					if (!toAdd.contains("mv") && !toAdd.contains("st"))
						Crypto.addToLog("Game packet income", toAdd);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// System.out.println("RECEIVED:
				// "+ArrayListToString(current_packet));
				current_packet = new ArrayList<Integer>();
				continue;
			}

			length = (currentByte & 0x7F);
			if (((currentByte & 0x80) & 0xFF) != 0 && currentByte != 0) {
				while (length != 0) {
					if (index < len) {
						currentByte = buf.get(index);
						++index;
						try {
							first = keys[(((currentByte & 0xF0)) >> 4) - 1];
							if (first != 0x6E)
								current_packet.add(first);

							if (length <= 1)
								break;

							second = keys[(currentByte & 0xF) - 1];
							if (second != 0x6E)
								current_packet.add(second);

							length -= 2;
						} catch (Exception e) {
							System.out.println("Exception packet: " + ArrayListToString(current_packet));
						}
					} else {
						--length;
					}
				}
			} else {
				while (length != 0) {
					if (index < len) {
						// current_packet.add(buf.get(index) ^ 0xFF);¨
						current_packet.add(buf.get(index) ^ 0xFF);
						++index;
					}

					--length;
				}
			}
		}

		return output;
	}

	public static String EncryptGamePacket(String buff, int session, boolean is_session_packet) {
		// System.out.println("SEND: "+buf);
		Crypto.addToLog("Game packet sent", buff);
		// try {
		// buf = new String(buf.getBytes("UTF-8"));
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		byte[] buf = new byte[1];
		try {
			buf = null;
			buf = buff.getBytes("windows-1250");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int packet_length = buf.length;
		String packet_mask = "";
		for (int i = 0; i < buf.length; i++) {
			char c = (char) buf[i];
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
		while (current_position <= packet_length) {
			last_position = current_position;
			while (current_position < packet_length && packet_mask.charAt(current_position) == ('0'))
				++current_position;

			if (current_position != 0) {
				length = (current_position - last_position);
				sequences = (length / 0x7E);
				for (int i = 0; i < length; ++i, ++last_position) {
					if (i == (sequence_counter * 0x7E)) {
						if (!(sequences != 0)) {
							output.add(output.size(), (char) (length - i));

						} else {
							output.add(output.size(), (char) 0x7E);
							--sequences;
							++sequence_counter;
						}
					}

					output.add(output.size(), (char) (buf[last_position] ^ 0xFF));
				}
			}

			if (current_position >= packet_length)
				break;

			last_position = current_position;
			while (current_position < packet_length && packet_mask.charAt(current_position) == '1')
				++current_position;

			if (current_position != 0) {
				length = (current_position - last_position);
				sequences = (length / 0x7E);
				for (int i = 0; i < length; ++i, ++last_position) {
					if (i == (sequence_counter * 0x7E)) {
						if (!(sequences != 0)) {
							output.add(output.size(), (char) ((length - i) | 0x80));
						} else {
							output.add(output.size(), (char) (0x7E | 0x80));
							--sequences;
							++sequence_counter;
						}
					}

					current_byte = (char) buf[last_position];
					switch (current_byte) {
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

					if (current_byte != 0x00) {
						if (i % 2 == 0) {
							output.add(output.size(), (char) (current_byte << 4));
						} else {
							char t = output.get(output.size() - 1);
							t = (char) (t | current_byte);
							output.set(output.size() - 1, t);
						}
					}
				}
			}
		}

		output.add(output.size(), (char) 0xFF);
		output = completeGamePacketEncrypt(output, session, is_session_packet);

		// ArrayList to String
		String returnValue = "";
		for (int i = 0; i < output.size(); i++) {
			// System.out.println((int)output.get(i));
			returnValue += output.get(i);

		}

		return returnValue;
	}

	private static ArrayList<Character> completeGamePacketEncrypt(ArrayList<Character> buf, int session,
			Boolean is_session_packet) {
		char session_number = (char) (((session >> 6) & 0xFF) & 0x80000003);

		if (session_number < 0)
			session_number = (char) (((session_number - 1) | 0xFFFFFFFC) + 1);

		char session_key = (char) (session & 0xFF);

		if (is_session_packet)
			session_number = (char) -1;

		switch (session_number) {
		case 0:
			for (int i = 0; i < buf.size(); ++i)
				buf.set(i, (char) (buf.get(i) + (session_key + 0x40)));
			break;

		case 1:
			for (int i = 0; i < buf.size(); ++i)
				buf.set(i, (char) ((buf.get(i) - (session_key + 0x40))));
			break;

		case 2:
			for (int i = 0; i < buf.size(); ++i)
				buf.set(i, (char) ((((buf.get(i) ^ 0xC3) + (session_key + 0x40)))));

			break;

		case 3:
			for (int i = 0; i < buf.size(); ++i)
				buf.set(i, (char) (((((buf.get(i)) ^ 0xC3) - (session_key + 0x40)))));
			break;

		default:
			for (int i = 0; i < buf.size(); ++i)
				buf.set(i, (char) (buf.get(i) + 0x0F));
			break;
		}
		return buf;
	}

	public static void addToLog(String tag, String message) {
		String timeStamp = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(Calendar.getInstance().getTime());
		String ms = timeStamp + " " + tag.toUpperCase() + " " + message;
		ms.replaceAll("\n+", "\n");
		Log.log(tag, ms);
		//System.out.print(ms);
	}

}