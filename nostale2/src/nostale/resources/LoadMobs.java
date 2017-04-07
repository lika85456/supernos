package nostale.resources;
import java.io.*;
import java.util.ArrayList;

import nostale.struct.*;
public class LoadMobs
{

    public LoadMobs()
    {

    }
    
    public static Mob[] loadMobs(Item[] itemsss) throws Exception{
        //resources/Monster.dat
        //resources/names/cz/monster.txt
        
        ArrayList<Mob> items = new ArrayList<Mob>();
        String line;
        try (
        InputStream fis = new FileInputStream("resources/Monster.dat");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
         ) 
        {
  
        
            int[] basicHp = new int[100];
            int[] basicMp = new int[100];
            int[] basicXp = new int[100];
            int[] basicJXp = new int[100];

            // basicHpLoad
            int baseHp = 138;
            int basup = 17;
            for (int i = 0; i < 100; i++)
            {
                basicHp[i] = baseHp;
                basup++;
                baseHp += basup;

                if (i == 37)
                {
                    baseHp = 1765;
                    basup = 65;
                }
                if (i >= 41)
                {
                    if ((99 - i) % 8 == 0)
                    {
                        basup++;
                    }
                }
            }

           // basicMpLoad
           for (int i = 0; i < 100; i++)
           {
               basicMp[i] = basicHp[i];
           }

           // basicXPLoad
           for (int i = 0; i < 100; i++)
           {
               basicXp[i] = i * 180;
           }

           // basicJXpLoad
           for (int i = 0; i < 100; i++)
           {
               basicJXp[i] = 360;
           }

           long unknownData = 0;
           Mob npc = null;
           while ((line = br.readLine()) != null) {
             String[] currentLine = line.split("\\s+");
             
             if(currentLine.length>1)
             {
            	 if (currentLine.length > 2 && currentLine[1].equals("VNUM"))
                 {
                     if(npc!=null)
                     items.add(npc);
                     npc = new Mob();

                     npc.VNUM = Short.parseShort(currentLine[2]);
                 }
                 else if (currentLine.length > 2 && currentLine[1].equals("NAME"))
                 {
                     npc.NameID = Short.parseShort(currentLine[2].substring(3,4));
                 }
                 else if (currentLine.length > 2 && currentLine[1].equals("LEVEL"))
                 {
                     npc.Level = Short.parseShort(currentLine[2]);
                 }
                 else if (currentLine.length > 3 && currentLine[1].equals("RACE"))
                 {
                     npc.Race = Short.parseShort(currentLine[2]);
                     npc.RaceType = Short.parseShort(currentLine[3]);
                 }
                 else if (currentLine.length > 7 && currentLine[1].equals("ATTRIB"))
                 {
                     npc.Element = Short.parseShort(currentLine[2]);
                     npc.ElementRate = Short.parseShort(currentLine[3]);
                     npc.FireResistance = Short.parseShort(currentLine[4]);
                     npc.WaterResistance = Short.parseShort(currentLine[5]);
                     npc.LightResistance = Short.parseShort(currentLine[6]);
                     npc.DarkResistance = Short.parseShort(currentLine[7]);
                 }
                 else if (currentLine.length > 3 && currentLine[1].equals("HP/MP"))
                 {
                     npc.MaxHp = Integer.parseInt(currentLine[2]) + basicHp[npc.Level];
                     npc.MaxMp = Integer.parseInt(currentLine[3]) + basicMp[npc.Level];
                 }
                 else if (currentLine.length > 2 && currentLine[1].equals("EXP"))
                 {
                     npc.XP = Math.abs(Integer.parseInt(currentLine[2]) + basicXp[npc.Level]);
                     npc.JobXP = Integer.parseInt(currentLine[3]) + basicJXp[npc.Level];
                 }
                 else if (currentLine.length > 6 && currentLine[1].equals("PREATT"))
                 {
                     npc.IsHostile = currentLine[2] != "0";
                     npc.NoticeRange = Short.parseShort(currentLine[4]);
                     npc.Speed = Short.parseShort(currentLine[5]);
                     npc.RespawnTime = Integer.parseInt(currentLine[6]);
                 }
                 else if (currentLine.length > 6 && currentLine[1].equals("WEAPON"))
                 {
                     if (currentLine[3].equals("1"))
                     {
                         npc.DamageMinimum = (short)((Short.parseShort(currentLine[2]) - 1) * 4 + 32 + Short.parseShort(currentLine[4]) + Math.round(((npc.Level - 1) / 5)));
                         npc.DamageMaximum = (short)((Short.parseShort(currentLine[2]) - 1) * 6 + 40 + Short.parseShort(currentLine[5]) - Math.round(((npc.Level - 1) / 5)));
                         npc.Concentrate = (short)((Short.parseShort(currentLine[2]) - 1) * 5 + 27 + Short.parseShort(currentLine[6]));
                         npc.CriticalChance = (short)(4 + Short.parseShort(currentLine[7]));
                         npc.CriticalRate = (short)(70 + Short.parseShort(currentLine[8]));
                     }
                     else if (currentLine[3].equals("2"))
                     {
                         npc.DamageMinimum = (short)(Short.parseShort(currentLine[2]) * 6.5f + 23 + Short.parseShort(currentLine[4]));
                         npc.DamageMaximum = (short)((Short.parseShort(currentLine[2]) - 1) * 8 + 38 + Short.parseShort(currentLine[5]));
                         npc.Concentrate = (short)(70 + Short.parseShort(currentLine[6]));
                     }
                 }
                 else if (currentLine.length > 6 && currentLine[1].equals("ARMOR"))
                 {
                     npc.CloseDefence = (short)((Short.parseShort(currentLine[2]) - 1) * 2 + 18);
                     npc.DistanceDefence = (short)((Short.parseShort(currentLine[2]) - 1) * 3 + 17);
                     npc.MagicDefence = (short)((Short.parseShort(currentLine[2]) - 1) * 2 + 13);
                     npc.DefenceDodge = (short)((Short.parseShort(currentLine[2]) - 1) * 5 + 31);
                     npc.DistanceDefenceDodge = (short)((Short.parseShort(currentLine[2]) - 1) * 5 + 31);
                 }
                 else if (currentLine.length > 7 && currentLine[1].equals("ETC"))
                 {
                     unknownData = Integer.parseInt(currentLine[2]);
                     if (unknownData == -2147481593)
                     {
                         npc.MonsterType = MonsterType.Special;
                     }
                     if (unknownData == -2147483616 || unknownData == -2147483647 || unknownData == -2147483646)
                     {
                         if (npc.Race == 8 && npc.RaceType == 0)
                         {
                             npc.NoAggresiveIcon = true;
                         }
                         else
                         {
                             npc.NoAggresiveIcon = false;
                         }
                     }
                     if (npc.VNUM >= 588 && npc.VNUM <= 607)
                     {
                         npc.MonsterType = MonsterType.Elite;
                     }
                 }
                 else if (currentLine.length > 6 && currentLine[1].equals("SETTING"))
                 {
                     if (currentLine[4] != "0")
                     {
                         npc.VNumRequired = Short.parseShort(currentLine[4]);
                         npc.AmountRequired = 1;
                     }
                 }
                 else if (currentLine.length > 4 && currentLine[1].equals("PETINFO"))
                 {
                     if (npc.VNumRequired == 0 && (unknownData == -2147481593 || unknownData == -2147481599 || unknownData == -1610610681))
                     {
                         npc.VNumRequired = Short.parseShort(currentLine[2]);
                         npc.AmountRequired = Short.parseShort(currentLine[3]);
                     }
                 }
                 else if (currentLine.length > 2 && currentLine[1].equals("EFF"))
                 {
                     npc.BasicSkill = Short.parseShort(currentLine[2]);
                 }
                 else if (currentLine.length > 8 && currentLine[1].equals("ZSKILL"))
                 {
                     npc.AttackClass = Short.parseShort(currentLine[2]);
                     npc.BasicRange = Short.parseShort(currentLine[3]);
                     npc.BasicArea = Short.parseShort(currentLine[5]);
                     npc.BasicCooldown = Short.parseShort(currentLine[6]);
                 }
                 else if (currentLine.length > 4 && currentLine[1].equals("WINFO"))
                 {
                     npc.AttackUpgrade = Short.parseShort(unknownData == 1 ? currentLine[2] : currentLine[4]);
                 }
                 else if (currentLine.length > 3 && currentLine[1].equals("AINFO"))
                 {
                     npc.DefenceUpgrade = Short.parseShort(unknownData == 1 ? currentLine[2] : currentLine[3]);
                 }




             
             
           }
        
        } 
        }

        line="";
        try (
        InputStream fis2 = new FileInputStream("resources/names/cz/monster.txt");
        InputStreamReader isr2 = new InputStreamReader(fis2);
        BufferedReader br2 = new BufferedReader(isr2);
         ) 
        {
           while ((line = br2.readLine()) != null) {
                 String[] splited = line.split("\\s+");
                 int id = Integer.parseInt(splited[0].substring(3,4));
                 
                 for(int i = 0;i<items.size();i++)
                 {
                       if(items.get(i).NameID==id)
                       {
                    	 
                         Mob tItem = items.get(i);
                         tItem.Name = "";
                         for(int asid = 1;asid<splited.length;asid++)
                         tItem.Name += splited[asid]+" ";
                         
                         //for(int ii=1;ii<splited.length;ii++)
                         //{
                         //  tItem.Name+= splited[ii]+" ";   
                         //}
                         
                         items.set(i,tItem);
                       }
                    
                 }
           }
        
        } 
        
        return items.toArray(new Mob[0]);
       
    
    }
}
