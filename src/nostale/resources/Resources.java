package nostale.resources;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import nostale.data.Item;
import nostale.data.NpcMonster;
import nostale.data.Skill;
import nostale.domain.EquipmentType;
import nostale.domain.InventoryType;
import nostale.domain.ItemType;
import nostale.domain.MonsterType;

public class Resources {

	// public Pos pos; //Position
	private static HashMap<Integer, Skill> skills;
	private static HashMap<Integer, Item> items;
	private static HashMap<Integer, NpcMonster> mobs;
	private static Boolean loaded = false;
	
	/*public static Item[] getDrop(NpcMonster monster)
	{
		Item[] toRet = new Item[monster.Drop.length];
		for(int i = 0;i<monster.Drop.length;i++)
		{
			toRet[i] = Resources.getItem(monster.Drop[i]);
		}
		return toRet;
	}
	
	*/

	public static Skill getSkill(Integer id) {
		return skills.get(id);
	}

	public static Item getItem(Integer id) {
		return items.get(id);
	}

	public static NpcMonster getMob(Integer id) {
		return mobs.get(id);
	}

	public static void load() {
		if (loaded == false) {
			try {
				skills = loadSkills();
				items = loadItems();
				mobs = loadMonsters();
				loaded = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Resources loaded");
	}

	// TODO rewrite reading from files to FileLoader
	public static HashMap<Integer, Skill> loadSkills() {
		HashMap<Integer, Skill> skills = new HashMap<Integer, Skill>();
		String line;
		int counter = 0;
		try (InputStream fis = new FileInputStream("resources/Skill.dat");
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);) {

			Skill skill = null;
			while ((line = br.readLine()) != null) {
				String[] currentLine = line.split("\\s+");
				if (currentLine.length > 2 && currentLine[1].equals("VNUM")) {
					skill = new Skill();
					skill.SkillVNum = Short.parseShort(currentLine[2]);
				} else if (currentLine.length > 2 && currentLine[1].equals("NAME")) {
					skill.NameID = currentLine[2];
				} else if (currentLine.length > 2 && currentLine[1].equals("TYPE")) {
					skill.SkillType = Short.parseShort(currentLine[2]);
					skill.CastId = Short.parseShort(currentLine[3]);
					skill.Class = Short.parseShort(currentLine[4]);
					skill.Type = Short.parseShort(currentLine[5]);
					skill.Element = Short.parseShort(currentLine[7]);
				} else if (currentLine.length > 3 && currentLine[1].equals("COST")) {
					skill.CPCost = currentLine[2].equals("-1") ? (byte) 0 : Short.parseShort(currentLine[2]);
					skill.Price = Integer.parseInt(currentLine[3]);
				} else if (currentLine.length > 2 && currentLine[1].equals("LEVEL")) {
					skill.LevelMinimum = currentLine[2] != "-1" ? Short.parseShort(currentLine[2]) : (byte) 0;
					skill.MinimumAdventurerLevel = currentLine[3] != "-1" ? Short.parseShort(currentLine[3]) : (byte) 0;
					skill.MinimumSwordmanLevel = currentLine[4] != "-1" ? Short.parseShort(currentLine[4]) : (byte) 0;
					skill.MinimumArcherLevel = currentLine[5] != "-1" ? Short.parseShort(currentLine[5]) : (byte) 0;
					skill.MinimumMagicianLevel = currentLine[6] != "-1" ? Short.parseShort(currentLine[6]) : (byte) 0;
				} else if (currentLine.length > 2 && currentLine[1].equals("EFFECT")) {
					skill.CastEffect = Short.parseShort(currentLine[3]);
					skill.CastAnimation = Short.parseShort(currentLine[4]);
					skill.Effect = Short.parseShort(currentLine[5]);
					skill.AttackAnimation = Short.parseShort(currentLine[6]);
				} else if (currentLine.length > 2 && currentLine[1].equals("TARGET")) {
					skill.TargetType = Short.parseShort(currentLine[2]);
					skill.HitType = Short.parseShort(currentLine[3]);
					skill.Range = Short.parseShort(currentLine[4]);
					skill.TargetRange = Short.parseShort(currentLine[5]);
				} else if (currentLine.length > 2 && currentLine[1].equals("DATA")) {
					skill.UpgradeSkill = Short.parseShort(currentLine[2]);
					skill.UpgradeType = Short.parseShort(currentLine[3]);
					skill.CastTime = Short.parseShort(currentLine[6]);
					skill.Cooldown = Short.parseShort(currentLine[7]);
					skill.MpCost = Short.parseShort(currentLine[10]);
					skill.ItemVNum = Short.parseShort(currentLine[12]);
				} else if (currentLine.length > 2 && currentLine[1].equals("FCOMBO")) {
					// investigate
					/*
					 * if (currentLine[2].equals("1") { combo.FirstActivationHit
					 * = Short.parseShort(currentLine[3]);
					 * combo.FirstComboAttackAnimation =
					 * Short.parseShort(currentLine[4]); combo.FirstComboEffect
					 * = Short.parseShort(currentLine[5]);
					 * combo.SecondActivationHit =
					 * Short.parseShort(currentLine[3]);
					 * combo.SecondComboAttackAnimation =
					 * Short.parseShort(currentLine[4]); combo.SecondComboEffect
					 * = Short.parseShort(currentLine[5]);
					 * combo.ThirdActivationHit =
					 * Short.parseShort(currentLine[3]);
					 * combo.ThirdComboAttackAnimation =
					 * Short.parseShort(currentLine[4]); combo.ThirdComboEffect
					 * = Short.parseShort(currentLine[5]);
					 * combo.FourthActivationHit =
					 * Short.parseShort(currentLine[3]);
					 * combo.FourthComboAttackAnimation =
					 * Short.parseShort(currentLine[4]); combo.FourthComboEffect
					 * = Short.parseShort(currentLine[5]);
					 * combo.FifthActivationHit =
					 * Short.parseShort(currentLine[3]);
					 * combo.FifthComboAttackAnimation =
					 * Short.parseShort(currentLine[4]); combo.FifthComboEffect
					 * = Short.parseShort(currentLine[5]); }
					 */
				} else if (currentLine.length > 2 && currentLine[1].equals("CELL")) {
					// investigate
				} else if (currentLine.length > 1 && currentLine[1].equals("Z_DESC")) {
					// investigate
					skills.put((int) skill.SkillVNum, skill);
					counter++;
				}
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		line = "";
		try (InputStream fis2 = new FileInputStream("resources/names/cz/skill.txt");
				InputStreamReader isr2 = new InputStreamReader(fis2);
				BufferedReader br2 = new BufferedReader(isr2);) {
			while ((line = br2.readLine()) != null) {
				String[] splited = line.split("\\s+");
				String id = splited[0];

				skills.forEach((k, v) -> {
					String tempName = "";
					// v.Name = "";
					if (v.NameID.equals(id)) {
						for (int ii = 1; ii < splited.length; ii++) {
							tempName += splited[ii] + " ";
						}
						tempName = tempName.substring(0, tempName.length() - 1);
						v.Name = tempName;
						// System.out.println(k+"->"+v.Name);
						// items.remove(k)
						// items.replace(k, v);
					}

				});

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return skills;
	}

	public static HashMap<Integer, NpcMonster> loadMonsters() {
		HashMap<Integer, NpcMonster> items = new HashMap<Integer, NpcMonster>();
		String line;
		try (InputStream fis = new FileInputStream("resources/Monster.dat");
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);) {

			int[] basicHp = new int[100];
			int[] basicMp = new int[100];
			int[] basicXp = new int[100];
			int[] basicJXp = new int[100];

			// basicHpLoad
			int baseHp = 138;
			int basup = 17;
			for (int i = 0; i < 100; i++) {
				basicHp[i] = baseHp;
				basup++;
				baseHp += basup;

				if (i == 37) {
					baseHp = 1765;
					basup = 65;
				}
				if (i >= 41) {
					if ((99 - i) % 8 == 0) {
						basup++;
					}
				}
			}

			// basicMpLoad
			for (int i = 0; i < 100; i++) {
				basicMp[i] = basicHp[i];
			}

			// basicXPLoad
			for (int i = 0; i < 100; i++) {
				basicXp[i] = i * 180;
			}

			// basicJXpLoad
			for (int i = 0; i < 100; i++) {
				basicJXp[i] = 360;
			}

			long unknownData = 0;
			NpcMonster npc = null;
			while ((line = br.readLine()) != null) {
				String[] currentLine = line.split("\\s+");

				if (currentLine.length > 1) {
					if (currentLine.length > 2 && currentLine[1].equals("VNUM")) {
						if (npc != null)
							items.put(Integer.valueOf(npc.NpcMonsterVNum), npc);
						npc = new NpcMonster();

						npc.NpcMonsterVNum = Short.parseShort(currentLine[2]);
					} else if (currentLine.length > 2 && currentLine[1].equals("NAME")) {
						npc.NameID = currentLine[2];
					} else if (currentLine.length > 2 && currentLine[1].equals("LEVEL")) {
						npc.Level = Short.parseShort(currentLine[2]);
					} else if (currentLine.length > 3 && currentLine[1].equals("RACE")) {
						npc.Race = Short.parseShort(currentLine[2]);
						npc.RaceType = Short.parseShort(currentLine[3]);
					} else if (currentLine.length > 7 && currentLine[1].equals("ATTRIB")) {
						npc.Element = Short.parseShort(currentLine[2]);
						npc.ElementRate = Short.parseShort(currentLine[3]);
						npc.FireResistance = Short.parseShort(currentLine[4]);
						npc.WaterResistance = Short.parseShort(currentLine[5]);
						npc.LightResistance = Short.parseShort(currentLine[6]);
						npc.DarkResistance = Short.parseShort(currentLine[7]);
					} else if (currentLine.length > 3 && currentLine[1].equals("HP/MP")) {
						npc.MaxHP = Integer.parseInt(currentLine[2]) + basicHp[npc.Level];
						npc.MaxMP = Integer.parseInt(currentLine[3]) + basicMp[npc.Level];
					} else if (currentLine.length > 2 && currentLine[1].equals("EXP")) {
						npc.XP = Math.abs(Integer.parseInt(currentLine[2]) + basicXp[npc.Level]);
						npc.JobXP = Integer.parseInt(currentLine[3]) + basicJXp[npc.Level];
					} else if (currentLine.length > 6 && currentLine[1].equals("PREATT")) {
						npc.IsHostile = currentLine[2] != "0";
						npc.NoticeRange = Short.parseShort(currentLine[4]);
						npc.Speed = Short.parseShort(currentLine[5]);
						npc.RespawnTime = Integer.parseInt(currentLine[6]);
					} else if (currentLine.length > 6 && currentLine[1].equals("WEAPON")) {
						if (currentLine[3].equals("1")) {
							npc.DamageMinimum = (short) ((Short.parseShort(currentLine[2]) - 1) * 4 + 32
									+ Short.parseShort(currentLine[4]) + Math.round(((npc.Level - 1) / 5)));
							npc.DamageMaximum = (short) ((Short.parseShort(currentLine[2]) - 1) * 6 + 40
									+ Short.parseShort(currentLine[5]) - Math.round(((npc.Level - 1) / 5)));
							npc.Concentrate = (short) ((Short.parseShort(currentLine[2]) - 1) * 5 + 27
									+ Short.parseShort(currentLine[6]));
							npc.CriticalChance = (short) (4 + Short.parseShort(currentLine[7]));
							npc.CriticalRate = (short) (70 + Short.parseShort(currentLine[8]));
						} else if (currentLine[3].equals("2")) {
							npc.DamageMinimum = (short) (Short.parseShort(currentLine[2]) * 6.5f + 23
									+ Short.parseShort(currentLine[4]));
							npc.DamageMaximum = (short) ((Short.parseShort(currentLine[2]) - 1) * 8 + 38
									+ Short.parseShort(currentLine[5]));
							npc.Concentrate = (short) (70 + Short.parseShort(currentLine[6]));
						}
					} else if (currentLine.length > 6 && currentLine[1].equals("ARMOR")) {
						npc.CloseDefence = (short) ((Short.parseShort(currentLine[2]) - 1) * 2 + 18);
						npc.DistanceDefence = (short) ((Short.parseShort(currentLine[2]) - 1) * 3 + 17);
						npc.MagicDefence = (short) ((Short.parseShort(currentLine[2]) - 1) * 2 + 13);
						npc.DefenceDodge = (short) ((Short.parseShort(currentLine[2]) - 1) * 5 + 31);
						npc.DistanceDefenceDodge = (short) ((Short.parseShort(currentLine[2]) - 1) * 5 + 31);
					} else if (currentLine.length > 7 && currentLine[1].equals("ETC")) {
						unknownData = Integer.parseInt(currentLine[2]);
						if (unknownData == -2147481593) {
							npc.MonsterType = MonsterType.Special;
						}
						if (unknownData == -2147483616 || unknownData == -2147483647 || unknownData == -2147483646) {
							if (npc.Race == 8 && npc.RaceType == 0) {
								npc.NoAggresiveIcon = true;
							} else {
								npc.NoAggresiveIcon = false;
							}
						}
						if (npc.NpcMonsterVNum >= 588 && npc.NpcMonsterVNum <= 607) {
							npc.MonsterType = MonsterType.Elite;
						}
					} else if (currentLine.length > 6 && currentLine[1].equals("SETTING")) {
						if (currentLine[4] != "0") {
							npc.VNumRequired = Short.parseShort(currentLine[4]);
							npc.AmountRequired = 1;
						}
					} else if (currentLine.length > 4 && currentLine[1].equals("PETINFO")) {
						if (npc.VNumRequired == 0 && (unknownData == -2147481593 || unknownData == -2147481599
								|| unknownData == -1610610681)) {
							npc.VNumRequired = Short.parseShort(currentLine[2]);
							npc.AmountRequired = Short.parseShort(currentLine[3]);
						}
					} else if (currentLine.length > 2 && currentLine[1].equals("EFF")) {
						npc.BasicSkill = Short.parseShort(currentLine[2]);
					} else if (currentLine.length > 8 && currentLine[1].equals("ZSKILL")) {
						npc.AttackClass = Short.parseShort(currentLine[2]);
						npc.BasicRange = Short.parseShort(currentLine[3]);
						npc.BasicArea = Short.parseShort(currentLine[5]);
						npc.BasicCooldown = Short.parseShort(currentLine[6]);
					} else if (currentLine.length > 4 && currentLine[1].equals("WINFO")) {
						npc.AttackUpgrade = Short.parseShort(unknownData == 1 ? currentLine[2] : currentLine[4]);
					} else if (currentLine.length > 3 && currentLine[1].equals("AINFO")) {
						npc.DefenceUpgrade = Short.parseShort(unknownData == 1 ? currentLine[2] : currentLine[3]);
					} /*else if (currentLine[1].equals("ITEM")) {

						int pos = line.indexOf("1	-1	0	0	-1");
						if (pos > 0) {

							String tempLine[] = line.substring(0, pos).split("\\s+");
							if (tempLine.length > 2) {

								npc.Drop = new int[tempLine.length - 2];
								for (int iii = 2; iii < tempLine.length; iii++) {
									npc.Drop[iii - 2] = Integer.parseInt(currentLine[iii]);
								}
							}
						}
					}*/

				}

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String line1 = "";
		try (InputStream fis = new FileInputStream("resources/names/cz/monster.txt");
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);) {
			while ((line1 = br.readLine()) != null) {
				String[] currentLine = line1.split("\\s+");
				int id = Integer.parseInt(currentLine[0].substring(3, currentLine[0].length() - 1));

				items.forEach((k, v) -> {
					String tempName = "";
					// v.Name = "";
					if (v.NameID.equals("zts" + id + "e")) {
						for (int ii = 1; ii < currentLine.length; ii++) {
							tempName += currentLine[ii] + " ";
						}
						tempName = tempName.substring(0, tempName.length() - 1);
						v.Name = tempName;
						// System.out.println(k+"->"+v.Name);
						// items.remove(k)
						// items.replace(k, v);
					}

				});

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return items;
	}

	public static HashMap<Integer, Item> loadItems() {
		String[] file = FileLoader.loadFile("resources/Item.dat");
		Boolean itemAreaBegin = false;
		Item item = new Item();
		HashMap<Integer, Item> items = new HashMap<Integer, Item>();
		for (int i = 0; i < file.length; i++) {
			String line = file[i];

			String[] currentLine = line.split("\\s+");

			if (currentLine.length > 3 && currentLine[1].equals("VNUM")) {
				itemAreaBegin = true;
				item.VNum = Short.parseShort(currentLine[2]);
				item.Price = Long.parseLong(currentLine[3]);
			} else if (currentLine.length > 1 && currentLine[1].equals("END")) {
				if (!itemAreaBegin) {
					continue;
				}
				items.put((int) item.VNum, item);
				item = new Item();
				itemAreaBegin = false;
			} else if (currentLine.length > 2 && currentLine[1].equals("NAME")) {
				item.NameID = currentLine[2];
			} else if (currentLine.length > 7 && currentLine[1].equals("INDEX")) {
				switch (Short.parseShort(currentLine[2])) {
				case 4:
					item.Type = InventoryType.Equipment;
					break;

				case 8:
					item.Type = InventoryType.Equipment;
					break;

				case 9:
					item.Type = InventoryType.Main;
					break;

				case 10:
					item.Type = InventoryType.Etc;
					break;

				default:
					// item.Type =
					// (InventoryType)Enum.Parse(typeof(InventoryType),
					// currentLine[2]);
					item.Type = InventoryType.values()[Short.parseShort(currentLine[2])];
					break;
				}
				item.ItemType = !currentLine[3].equals("-1") ? ItemType.values()[Integer.parseInt(currentLine[3])]
						: ItemType.Weapon;
				item.ItemSubType = Byte.parseByte(currentLine[4]);
				item.EquipmentSlot = !currentLine[5].equals("-1")
						? EquipmentType.values()[Short.parseShort(currentLine[5])] : EquipmentType.values()[0];

				// item.DesignId = Integer.parseInt(currentLine[6]);
				switch (item.VNum) {
				case 1906:
					item.Morph = 2368;
					item.Speed = 20;
					item.WaitDelay = 3000;
					break;

				case 1907:
					item.Morph = 2370;
					item.Speed = 20;
					item.WaitDelay = 3000;
					break;

				case 1965:
					item.Morph = 2406;
					item.Speed = 20;
					item.WaitDelay = 3000;
					break;

				case 5008:
					item.Morph = 2411;
					item.Speed = 20;
					item.WaitDelay = 3000;
					break;

				case 5117:
					item.Morph = 2429;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 5152:
					item.Morph = 2432;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 5173:
					item.Morph = 2511;
					item.Speed = 16;
					item.WaitDelay = 3000;
					break;

				case 5196:
					item.Morph = 2517;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 5226: // Invisible locomotion, only 5 seconds with booster
					item.Morph = 1817;
					item.Speed = 20;
					item.WaitDelay = 3000;
					break;

				case 5228: // Invisible locoomotion, only 5 seconds with booster
					item.Morph = 1819;
					item.Speed = 20;
					item.WaitDelay = 3000;
					break;

				case 5232:
					item.Morph = 2520;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 5234:
					item.Morph = 2522;
					item.Speed = 20;
					item.WaitDelay = 3000;
					break;

				case 5236:
					item.Morph = 2524;
					item.Speed = 20;
					item.WaitDelay = 3000;
					break;

				case 5238:
					item.Morph = 1817;
					item.Speed = 20;
					item.WaitDelay = 3000;
					break;

				case 5240:
					item.Morph = 1819;
					item.Speed = 20;
					item.WaitDelay = 3000;
					break;

				case 5319:
					item.Morph = 2526;
					item.Speed = 22;
					item.WaitDelay = 3000;
					break;

				case 5321:
					item.Morph = 2528;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 5323:
					item.Morph = 2530;
					item.Speed = 22;
					item.WaitDelay = 3000;
					break;

				case 5330:
					item.Morph = 2928;
					item.Speed = 22;
					item.WaitDelay = 3000;
					break;

				case 5332:
					item.Morph = 2930;
					item.Speed = 14;
					item.WaitDelay = 3000;
					break;

				case 5360:
					item.Morph = 2932;
					item.Speed = 22;
					item.WaitDelay = 3000;
					break;

				case 5386:
					item.Morph = 2934;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 5387:
					item.Morph = 2936;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 5388:
					item.Morph = 2938;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 5389:
					item.Morph = 2940;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 5390:
					item.Morph = 2942;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 5391:
					item.Morph = 2944;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 5914:
					item.Morph = 2513;
					item.Speed = 14;
					item.WaitDelay = 3000;
					break;

				case 5997:
					item.Morph = 3679;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 9054:
					item.Morph = 2368;
					item.Speed = 20;
					item.WaitDelay = 3000;
					break;

				case 9055:
					item.Morph = 2370;
					item.Speed = 20;
					item.WaitDelay = 3000;
					break;

				case 9058:
					item.Morph = 2406;
					item.Speed = 20;
					item.WaitDelay = 3000;
					break;

				case 9065:
					item.Morph = 2411;
					item.Speed = 20;
					item.WaitDelay = 3000;
					break;

				case 9070:
					item.Morph = 2429;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 9073:
					item.Morph = 2432;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 9078:
					item.Morph = 2520;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 9079:
					item.Morph = 2522;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 9080:
					item.Morph = 2524;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 9081:
					item.Morph = 1817;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 9082:
					item.Morph = 1819;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 9083:
					item.Morph = 2526;
					item.Speed = 22;
					item.WaitDelay = 3000;
					break;

				case 9084:
					item.Morph = 2528;
					item.Speed = 22;
					item.WaitDelay = 3000;
					break;

				case 9085:
					item.Morph = 2930;
					item.Speed = 22;
					item.WaitDelay = 3000;
					break;

				case 9086:
					item.Morph = 2928;
					item.Speed = 22;
					item.WaitDelay = 3000;
					break;

				case 9087:
					item.Morph = 2930;
					item.Speed = 14;
					item.WaitDelay = 3000;
					break;

				case 9088:
					item.Morph = 2932;
					item.Speed = 22;
					item.WaitDelay = 3000;
					break;

				case 9090:
					item.Morph = 2934;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 9091:
					item.Morph = 2936;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 9092:
					item.Morph = 2938;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 9093:
					item.Morph = 2940;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 9094:
					item.Morph = 2942;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				case 9115:
					item.Morph = 3679;
					item.Speed = 21;
					item.WaitDelay = 3000;
					break;

				default:
					if (item.EquipmentSlot == (EquipmentType.Amulet)) {
						switch (item.VNum) {
						case 4503:
							item.EffectValue = 4544;
							break;

						case 4504:
							item.EffectValue = 4294;
							break;

						case 282: // Red amulet
							item.Effect = 791;
							item.EffectValue = 3;
							break;

						case 283: // Blue amulet
							item.Effect = 792;
							item.EffectValue = 3;
							break;

						case 284: // Reinforcement amulet
							item.Effect = 793;
							item.EffectValue = 3;
							break;

						case 4264: // Heroic
							item.Effect = 794;
							item.EffectValue = 3;
							break;

						case 4262: // Random heroic
							item.Effect = 795;
							item.EffectValue = 3;
							break;

						default:
							item.EffectValue = Integer.parseInt(currentLine[7]);
							break;
						}
					} else {
						item.Morph = (short) Integer.parseInt(currentLine[7]);
					}
					break;
				}
			} else if (currentLine.length > 3 && currentLine[1].equals("TYPE")) {
				// currentLine[2] 0-range 2-range 3-magic
				item.Class = item.EquipmentSlot == EquipmentType.Fairy ? (byte) 15 : Byte.parseByte(currentLine[3]);
			} else if (currentLine.length > 3 && currentLine[1].equals("FLAG")) {
				item.IsSoldable = currentLine[5].equals("0");
				item.IsDroppable = currentLine[6].equals("0");
				item.IsTradable = currentLine[7].equals("0");
				item.IsMinilandActionable = currentLine[8].equals("1");
				item.IsWarehouse = currentLine[9].equals("1");
				item.Flag9 = currentLine[10].equals("1");
				item.Flag1 = currentLine[11].equals("1");
				item.Flag2 = currentLine[12].equals("1");
				item.Flag3 = currentLine[13].equals("1");
				item.Flag4 = currentLine[14].equals("1");
				item.Flag5 = currentLine[15].equals("1");
				item.IsColored = currentLine[16].equals("1");
				item.Sex = currentLine[18].equals("1") ? (byte) 1 : currentLine[17].equals("1") ? (byte) 2 : (byte) 0;
				// not used item.Flag6 = currentLine[19] == "1";
				item.Flag6 = currentLine[20].equals("1");
				if (currentLine[21].equals("1")) {
					item.ReputPrice = item.Price;
				}
				item.IsHeroic = currentLine[22].equals("1");
				item.Flag7 = currentLine[23].equals("1");
				item.Flag8 = currentLine[24].equals("1");

			} else if (currentLine.length > 1 && currentLine[1].equals("DATA")) {
				switch (item.ItemType) {
				case Weapon:
					item.LevelMinimum = Short.parseShort(currentLine[2]);
					item.DamageMinimum = (short) Integer.parseInt(currentLine[3]);
					item.DamageMaximum = (short) Integer.parseInt(currentLine[4]);
					item.HitRate = (short) Integer.parseInt(currentLine[5]);
					item.CriticalLuckRate = Byte.parseByte(currentLine[6]);
					item.CriticalRate = (short) Integer.parseInt(currentLine[7]);
					item.BasicUpgrade = Byte.parseByte(currentLine[10]);
					item.MaximumAmmo = 100;
					break;

				case Armor:
					item.LevelMinimum = Short.parseShort(currentLine[2]);
					item.CloseDefence = (short) Integer.parseInt(currentLine[3]);
					item.DistanceDefence = (short) Integer.parseInt(currentLine[4]);
					item.MagicDefence = (short) Integer.parseInt(currentLine[5]);
					item.DefenceDodge = (short) Integer.parseInt(currentLine[6]);
					item.DistanceDefenceDodge = (short) Integer.parseInt(currentLine[6]);
					item.BasicUpgrade = Byte.parseByte(currentLine[10]);
					break;

				case Box:
					switch (item.VNum) {
					// add here your custom effect/effectvalue for box item,
					// make
					// sure its unique for boxitems

					case 287:
						item.Effect = 69;
						item.EffectValue = 1;
						break;

					case 4240:
						item.Effect = 69;
						item.EffectValue = 2;
						break;

					case 4194:
						item.Effect = 69;
						item.EffectValue = 3;
						break;

					case 4106:
						item.Effect = 69;
						item.EffectValue = 4;
						break;

					case 185: // Hatus
					case 302: // Classic
					case 882: // Morcos
					case 942: // Calvina
					case 999: // Berios
						item.Effect = 999;
						break;

					default:
						item.Effect = (short) Integer.parseInt(currentLine[2]);
						item.EffectValue = Integer.parseInt(currentLine[3]);
						item.LevelMinimum = Short.parseShort(currentLine[4]);
						break;
					}
					break;

				case Fashion:
					item.LevelMinimum = Short.parseShort(currentLine[2]);
					item.CloseDefence = (short) Integer.parseInt(currentLine[3]);
					item.DistanceDefence = (short) Integer.parseInt(currentLine[4]);
					item.MagicDefence = (short) Integer.parseInt(currentLine[5]);
					item.DefenceDodge = (short) Integer.parseInt(currentLine[6]);
					if (item.EquipmentSlot == (EquipmentType.CostumeHat)
							|| item.EquipmentSlot == (EquipmentType.CostumeSuit)) {
						item.ItemValidTime = Integer.parseInt(currentLine[13]) * 3600;
					}
					break;

				case Food:
					item.Hp = (short) Integer.parseInt(currentLine[2]);
					item.Mp = (short) Integer.parseInt(currentLine[4]);
					break;

				case Jewelery:
					switch (item.EquipmentSlot) {
					case Amulet:
						item.LevelMinimum = Short.parseShort(currentLine[2]);
						if (item.VNum > 4055 && item.VNum < 4061 || item.VNum > 4172 && item.VNum < 4176) {
							item.ItemValidTime = 10800;
						} else if (item.VNum > 4045 && item.VNum < 4056 || item.VNum == 967 || item.VNum == 968) {
							// (item.VNum > 8104 && item.VNum < 8115) <= disaled
							// for now
							// because doesn't work!
							item.ItemValidTime = 10800;
						} else {
							item.ItemValidTime = Integer.parseInt(currentLine[3]) / 10;
						}
						break;
					case Fairy:
						item.Element = Byte.parseByte(currentLine[2]);
						item.ElementRate = (short) Integer.parseInt(currentLine[3]);
						if (item.VNum <= 256) {
							item.MaxElementRate = 50;
						} else {
							if (item.ElementRate == 0) {
								if (item.VNum >= 800 && item.VNum <= 804) {
									item.MaxElementRate = 50;
								} else {
									item.MaxElementRate = 70;
								}
							} else if (item.ElementRate == 30) {
								if (item.VNum >= 884 && item.VNum <= 887) {
									item.MaxElementRate = 50;
								} else {
									item.MaxElementRate = 30;
								}
							} else if (item.ElementRate == 35) {
								item.MaxElementRate = 35;
							} else if (item.ElementRate == 40) {
								item.MaxElementRate = 70;
							} else if (item.ElementRate == 50) {
								item.MaxElementRate = 80;
							}
						}
						break;
					default:
						item.LevelMinimum = Short.parseShort(currentLine[2]);
						item.MaxCellonLvl = Short.parseShort(currentLine[3]);
						item.MaxCellon = Short.parseShort(currentLine[4]);
						break;
					}
					break;

				case Event:
					switch (item.VNum) {
					case 1332:
						item.EffectValue = 5108;
						break;

					case 1333:
						item.EffectValue = 5109;
						break;

					case 1334:
						item.EffectValue = 5111;
						break;

					case 1335:
						item.EffectValue = 5107;
						break;

					case 1336:
						item.EffectValue = 5106;
						break;

					case 1337:
						item.EffectValue = 5110;
						break;

					case 1339:
						item.EffectValue = 5114;
						break;

					case 9031:
						item.EffectValue = 5108;
						break;

					case 9032:
						item.EffectValue = 5109;
						break;

					case 9033:
						item.EffectValue = 5011;
						break;

					case 9034:
						item.EffectValue = 5107;
						break;

					case 9035:
						item.EffectValue = 5106;
						break;

					case 9036:
						item.EffectValue = 5110;
						break;

					case 9038:
						item.EffectValue = 5114;
						break;

					// EffectItems aka. fireworks
					case 1581:
						item.EffectValue = 860;
						break;

					case 1582:
						item.EffectValue = 861;
						break;

					case 1585:
						item.EffectValue = 859;
						break;

					case 1983:
						item.EffectValue = 875;
						break;

					case 1984:
						item.EffectValue = 876;
						break;

					case 1985:
						item.EffectValue = 877;
						break;

					case 1986:
						item.EffectValue = 878;
						break;

					case 1987:
						item.EffectValue = 879;
						break;

					case 1988:
						item.EffectValue = 880;
						break;

					case 9044:
						item.EffectValue = 859;
						break;

					case 9059:
						item.EffectValue = 875;
						break;

					case 9060:
						item.EffectValue = 876;
						break;

					case 9061:
						item.EffectValue = 877;
						break;

					case 9062:
						item.EffectValue = 878;
						break;

					case 9063:
						item.EffectValue = 879;
						break;

					case 9064:
						item.EffectValue = 880;
						break;

					default:
						item.EffectValue = Integer.parseInt(currentLine[7]);
						break;
					}
					break;

				case Special:
					switch (item.VNum) {
					case 1246:
					case 9020:
						item.Effect = 6600;
						item.EffectValue = 1;
						break;

					case 1247:
					case 9021:
						item.Effect = 6600;
						item.EffectValue = 2;
						break;

					case 1248:
					case 9022:
						item.Effect = 6600;
						item.EffectValue = 3;
						break;

					case 1249:
					case 9023:
						item.Effect = 6600;
						item.EffectValue = 4;
						break;

					case 5130:
					case 9072:
						item.Effect = 1006;
						break;

					case 1272:
					case 1858:
					case 9047:
						item.Effect = 1005;
						item.EffectValue = 10;
						break;

					case 1273:
					case 9024:
						item.Effect = 1005;
						item.EffectValue = 30;
						break;

					case 1274:
					case 9025:
						item.Effect = 1005;
						item.EffectValue = 60;
						break;

					case 1279:
					case 9029:
						item.Effect = 1007;
						item.EffectValue = 30;
						break;

					case 1280:
					case 9030:
						item.Effect = 1007;
						item.EffectValue = 60;
						break;

					case 1923:
					case 9056:
						item.Effect = 1007;
						item.EffectValue = 10;
						break;

					case 1275:
					case 1886:
					case 9026:
						item.Effect = 1008;
						item.EffectValue = 10;
						break;

					case 1276:
					case 9027:
						item.Effect = 1008;
						item.EffectValue = 30;
						break;

					case 1277:
					case 9028:
						item.Effect = 1008;
						item.EffectValue = 60;
						break;

					case 5060:
					case 9066:
						item.Effect = 1003;
						item.EffectValue = 30;
						break;

					case 5061:
					case 9067:
						item.Effect = 1004;
						item.EffectValue = 7;
						break;

					case 5062:
					case 9068:
						item.Effect = 1004;
						item.EffectValue = 1;
						break;

					case 5105:
						item.Effect = 651;
						break;

					case 5115:
						item.Effect = 652;
						break;

					case 1981:
						item.Effect = 34; // imagined number as for I = √(-1),
											// complex z = a + bi
						break;

					case 1982:
						item.Effect = 6969; // imagined number as for I = √(-1),
											// complex z = a + bi
						break;

					case 1894:
					case 1895:
					case 1896:
					case 1897:
					case 1898:
					case 1899:
					case 1900:
					case 1901:
					case 1902:
					case 1903:
						item.Effect = 789;
						item.EffectValue = item.VNum + 2152;
						break;

					case 4046:
					case 4047:
					case 4048:
					case 4049:
					case 4050:
					case 4051:
					case 4052:
					case 4053:
					case 4054:
					case 4055:
						item.Effect = 790;
						break;

					default:
						if (item.VNum > 5891 && item.VNum < 5900 || item.VNum > 9100 && item.VNum < 9109) {
							item.Effect = 69; // imagined number as for I =
												// √(-1), complex z = a + bi
						} else {
							item.Effect = (short) Integer.parseInt(currentLine[2]);
						}
						break;
					}
					switch (item.Effect) {
					case 150:
					case 151:
						if (Integer.parseInt(currentLine[4]) == 1) {
							item.EffectValue = 30000;
						} else if (Integer.parseInt(currentLine[4]) == 2) {
							item.EffectValue = 70000;
						} else if (Integer.parseInt(currentLine[4]) == 3) {
							item.EffectValue = 180000;
						} else {
							item.EffectValue = Integer.parseInt(currentLine[4]);
						}
						break;

					case 204:
						item.EffectValue = 10000;
						break;

					case 305:
						item.EffectValue = (short) Integer.parseInt(currentLine[5]);
						item.Morph = (short) Integer.parseInt(currentLine[4]);
						break;

					default:
						item.EffectValue = item.EffectValue == 0 ? Integer.parseInt(currentLine[4]) : item.EffectValue;
						break;
					}
					item.WaitDelay = 5000;
					break;

				case Magical:
					if (item.VNum > 2059 && item.VNum < 2070) {
						item.Effect = 10;
					} else {
						item.Effect = (short) Integer.parseInt(currentLine[2]);
					}
					item.EffectValue = Integer.parseInt(currentLine[4]);
					break;

				case Specialist:

					// item.isSpecialist = Short.parseShort(currentLine[2]);
					// item.Unknown = Integer.parseInt(currentLine[3]);
					item.ElementRate = (short) Integer.parseInt(currentLine[4]);
					item.Speed = Short.parseShort(currentLine[5]);
					item.SpType = Byte.parseByte(currentLine[13]);

					// item.Morph = Integer.parseInt(currentLine[14]) + 1;
					item.FireResistance = Short.parseShort(currentLine[15]);
					item.WaterResistance = Short.parseShort(currentLine[16]);
					item.LightResistance = Short.parseShort(currentLine[17]);
					item.DarkResistance = Short.parseShort(currentLine[18]);

					// item.PartnerClass = Integer.parseInt(currentLine[19]);
					item.LevelJobMinimum = Byte.parseByte(currentLine[20]);
					item.ReputationMinimum = Byte.parseByte(currentLine[21]);

					// needs to be hardcoded
					switch (item.VNum) {
					case 901:
						item.Element = 1;
						break;

					case 903:
						item.Element = 2;
						break;

					case 906:
						item.Element = 3;
						break;

					case 909:
						item.Element = 3;
						break;
					}
					break;

				case Shell:

					// item.ShellMinimumLevel = Integer.parseInt(linesave[3]);
					// item.ShellMaximumLevel = Integer.parseInt(linesave[4]);
					// item.ShellType = Short.parseShort(linesave[5]); // 3
					// shells of each type
					break;

				case Main:
					item.Effect = (short) Integer.parseInt(currentLine[2]);
					item.EffectValue = Integer.parseInt(currentLine[4]);
					break;

				case Upgrade:
					item.Effect = (short) Integer.parseInt(currentLine[2]);
					switch (item.VNum) {
					// UpgradeItems (needed to be hardcoded)
					case 1218:
						item.EffectValue = 26;
						break;

					case 1363:
						item.EffectValue = 27;
						break;

					case 1364:
						item.EffectValue = 28;
						break;

					case 5107:
						item.EffectValue = 47;
						break;

					case 5207:
						item.EffectValue = 50;
						break;

					case 5369:
						item.EffectValue = 61;
						break;

					case 5519:
						item.EffectValue = 60;
						break;

					default:
						item.EffectValue = Integer.parseInt(currentLine[4]);
						break;
					}
					break;

				case Production:
					item.Effect = (short) Integer.parseInt(currentLine[2]);
					item.EffectValue = Integer.parseInt(currentLine[4]);
					break;

				case Map:
					item.Effect = (short) Integer.parseInt(currentLine[2]);
					item.EffectValue = Integer.parseInt(currentLine[4]);
					break;

				case Potion:
					item.Hp = (short) Integer.parseInt(currentLine[2]);
					item.Mp = (short) Integer.parseInt(currentLine[4]);
					break;

				case Snack:
					item.Hp = (short) Integer.parseInt(currentLine[2]);
					item.Mp = (short) Integer.parseInt(currentLine[4]);
					break;

				case Teacher:
					item.Effect = (short) Integer.parseInt(currentLine[2]);
					item.EffectValue = Integer.parseInt(currentLine[4]);

					// item.PetLoyality = Integer.parseInt(linesave[4]);
					// item.PetFood = Integer.parseInt(linesave[7]);
					break;

				case Part:

					// nothing to parse
					break;

				case Sell:

					// nothing to parse
					break;

				case Quest2:

					// nothing to parse
					break;

				case Quest1:

					// nothing to parse
					break;

				case Ammo:

					// nothing to parse
					break;
				default:
					break;
				}

				if (item.Type == InventoryType.Miniland) {
					item.MinilandObjectPoint = Integer.parseInt(currentLine[2]);
					item.EffectValue = Short.parseShort(currentLine[8]);
					item.Width = Byte.parseByte(currentLine[9]);
					item.Height = Byte.parseByte(currentLine[10]);
				}

				if (item.EquipmentSlot != EquipmentType.Boots && item.EquipmentSlot != EquipmentType.Gloves
						|| item.Type != InventoryType.Equipment) {
					continue;
				}
				item.FireResistance = Short.parseShort(currentLine[7]);
				item.WaterResistance = Short.parseShort(currentLine[8]);
				item.LightResistance = Short.parseShort(currentLine[9]);
				item.DarkResistance = Short.parseShort(currentLine[11]);
			} else if (currentLine.length > 1 && currentLine[1].equals("BUFF")) {
				// BUFF CARD
			}

		}

		String line = "";
		try (InputStream fis = new FileInputStream("resources/names/cz/item.txt");
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);) {
			while ((line = br.readLine()) != null) {
				String[] currentLine = line.split("\\s+");
				int id = Integer.parseInt(currentLine[0].substring(3, currentLine[0].length() - 1));

				items.forEach((k, v) -> {
					v.Name = "";
					if (v.NameID.equals("zts" + id + "e")) {
						for (int ii = 1; ii < currentLine.length; ii++) {
							v.Name += currentLine[ii] + " ";
						}

						items.put(k, v);
					}

				});

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

}
