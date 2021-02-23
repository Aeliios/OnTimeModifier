package fr.aeliios.ontimemodifier;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.mysql.jdbc.StringUtils;

import me.edge209.OnTime.OnTimeAPI;
import me.edge209.OnTime.OnTimeAPI.topData;

public class OnTimeModifier extends JavaPlugin implements Listener {
 
    @Override
    public void onEnable() {  
		getLogger().info("[OnTimeModifier] Plugin allumé");
        getServer().getPluginManager().registerEvents(this, this); 
    }
    
    @Override
    public void onDisable() {
		getLogger().info("[OnTimeModifier] Plugin éteint");
    }
    
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPreCommand(PlayerCommandPreprocessEvent e) {
    	
        if (e.getMessage().toLowerCase().contains("/ontime top")){
        	e.setCancelled(true);
        	
        	String[] message = e.getMessage().split(" ");
        	String leaderBoard="";
        	
        	if(message.length==2) {
        		leaderBoard = getLeaderBoard(10,"total");
        	}
        	else {
        		if(message.length==3) {
        			if(StringUtils.isStrictlyNumeric(message[2]) && Integer.valueOf(message[2])<=10){
        				leaderBoard = getLeaderBoard(Integer.valueOf(message[2]),"total");
        			}
        			else {
        				leaderBoard = "§4Vous devez entrer un nombre inférieur ou égal à 10";
        			}
        		}
        		else {
        			if(message.length==4) {
        				if(StringUtils.isStrictlyNumeric(message[2]) && Integer.valueOf(message[2])<=10){
            				if(message[3].toLowerCase().equals("playtime")) {
            					leaderBoard = getLeaderBoard(Integer.valueOf(message[2]),"total");
            				}
            				else {
            					leaderBoard = "§4Vous n'avez pas accès à cette recherche";
            				}
        				}
        				else {
        					leaderBoard = "§4Vous devez entrer un nombre inférieur ou égal à 10";
        				}
        			}
        			else {
        				if(StringUtils.isStrictlyNumeric(message[2]) && Integer.valueOf(message[2])<=10){
            				if(message[3].toLowerCase().equals("playtime")) {
            					if(Arrays.asList("today","week","month","total").contains(message[4])){
            						leaderBoard = getLeaderBoard(Integer.valueOf(message[2]),message[4]);
            					}
            					else {
            						leaderBoard = "§4Critère de recherche inconnu, les critères valides sont : today | week | month | total";
            					}
            					
            				}
            				else {
            					leaderBoard = "§4Vous n'avez pas accès à cette recherche";
            				}
        				}
        				else
        				{
        					leaderBoard = "§4Vous devez entrer un nombre inférieur ou égal à 10";
        				}
        			}
        		}
        	}
        	e.getPlayer().sendMessage(leaderBoard);
        }
    }
    
    public String getLeaderBoard(int nb, String mode) {
    	String result;
    	topData[] top;
    	
    	result = "§e -- <Moncube>  §3OnTime  §eClassement --\n";
    	
    	switch(mode) {
    		case "today":
    			result+="              §3-- Aujourd'hui -- \n";
    			top = OnTimeAPI.getTopData(OnTimeAPI.data.TODAYPLAY);
    			break;	
    		
    		case "week":
    			result+="               §3-- Semaine -- \n";
    			top = OnTimeAPI.getTopData(OnTimeAPI.data.WEEKPLAY);
    			break;
    		
    		case "month":
    			result+="                 §3-- Mois -- \n";
    			top = OnTimeAPI.getTopData(OnTimeAPI.data.MONTHPLAY);
    			break;	
    		
    		default:
    			result+="                 §3-- Total -- \n";
    			top = OnTimeAPI.getTopData(OnTimeAPI.data.TOTALPLAY);
    			break;
    	}

    	for(int i=0;i<nb;i++) {
    		if(top[i]!=null) {
    			if(top[i].getPlayerName()!=null) {
    				if(i+1<10) {
        				result+=" §f# "+(i+1)+":  §a"+top[i].getPlayerName()+"§7  ";
            		}
            		else {
            			result+=" §f#"+(i+1)+":  §a"+top[i].getPlayerName()+"§7  ";
            		}
            		
    				int jours = (int) TimeUnit.MILLISECONDS.toDays(top[i].getValue());
    				int heures = (int) (TimeUnit.MILLISECONDS.toHours(top[i].getValue()) - TimeUnit.DAYS.toHours(jours));
    				int minutes = (int) (TimeUnit.MILLISECONDS.toMinutes(top[i].getValue()) - TimeUnit.HOURS.toMinutes(heures) - TimeUnit.DAYS.toMinutes(jours));

    				result+=jours+" Jours  "+heures+" Heures  "+minutes+" Minutes  "+"\n";
    			}
    		}
    	}
    	
    	return result;
    }
    
}
