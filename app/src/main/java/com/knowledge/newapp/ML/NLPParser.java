package com.knowledge.newapp.ML;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NLPParser {

    public static class ParsedResult {
        public String title;
        public String category;
        public long dateTimeMillis;
        public String raw;
    }

    public static ParsedResult parse(String input) {

        ParsedResult res = new ParsedResult();
        res.raw = input;

        Calendar cal = Calendar.getInstance();

        // ===== TEMP 5-MINUTE REMINDER =====
        cal.add(Calendar.MINUTE, 5); // reminder 5 minutes from now
        // ==================================

        /* ORIGINAL PARSING (for real NLP use)
        if (input.toLowerCase().contains("tomorrow"))
            cal.add(Calendar.DAY_OF_YEAR, 1);

        Pattern p = Pattern.compile("(\\d{1,2})\\s?(am|pm|AM|PM)");
        Matcher m = p.matcher(input);

        if (m.find()) {
            int hr = Integer.parseInt(m.group(1));
            if (m.group(2).toLowerCase().equals("pm") && hr < 12) hr += 12;

            cal.set(Calendar.HOUR_OF_DAY, hr);
            cal.set(Calendar.MINUTE, 0);
        }
        */

        res.dateTimeMillis = cal.getTimeInMillis();

        // Title & Category
        if (input.toLowerCase().contains("call"))
            res.title = "Call " + input.substring(input.toLowerCase().indexOf("call") + 4).trim();
        else
            res.title = input;

        res.category = res.title.toLowerCase().contains("call") ? "Communication" : "General";

        return res;
    }
}
