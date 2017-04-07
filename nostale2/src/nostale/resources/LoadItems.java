package nostale.resources;
import java.io.*;
import nostale.struct.*;
import java.util.ArrayList;
public class LoadItems
{

    public LoadItems()
    {

    }
    
    public static Item[] loadItems() throws Exception{
        //resources/monster.dat
        //resources/names/cz/item.txt
        
        ArrayList<Item> items = new ArrayList<Item>();
        String line;
        try (
        InputStream fis = new FileInputStream("resources/Item.dat");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
         ) 
        {

           Item item = new Item();
           while ((line = br.readLine()) != null) {
             String[] currentLine = line.split("\\s+");
             if(currentLine.length>1)
             {
               if(currentLine[1].equals("VNUM"))
                  item.VNUM = (short)Integer.parseInt(currentLine[2]);
               else if(currentLine[1].equals("NAME"))
                  item.NameID = Integer.parseInt(currentLine[2].substring(3,currentLine[2].length()-1));
               else if(currentLine[1].equals("FLAG"))
                  {
                  item.IsSoldable = currentLine[5] == "0";
                  item.IsDroppable = currentLine[6] == "0";
                  item.IsTradable = currentLine[7] == "0";
                  item.IsBlocked = currentLine[8] == "1";
                  item.IsMinilandObject = currentLine[9] == "1";
                  item.IsHolder = currentLine[10] == "1";
                  item.IsColored = currentLine[16] == "1";
                  item.Sex = currentLine[18] == "1" ? (byte)1 : currentLine[17] == "1" ? (byte)2 : (byte)0;
                  if (currentLine[21] == "1")
                  {
                      item.ReputPrice = item.Price;
                  }
                  item.IsHeroic = currentLine[22] == "1";
                  }
               else if(currentLine[1].equals("INDEX"))
               {
                  switch(Integer.parseInt(currentLine[2]))
                  {
                      case 0:
                      item.Type = InventoryType.Equipment;
                      break;
                      case 1:
                      item.Type = InventoryType.Main;
                      break;
                      case 2:
                      item.Type = InventoryType.Etc;
                      break;
                      case 3:
                      item.Type = InventoryType.Miniland;
                      break;
                      case 6:
                      item.Type = InventoryType.Specialist;
                      break;
                      case 7:
                      item.Type = InventoryType.Costume;
                      break;
                      case 5:
                      item.Type = InventoryType.Specialist;
                      break;
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

                  }
                            
                  item.ItemType = currentLine[3] != "-1" ? ItemType.get(Integer.parseInt(currentLine[3])) : ItemType.Weapon;
                  item.ItemSubType = (byte)Integer.parseInt(currentLine[4]);
                  item.EquipmentSlot = EquipmentType.get(Integer.parseInt(currentLine[5] != "-1" ? currentLine[5] : "0"));

               }
               else if(currentLine[1].equals("TYPE"))
               {
            	   item.Class = (byte)(item.EquipmentSlot == EquipmentType.Fairy ? (byte)15 : Integer.parseInt(currentLine[3]));
               }
               else if (currentLine.length > 1 && currentLine[1] == "DATA")
               {
                   switch (item.ItemType)
                   {
                       case Weapon:
                           item.LevelMinimum = Short.parseShort(currentLine[2]);
                           item.DamageMinimum = Short.parseShort(currentLine[3]);
                           item.DamageMaximum = Short.parseShort(currentLine[4]);
                           item.HitRate = Short.parseShort(currentLine[5]);
                           item.CriticalLuckRate = Short.parseShort(currentLine[6]);
                           item.CriticalRate = Short.parseShort(currentLine[7]);
                           item.BasicUpgrade = Short.parseShort(currentLine[10]);
                           item.MaximumAmmo = 100;
                           break;

                       case Armor:
                           item.LevelMinimum = Short.parseShort(currentLine[2]);
                           item.CloseDefence = Short.parseShort(currentLine[3]);
                           item.DistanceDefence = Short.parseShort(currentLine[4]);
                           item.MagicDefence = Short.parseShort(currentLine[5]);
                           item.DefenceDodge = Short.parseShort(currentLine[6]);
                           item.DistanceDefenceDodge = Short.parseShort(currentLine[6]);
                           item.BasicUpgrade = Short.parseShort(currentLine[10]);
                           break;

                       case Box:
                           switch (item.VNUM)
                           {
                               // add here your custom effect/effectvalue for box item, make sure its unique for boxitems

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

                               default:
                                   item.Effect = Short.parseShort(currentLine[2]);
                                   item.EffectValue = Integer.parseInt(currentLine[3]);
                                   break;
                           }
                           break;

                       case Fashion:
                           item.LevelMinimum = Short.parseShort(currentLine[2]);
                           item.CloseDefence = Short.parseShort(currentLine[3]);
                           item.DistanceDefence = Short.parseShort(currentLine[4]);
                           item.MagicDefence = Short.parseShort(currentLine[5]);
                           item.DefenceDodge = Short.parseShort(currentLine[6]);
                           if (item.EquipmentSlot==(EquipmentType.CostumeHat) || item.EquipmentSlot==(EquipmentType.CostumeSuit))
                           {
                               item.ItemValidTime = Integer.parseInt(currentLine[13]) * 3600;
                           }
                           break;

                       case Food:
                           item.Hp = Short.parseShort(currentLine[2]);
                           item.Mp = Short.parseShort(currentLine[4]);
                           break;

                       case Jewelery:
                           if (item.EquipmentSlot==(EquipmentType.Amulet))
                           {
                               item.LevelMinimum = Short.parseShort(currentLine[2]);
                               if (item.VNUM > 4055 && item.VNUM < 4061 || item.VNUM > 4172 && item.VNUM < 4176)
                               {
                                   item.ItemValidTime = 10800;
                               }
                               else if (item.VNUM > 4045 && item.VNUM < 4056 || item.VNUM == 967 || item.VNUM == 968)
                               {
                                   // (item.VNUM > 8104 && item.VNUM < 8115) <= disaled for now
                                   // because doesn't work!
                                   item.ItemValidTime = 3600;
                               }
                               else
                               {
                                   item.ItemValidTime = Integer.parseInt(currentLine[3]) / 10;
                               }
                           }
                           else if (item.EquipmentSlot==(EquipmentType.Fairy))
                           {
                               item.Element = Short.parseShort(currentLine[2]);
                               item.ElementRate = Short.parseShort(currentLine[3]);
                               if (item.VNUM <= 256)
                               {
                                   item.MaxElementRate = 50;
                               }
                               else
                               {
                                   if (item.ElementRate == 0)
                                   {
                                       if (item.VNUM >= 800 && item.VNUM <= 804)
                                       {
                                           item.MaxElementRate = 50;
                                       }
                                       else
                                       {
                                           item.MaxElementRate = 70;
                                       }
                                   }
                                   else if (item.ElementRate == 30)
                                   {
                                       if (item.VNUM >= 884 && item.VNUM <= 887)
                                       {
                                           item.MaxElementRate = 50;
                                       }
                                       else
                                       {
                                           item.MaxElementRate = 30;
                                       }
                                   }
                                   else if (item.ElementRate == 35)
                                   {
                                       item.MaxElementRate = 35;
                                   }
                                   else if (item.ElementRate == 40)
                                   {
                                       item.MaxElementRate = 70;
                                   }
                                   else if (item.ElementRate == 50)
                                   {
                                       item.MaxElementRate = 80;
                                   }
                               }
                           }
                           else
                           {
                               item.LevelMinimum = Short.parseShort(currentLine[2]);
                               item.MaxCellonLvl = Short.parseShort(currentLine[3]);
                               item.MaxCellon = Short.parseShort(currentLine[4]);
                           }
                           break;

                       case Event:
                           switch (item.VNUM)
                           {
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
                                   item.EffectValue = Short.parseShort(currentLine[7]);
                                   break;
                           }
                           break;

                       case Special:
                           switch (item.VNUM)
                           {
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
                                   item.Effect = 34; // imagined number as for I = √(-1), complex z = a + bi
                                   break;

                               case 1982:
                                   item.Effect = 6969; // imagined number as for I = √(-1), complex z = a + bi
                                   break;

                               default:
                                   if (item.VNUM > 5891 && item.VNUM < 5900 || item.VNUM > 9100 && item.VNUM < 9109)
                                   {
                                       item.Effect = 69; // imagined number as for I = √(-1), complex z = a + bi
                                   }
                                   else
                                   {
                                       item.Effect = Short.parseShort(currentLine[2]);
                                   }
                                   break;
                           }
                           switch (item.Effect)
                           {
                               case 150:
                               case 151:
                                   if (Integer.parseInt(currentLine[4]) == 1)
                                   {
                                       item.EffectValue = 30000;
                                   }
                                   else if (Integer.parseInt(currentLine[4]) == 2)
                                   {
                                       item.EffectValue = 70000;
                                   }
                                   else if (Integer.parseInt(currentLine[4]) == 3)
                                   {
                                       item.EffectValue = 180000;
                                   }
                                   else
                                   {
                                       item.EffectValue = Integer.parseInt(currentLine[4]);
                                   }
                                   break;

                               case 204:
                                   item.EffectValue = 10000;
                                   break;

                               default:
                                   item.EffectValue = item.EffectValue == 0 ? Integer.parseInt(currentLine[4]) : item.EffectValue;
                                   break;
                           }
                           item.WaitDelay = 5000;
                           break;

                       case Magical:
                           if (item.VNUM > 2059 && item.VNUM < 2070)
                           {
                               item.Effect = 10;
                           }
                           else
                           {
                               item.Effect = Short.parseShort(currentLine[2]);
                           }
                           item.EffectValue = Integer.parseInt(currentLine[4]);
                           break;

                       case Specialist:

                           // item.isSpecialist = Short.parseShort(currentLine[2]); item.Unknown = Short.parseShort(currentLine[3]);
                           item.ElementRate = Short.parseShort(currentLine[4]);
                           item.Speed = Short.parseShort(currentLine[5]);
                           item.SpType = Short.parseShort(currentLine[13]);

                           // item.Morph = Short.parseShort(currentLine[14]) + 1;
                           item.FireResistance = Short.parseShort(currentLine[15]);
                           item.WaterResistance = Short.parseShort(currentLine[16]);
                           item.LightResistance = Short.parseShort(currentLine[17]);
                           item.DarkResistance = Short.parseShort(currentLine[18]);

                           // item.PartnerClass = Short.parseShort(currentLine[19]);
                           item.LevelJobMinimum = Short.parseShort(currentLine[20]);
                           item.ReputationMinimum = Short.parseShort(currentLine[21]);

                           switch (item.VNUM)
                           {
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

                           // item.ShellMinimumLevel = Short.parseShort(linesave[3]);
                           // item.ShellMaximumLevel = Short.parseShort(linesave[4]);
                           // item.ShellType = Short.parseShort(linesave[5]); // 3 shells of each type
                           break;

                       case Main:
                           item.Effect = Short.parseShort(currentLine[2]);
                           item.EffectValue = Integer.parseInt(currentLine[4]);
                           break;

                       case Upgrade:
                           item.Effect = Short.parseShort(currentLine[2]);
                           switch (item.VNUM)
                           {
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
                           item.Effect = Short.parseShort(currentLine[2]);
                           item.EffectValue = Integer.parseInt(currentLine[4]);
                           break;

                       case Map:
                           item.Effect = Short.parseShort(currentLine[2]);
                           item.EffectValue = Integer.parseInt(currentLine[4]);
                           break;

                       case Potion:
                           item.Hp = Short.parseShort(currentLine[2]);
                           item.Mp = Short.parseShort(currentLine[4]);
                           break;

                       case Snack:
                           item.Hp = Short.parseShort(currentLine[2]);
                           item.Mp = Short.parseShort(currentLine[4]);
                           break;

                       case Teacher:
                           item.Effect = Short.parseShort(currentLine[2]);
                           item.EffectValue = Integer.parseInt(currentLine[4]);

                           // item.PetLoyality = Short.parseShort(linesave[4]); item.PetFood = Short.parseShort(linesave[7]);
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
                   }
                   if ((item.EquipmentSlot == EquipmentType.Boots || item.EquipmentSlot == EquipmentType.Gloves) && item.Type == InventoryType.get(0))
                   {
                       item.FireResistance = Short.parseShort(currentLine[7]);
                       item.WaterResistance = Short.parseShort(currentLine[8]);
                       item.LightResistance = Short.parseShort(currentLine[9]);
                       item.DarkResistance = Short.parseShort(currentLine[11]);
                   }
}
             }
             if(line.contains("#="))
             {
                  items.add(item);
                  item = new Item();
             }
           }
        
        } 
        
        
        line="";
        try (
        InputStream fis = new FileInputStream("resources/names/cz/item.txt");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
         ) 
        {
           while ((line = br.readLine()) != null) {
                 String[] currentLine = line.split("\\s+");
                 int id = Integer.parseInt(currentLine[0].substring(3,currentLine[0].length()-1));
                 
                 for(int i = 0;i<items.size();i++)
                 {
                       if(items.get(i).NameID==id)
                       {
                         Item tItem = items.get(i);
                         //tItem.Name = currentLine[1];
                         tItem.Name = "";
                         for(int ii=1;ii<currentLine.length;ii++)
                         {
                           tItem.Name+= currentLine[ii]+" ";   
                         }
                         
                         items.set(i,tItem);
                       }
                    
                 }
           }
        
        } 
        
        return items.toArray(new Item[0]);
       
    
    }


}
