package com.sissomak.sqldemo;

import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

/**
 * LowInventoryAlert Class
 * Functionality:
 *      - Prompts user to enable SMS alerts for low inventory messages
 *
 * @author Aaron Sissom
 * @course CS-499 Computer Science Capstone
 * @school Southern New Hampshire University
 */

public class LowInventoryAlert
{
    public static AlertDialog doubleButton(final ItemList context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.sms_alert_title)
                .setIcon(R.drawable.sms_notification)
                .setCancelable(false)
                .setMessage(R.string.sms_alert_message_text)
                .setPositiveButton(R.string.sms_alert_button_enable, (dialog, arg1) -> {
                    Toast.makeText(context, "SMS alerts enabled", Toast.LENGTH_LONG).show();
                    ItemList.AllowAlerts();
                    dialog.cancel();
                })
                .setNegativeButton(R.string.sms_alert_button_disable, (dialog, arg1) -> {
                    Toast.makeText(context, "SMS alerts disabled", Toast.LENGTH_SHORT).show();
                    ItemList.BlockAlerts();
                    dialog.cancel();
                });
        return builder.create();
    }
}