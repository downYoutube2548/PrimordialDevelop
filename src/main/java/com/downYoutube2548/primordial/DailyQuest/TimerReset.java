package com.downYoutube2548.primordial.DailyQuest;

import com.develop.primordial.primordialdevelop.PrimordialData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerReset extends TimerTask {
    public static void start() {
        TimerReset timerReset = new TimerReset();
        timerReset.a();
    }

    private void a() {
        Timer time = new Timer();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        //time.schedule(this, );
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            PrimordialData player = PrimordialData.get(p.getUniqueId());
            player.getQuestManager().reset();
        }
    }
}
