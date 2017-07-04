package nostale.resources;
import java.io.*;
import java.util.ArrayList;

import nostale.data.Skill;
public class LoadSkills
{

    public LoadSkills()
    {

    }
    
    public static Skill[] loadSkills() throws Exception{
        //resources/Skill.dat
        //resources/names/cz/skill.txt
        
        ArrayList<Skill> skills = new ArrayList<Skill>();
        String line;
        try (
        InputStream fis = new FileInputStream("resources/Skill.dat");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
         ) 
        {

           Skill skill = null;
           while ((line = br.readLine()) != null) {
             String[] currentLine = line.split("\\s+");
             if(currentLine.length>1)
             {if (currentLine.length > 2 && currentLine[1].equals("VNUM"))
             {
            	 if(skill!=null)
                 skills.add(skill);
                 skill = new Skill();
                 skill.VNUM = Short.parseShort(currentLine[2]);

             }
             else if (currentLine.length > 2 && currentLine[1].equals("NAME"))
             {
                 skill.NameID = currentLine[2];
             }
             else if (currentLine.length > 2 && currentLine[1].equals("TYPE"))
             {
                 skill.SkillType = Short.parseShort(currentLine[2]);
                 skill.CastId = Short.parseShort(currentLine[3]);
                 skill.Class = Short.parseShort(currentLine[4]);
                 skill.Type = Short.parseShort(currentLine[5]);
                 skill.Element = Short.parseShort(currentLine[7]);
             }
             else if (currentLine.length > 3 && currentLine[1].equals("COST"))
             {
                 skill.CPCost = currentLine[2].equals("-1") ? (byte)0 : Short.parseShort(currentLine[2]);
                 skill.Price = Integer.parseInt(currentLine[3]);
             }
          
             else if (currentLine.length > 2 && currentLine[1].equals("EFFECT"))
             {
                 // skill.Unknown = Short.parseShort(currentLine[2]);
                 skill.CastEffect = Short.parseShort(currentLine[3]);
                 skill.CastAnimation = Short.parseShort(currentLine[4]);
                 skill.Effect = Short.parseShort(currentLine[5]);
                 skill.AttackAnimation = Short.parseShort(currentLine[6]);
             }
             else if (currentLine.length > 2 && currentLine[1].equals("TARGET"))
             {
                 // 1&2 used as type third unknown
                 skill.TargetType = Short.parseShort(currentLine[2]);
                 skill.HitType = Short.parseShort(currentLine[3]);
                 skill.TargetRange = Short.parseShort(currentLine[5]);
             }
             else if (currentLine.length > 2 && currentLine[1].equals("DATA"))
             {
                 skill.UpgradeSkill = Short.parseShort(currentLine[2]);
                 skill.UpgradeType = Short.parseShort(currentLine[3]);
                 skill.CastTime = Short.parseShort(currentLine[6]);
                 skill.Cooldown = Short.parseShort(currentLine[7]);
                 skill.MpCost = Short.parseShort(currentLine[10]);
                 skill.ItemVNum = Short.parseShort(currentLine[12]);
                 skill.Range = Short.parseShort(currentLine[13]);
             }
             else if (currentLine.length > 2 && currentLine[1].equals("BASIC"))
             {
                 switch (currentLine[2])
                 {
                     case "0":

                         // All needs to be divided by 4
                         if (currentLine[3].equals("3"))
                         {
                             skill.Damage = Short.parseShort(currentLine[5]);
                         }
                         if (currentLine[3].equals("7"))
                         {
                             skill.Damage = Short.parseShort(currentLine[5]);
                         }
                         if (currentLine[3].equals("29"))
                         {
                             skill.SkillChance = Short.parseShort(currentLine[5]);

                             // skill.MonsterVNum = Short.parseShort(currentLine[6]);
                         }
                         if (currentLine[3].equals("43"))
                         {
                             // skill.AdditionalDamage = Short.parseShort(currentLine[5]);
                         }
                         if (currentLine[3].equals("48"))
                         {
                             // skill.MonsterSpawnAmount = Short.parseShort(currentLine[5]);
                             // skill.MonsterVNum = Short.parseShort(currentLine[6]);
                         }
                         if (currentLine[3].equals("64"))
                         {
                             skill.SkillChance = Short.parseShort(currentLine[5]);

                             // skill.Unknown = Short.parseShort(currentLine[6]);
                         }
                         if (currentLine[3].equals("66"))
                         {
                             skill.SkillChance = Short.parseShort(currentLine[5]);

                             // skill.Unknown = Short.parseShort(currentLine[6]);
                         }
                         if (currentLine[3].equals("68"))
                         {
                             skill.SkillChance = Short.parseShort(currentLine[5]);
                             if (currentLine[4].equals("0"))
                             {
                                 skill.SecondarySkillVNum = Short.parseShort(currentLine[6]);
                             }
                             else
                             {
                                 skill.BuffId = Short.parseShort(currentLine[6]);
                             }
                         }
                         if (currentLine[3].equals("69"))
                         {
                             skill.SkillChance = Short.parseShort(currentLine[5]);

                             // skill.MonsterVNum = Short.parseShort(currentLine[6]);
                         }
                         if (currentLine[3].equals("72"))
                         {
                             // skill.Times = Short.parseShort(currentLine[5]);
                             skill.BuffId = Short.parseShort(currentLine[6]);
                         }
                         if (currentLine[3].equals("80"))
                         {
                             skill.SkillChance = Short.parseShort(currentLine[5]);

                             // skill.CloneAmount = Short.parseShort(currentLine[6]);
                         }
                         if (currentLine[3].equals("81"))
                         {
                             skill.SkillChance = Short.parseShort(currentLine[5]); // abs * 4
                             skill.BuffId = Short.parseShort(currentLine[6]);
                         }
                         else
                         {
                             skill.Damage = Short.parseShort(currentLine[5]);
                         }
                         break;

                     case "1":
                         skill.ElementalDamage = Short.parseShort(currentLine[5]); // Divide by 4(?)

                         // skill.Unknown =cskill.Unknown = Short.parseShort(currentLine[2]);
                         // skill.Unknown = Short.parseShort(currentLine[3]); skill.Unknown =
                         // Short.parseShort(currentLine[4]); skill.Unknown =
                         // Short.parseShort(currentLine[6]); skill.Unknown = Short.parseShort(currentLine[7]);
                         break;

                     case "2":

                         // unknown
                         /*
                         skill.Unknown = Short.parseShort(currentLine[2]);
                         skill.Unknown = Short.parseShort(currentLine[3]);
                         skill.Unknown = Short.parseShort(currentLine[4]);
                         skill.Unknown = Short.parseShort(currentLine[5]);
                         skill.Unknown = Short.parseShort(currentLine[6]);
                         skill.Unknown = Short.parseShort(currentLine[7]);
                         */
                         break;

                     case "3":

                         // unknown
                         /*
                         skill.Unknown = Short.parseShort(currentLine[2]);
                         skill.Unknown = Short.parseShort(currentLine[3]);
                         skill.Unknown = Short.parseShort(currentLine[4]);
                         skill.Unknown = Short.parseShort(currentLine[5]);
                         skill.Unknown = Short.parseShort(currentLine[6]);
                         skill.Unknown = Short.parseShort(currentLine[7]);
                         */
                         break;

                     case "4":

                         // unknown
                         /*
                         skill.Unknown = Short.parseShort(currentLine[2]);
                         skill.Unknown = Short.parseShort(currentLine[3]);
                         skill.Unknown = Short.parseShort(currentLine[4]);
                         skill.Unknown = Short.parseShort(currentLine[5]);
                         skill.Unknown = Short.parseShort(currentLine[6]);
                         skill.Unknown = Short.parseShort(currentLine[7]);
                         */
                         break;
                 }
             }
             else if (currentLine.length > 2 && currentLine[1].equals("FCOMBO"))
             {
                 /* // Parse when done
                 if (currentLine[2].equals("1")
                 {
                     combo.FirstActivationHit = Short.parseShort(currentLine[3]);
                     combo.FirstComboAttackAnimation = Short.parseShort(currentLine[4]);
                     combo.FirstComboEffect = Short.parseShort(currentLine[5]);
                     combo.SecondActivationHit = Short.parseShort(currentLine[3]);
                     combo.SecondComboAttackAnimation = Short.parseShort(currentLine[4]);
                     combo.SecondComboEffect = Short.parseShort(currentLine[5]);
                     combo.ThirdActivationHit = Short.parseShort(currentLine[3]);
                     combo.ThirdComboAttackAnimation = Short.parseShort(currentLine[4]);
                     combo.ThirdComboEffect = Short.parseShort(currentLine[5]);
                     combo.FourthActivationHit = Short.parseShort(currentLine[3]);
                     combo.FourthComboAttackAnimation = Short.parseShort(currentLine[4]);
                     combo.FourthComboEffect = Short.parseShort(currentLine[5]);
                     combo.FifthActivationHit = Short.parseShort(currentLine[3]);
                     combo.FifthComboAttackAnimation = Short.parseShort(currentLine[4]);
                     combo.FifthComboEffect = Short.parseShort(currentLine[5]);
                 }
                 */
             }
             else if (currentLine.length > 2 && currentLine[1].equals("CELL"))
             {
                 // skill.Unknown = Short.parseShort(currentLine[2]); // 2 - ??
             }
           }
        
        } }
        
        
        line="";
        try (
        InputStream fis2 = new FileInputStream("resources/names/cz/skill.txt");
        InputStreamReader isr2 = new InputStreamReader(fis2);
        BufferedReader br2 = new BufferedReader(isr2);
         ) 
        {
           while ((line = br2.readLine()) != null) {
                 String[] splited = line.split("\\s+");
                 String id = splited[0];
                 for(int i = 0;i<skills.size();i++)
                 {
                       if(skills.get(i).NameID.equals(id))
                       {
                         Skill tSkill = skills.get(i);
                         tSkill.Name = "";
                         //tItem.Name = splited[1];
                         
                         for(int ii=1;ii<splited.length;ii++)
                         {
                           tSkill.Name+= splited[ii]+" ";   
                         }
                         
                         skills.set(i,tSkill);
                       }
                    
                 }
           }
        
        } 
        
        return skills.toArray(new Skill[0]);
    }
    
}